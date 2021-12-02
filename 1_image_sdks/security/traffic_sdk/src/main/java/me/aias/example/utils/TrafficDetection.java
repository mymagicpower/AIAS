package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// https://www.paddlepaddle.org.cn/hubdetail?name=ssd_vgg16_512_coco2017&en_category=ObjectDetection

// /Users/calvin/Desktop/Download/browser/PaddleHub-release-v2.1/modules/image/object_detection/ssd_vgg16_512_coco2017

public final class TrafficDetection {

  private static final Logger logger = LoggerFactory.getLogger(TrafficDetection.class);

  private TrafficDetection() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String imagePath = "src/test/resources/ped_vec2.jpeg";
    BufferedImage img = ImageIO.read(new File(imagePath));
    Image image = ImageFactory.getInstance().fromImage(img);

    DetectedObjects detections = TrafficDetection.predict(image);

    saveBoundingBoxImage(image, detections, "result.png", "build/output");

    logger.info("{}", detections);
  }

  public static DetectedObjects predict(Image img)
      throws IOException, ModelException, TranslateException {
    img.getWrappedImage();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/traffic.zip")
            //                    .optModelUrls("/Users/calvin/model/ssd_vgg16_512_coco2017/")
            .optModelName("inference")
            .optTranslator(new TrafficTranslator())
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    try (ZooModel model = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
        DetectedObjects objects = predictor.predict(img);
        return objects;
      }
    }
  }

  private static final class TrafficTranslator implements Translator<Image, DetectedObjects> {

    private List<String> className;

    TrafficTranslator() {}

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
      Model model = ctx.getModel();
      try (InputStream is = model.getArtifact("label_file.txt").openStream()) {
        className = Utils.readLines(is, true);
        //            classes.add(0, "blank");
        //            classes.add("");
      }
    }

    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
      return processImageOutput(list);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
      array = NDImageUtils.resize(array, 512, 512);
      if (!array.getDataType().equals(DataType.FLOAT32)) {
        array = array.toType(DataType.FLOAT32, false);
      }
      //      array = array.div(255f);
      NDArray mean = ctx.getNDManager().create(new float[] {104f, 117f, 123f}, new Shape(1, 1, 3));
      NDArray std = ctx.getNDManager().create(new float[] {1f, 1f, 1f}, new Shape(1, 1, 3));
      array = array.sub(mean);
      array = array.div(std);

      array = array.transpose(2, 0, 1); // HWC -> CHW RGB
      array = array.expandDims(0);

      return new NDList(array);
    }

    @Override
    public Batchifier getBatchifier() {
      return null;
    }

    DetectedObjects processImageOutput(NDList list) {
      NDArray result = list.singletonOrThrow();
      float[] probabilities = result.get(":,1").toFloatArray();
      List<String> names = new ArrayList<>();
      List<Double> prob = new ArrayList<>();
      List<BoundingBox> boxes = new ArrayList<>();
      for (int i = 0; i < probabilities.length; i++) {
        if (probabilities[i] < 0.55) continue;

        float[] array = result.get(i).toFloatArray();
        //        [  0.          0.9627503 172.78745    22.62915   420.2703    919.949    ]
        //        [  0.          0.8364255 497.77234   161.08307   594.4088    480.63745  ]
        //        [  0.          0.7247823  94.354065  177.53668   169.24417   429.2456   ]
        //        [  0.          0.5549363  18.81821   209.29712   116.40645   471.8595   ]
        // 1-person 行人 2-bicycle 自行车 3-car 小汽车 4-motorcycle 摩托车 6-bus 公共汽车 8-truck 货车

        int index = (int) array[0];
        if (index != 1 && index != 2 && index != 3 && index != 4 && index != 6 && index != 8)
          continue;

        names.add(className.get(index));
        // array[0] category_id
        // array[1] confidence
        // bbox
        // array[2]
        // array[3]
        // array[4]
        // array[5]
        prob.add((double) probabilities[i]);
        // x, y , w , h
        // dt['left'], dt['top'], dt['right'], dt['bottom'] = clip_bbox(bbox, org_img_width,
        // org_img_height)
        boxes.add(new Rectangle(array[2], array[3], array[4] - array[2], array[5] - array[3]));
      }
      return new DetectedObjects(names, prob, boxes);
    }
  }

  private static void saveBoundingBoxImage(
      Image img, DetectedObjects detection, String name, String path) throws IOException {
    // Make image copy with alpha channel because original image was jpg
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK can't save jpg with alpha channel
    img.save(Files.newOutputStream(imagePath), "png");
  }
}
