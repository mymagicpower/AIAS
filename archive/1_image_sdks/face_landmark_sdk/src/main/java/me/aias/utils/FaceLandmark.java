package me.aias.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class FaceLandmark {

  private static final Logger logger = LoggerFactory.getLogger(FaceLandmark.class);

  public FaceLandmark() {}

  public Criteria<Image, float[][]> criteria() {
    Criteria<Image, float[][]> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, float[][].class)
            .optModelPath(Paths.get("models/face_landmark.zip"))
            .optModelName("inference")
            .optProgress(new ProgressBar())
            .optTranslator(new FaceLandmarkTranslator())
            .build();

    return criteria;
  }

  private final class FaceLandmarkTranslator implements Translator<Image, float[][]> {

    FaceLandmarkTranslator() {}

    @Override
    public float[][] processOutput(TranslatorContext ctx, NDList list) {
      NDList result = new NDList();
      long numOutputs = list.singletonOrThrow().getShape().get(0);
      for (int i = 0; i < numOutputs; i++) {
        result.add(list.singletonOrThrow().get(i));
      }
      return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      // 转灰度图
      // Convert to grayscale image
      NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.GRAYSCALE);
      Shape shape = array.getShape();

      array = NDImageUtils.resize(array, 60, 60, Image.Interpolation.BICUBIC);

      NDArray mean = array.mean();
      double std = std(array);

      array = array.transpose(2, 0, 1); // HWC -> CHW
      array = array.sub(mean).div(std); // normalization
      array = array.expandDims(0); // make batch dimension
      return new NDList(array);
    }

    @Override
    public Batchifier getBatchifier() {
      return null;
    }
  }

  // 计算全局标准差
  // Calculate the global standard deviation
  private float std(NDArray points) {
    byte[] arr = points.toByteArray();
    float std = 0;
    for (int i = 0; i < arr.length; i++) {
      std = std + (float) Math.pow(arr[i], 2);
    }
    std = (float) Math.sqrt(std / arr.length);
    return std;
  }
}
