package me.aias.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

public final class BigGANTranslator implements Translator<Long, Image> {

  private float truncation;
  private int size;

  public BigGANTranslator(int size, float truncation) {
    this.size = size;
    this.truncation = truncation;
  }

  @Override
  public Image processOutput(TranslatorContext ctx, NDList list) {
    NDArray output = list.get(0).addi(1).muli(128).clip(0, 255).toType(DataType.UINT8, false);

    Image image = ImageFactory.getInstance().fromNDArray(output.get(0));

    return image;
  }

  @Override
  public NDList processInput(TranslatorContext ctx, Long imageClass) throws Exception {
    NDManager manager = ctx.getNDManager();

    NDArray seed =
        manager.truncatedNormal(new Shape(1, this.size)).clip(-2.0, 2.0).muli(truncation);

    return new NDList(seed, manager.create(imageClass).expandDims(0), manager.create(truncation));
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }
}
