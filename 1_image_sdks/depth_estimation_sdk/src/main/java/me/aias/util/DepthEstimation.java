package me.aias.util;

import ai.djl.modality.cv.Image;
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

// https://www.paddlepaddle.org.cn/hubdetail?name=MiDaS_Small&en_category=DepthEstimation
public final class DepthEstimation {

  public DepthEstimation() {}

  public Criteria<Image, float[][]> criteria() {

    Criteria<Image, float[][]> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, float[][].class)
            .optModelPath(Paths.get("models/MiDaS_Small.zip"))
            .optTranslator(new FeatureTranslator())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }

  private final class FeatureTranslator implements Translator<Image, float[][]> {
    protected Batchifier batchifier = Batchifier.STACK;

    FeatureTranslator() {}

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
      int[] hw = resize32(256, 256);
      array = NDImageUtils.resize(array, hw[1], hw[0], Image.Interpolation.AREA);

      if (!array.getDataType().equals(DataType.FLOAT32)) {
        array = array.toType(DataType.FLOAT32, false);
      }

      array = array.transpose(2, 0, 1).div(255f); // HWC -> CHW RGB

      NDArray mean =
          ctx.getNDManager()
              .create(
                  new float[] {0.485f, 0.456f, 0.406f}, new Shape(3, 1, 1));
      NDArray std =
          ctx.getNDManager().create(new float[] {0.229f, 0.224f, 0.225f}, new Shape(3, 1, 1));
      array = array.sub(mean);
      array = array.div(std);

      return new NDList(array);
    }

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
    public Batchifier getBatchifier() {
      return batchifier;
    }

    private int[] resize32(double h, double w) {
      double min = Math.min(h, w);
      if (min < 32) {
        h = 32.0 / min * h;
        w = 32.0 / min * w;
      }
      int h32 = (int) h / 32;
      int w32 = (int) w / 32;
      return new int[] {h32 * 32, w32 * 32};
    }
  }
}
