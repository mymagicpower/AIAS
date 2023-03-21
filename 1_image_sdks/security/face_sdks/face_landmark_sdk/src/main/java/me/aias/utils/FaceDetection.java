package me.aias.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
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
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class FaceDetection {

  private static final Logger logger = LoggerFactory.getLogger(FaceDetection.class);

  public FaceDetection() {}

  public Criteria<Image, DetectedObjects> criteria(float shrink, float threshold) {
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/face_detection.zip"))
            .optProgress(new ProgressBar())
            .optTranslator(new FaceTranslator(shrink, threshold))
            .build();

    return criteria;
  }

  private final class FaceTranslator implements Translator<Image, DetectedObjects> {

    private float shrink;
    private float threshold;
    private List<String> className;

    FaceTranslator(float shrink, float threshold) {
      this.shrink = shrink;
      this.threshold = threshold;
      className = Arrays.asList("Not Face", "Face");
    }

    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
      return processImageOutput(list, className, threshold);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      return processImageInput(ctx.getNDManager(), input, shrink);
    }

    @Override
    public Batchifier getBatchifier() {
      return null;
    }

    NDList processImageInput(NDManager manager, Image input, float shrink) {
      NDArray array = input.toNDArray(manager);
      Shape shape = array.getShape();
      array =
          NDImageUtils.resize(array, (int) (shape.get(1) * shrink), (int) (shape.get(0) * shrink));
      array = array.transpose(2, 0, 1).flip(0); // HWC -> CHW BGR -> RGB
      NDArray mean = manager.create(new float[] {104f, 117f, 123f}, new Shape(3, 1, 1));
      array = array.sub(mean).mul(0.007843f); // normalization
      array = array.expandDims(0); // make batch dimension
      return new NDList(array);
    }

    DetectedObjects processImageOutput(NDList list, List<String> className, float threshold) {
      NDArray result = list.singletonOrThrow();
      float[] probabilities = result.get(":,1").toFloatArray();
      List<String> names = new ArrayList<>();
      List<Double> prob = new ArrayList<>();
      List<BoundingBox> boxes = new ArrayList<>();
      for (int i = 0; i < probabilities.length; i++) {
        if (probabilities[i] >= threshold) {
          float[] array = result.get(i).toFloatArray();
          names.add(className.get((int) array[0]));
          prob.add((double) probabilities[i]);
          boxes.add(new Rectangle(array[2], array[3], array[4] - array[2], array[5] - array[3]));
        }
      }
      return new DetectedObjects(names, prob, boxes);
    }
  }
}
