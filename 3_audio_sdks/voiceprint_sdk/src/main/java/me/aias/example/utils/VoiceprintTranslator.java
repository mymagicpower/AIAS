package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class VoiceprintTranslator implements Translator<float[][], float[]> {
  VoiceprintTranslator() {}

  @Override
  public NDList processInput(TranslatorContext ctx, float[][] mag) {
    NDManager manager = ctx.getNDManager();

    int spec_len = 257;
    NDArray magNDArray = manager.create(mag);

    NDArray spec_mag = magNDArray.get(":, :" + spec_len);

    // 按列计算均值
    // Calculate the mean by column
    NDArray mean = spec_mag.mean(new int[] {0}, true);
    NDArray std = manager.create(JLibrasaEx.std(spec_mag, mean)).reshape(1, spec_len);

    spec_mag = spec_mag.sub(mean).div(std.add(1e-5));
    spec_mag = spec_mag.expandDims(0); // (1,257,201)
    spec_mag = spec_mag.expandDims(0); // (1,1,257,201)

    return new NDList(spec_mag);
  }

  @Override
  public float[] processOutput(TranslatorContext ctx, NDList list) {
    NDArray feature = list.singletonOrThrow();

    return feature.toFloatArray();
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }
}
