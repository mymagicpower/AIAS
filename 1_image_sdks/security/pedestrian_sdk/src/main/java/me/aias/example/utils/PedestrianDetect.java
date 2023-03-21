package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://www.paddlepaddle.org.cn/hubdetail?name=yolov3_darknet53_pedestrian&en_category=ObjectDetection
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class PedestrianDetect {

  private static final Logger logger = LoggerFactory.getLogger(PedestrianDetect.class);

  public PedestrianDetect() {}

  public Criteria<Image, DetectedObjects> criteria() {

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/pedestrian.zip"))
            .optModelName("inference")
            .optTranslator(new PedestrianTranslator())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }

  private final class PedestrianTranslator implements Translator<Image, DetectedObjects> {
    private int width;
    private int height;
    private List<String> className;

    PedestrianTranslator() {
      className = Arrays.asList("pedestrian");
    }

    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
      return processImageOutput(list);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
      array = NDImageUtils.resize(array, 608, 608);
      if (!array.getDataType().equals(DataType.FLOAT32)) {
        array = array.toType(DataType.FLOAT32, false);
      }
      array = array.div(255f);
      NDArray mean =
          ctx.getNDManager().create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
      NDArray std =
          ctx.getNDManager().create(new float[] {0.229f, 0.224f, 0.225f}, new Shape(1, 1, 3));
      array = array.sub(mean);
      array = array.div(std);

      array = array.transpose(2, 0, 1); // HWC -> CHW RGB
      array = array.expandDims(0);
      width = input.getWidth();
      height = input.getHeight();
      NDArray imageSize = ctx.getNDManager().create(new int[] {height, width});
      imageSize = imageSize.toType(DataType.INT32, false);

      imageSize = imageSize.expandDims(0);
      //      if (!imageSize.getDataType().equals(DataType.INT32)) {
      //        imageSize = imageSize.toType(DataType.INT64, false);
      //      }

      return new NDList(array, imageSize);
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
        float[] array = result.get(i).toFloatArray();
        //        [  0.          0.9627503 172.78745    22.62915   420.2703    919.949    ]
        //        [  0.          0.8364255 497.77234   161.08307   594.4088    480.63745  ]
        //        [  0.          0.7247823  94.354065  177.53668   169.24417   429.2456   ]
        //        [  0.          0.5549363  18.81821   209.29712   116.40645   471.8595   ]
        names.add(className.get((int) array[0]));
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
        boxes.add(
            new Rectangle(
                array[2] / width,
                array[3] / height,
                (array[4] - array[2]) / width,
                (array[5] - array[3]) / height));
      }
      return new DetectedObjects(names, prob, boxes);
    }
  }
}
