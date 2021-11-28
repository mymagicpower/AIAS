package me.aias.example.utils;

import ai.djl.Model;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class AudioTranslator implements Translator<NDArray, Pair> {
  AudioTranslator() {}

  private List<String> vocabulary = null;

  @Override
  public void prepare(TranslatorContext ctx) throws IOException {
    Model model = ctx.getModel();
    try (InputStream is = model.getArtifact("zh_vocab.txt").openStream()) {
      vocabulary = Utils.readLines(is, true);
    }
  }

  @Override
  public NDList processInput(TranslatorContext ctx, NDArray audioFeature) {
    NDManager manager = ctx.getNDManager();

    long audio_len = audioFeature.getShape().get(1);
    long mask_shape0 = (audioFeature.getShape().get(0) - 1) / 2 + 1;
    long mask_shape1 = (audioFeature.getShape().get(1) - 1) / 3 + 1;
    long mask_max_len = (audio_len - 1) / 3 + 1;

    NDArray mask_ones = manager.ones(new Shape(mask_shape0, mask_shape1));
    NDArray mask_zeros = manager.zeros(new Shape(mask_shape0, mask_max_len - mask_shape1));
    NDArray maskArray = NDArrays.concat(new NDList(mask_ones, mask_zeros), 1);
    maskArray = maskArray.reshape(1, mask_shape0, mask_max_len);
    NDList list = new NDList();
    for (int i = 0; i < 32; i++) {
      list.add(maskArray);
    }
    NDArray mask = NDArrays.concat(list, 0);

    NDArray audio_data = audioFeature.expandDims(0);
    NDArray seq_len_data = manager.create(new long[] {audio_len});
    NDArray masks = mask.expandDims(0);
    //    System.out.println(maskArray.toDebugString(1000000000, 1000, 10, 1000));
    return new NDList(audio_data, seq_len_data, masks);
  }

  @Override
  public Pair processOutput(TranslatorContext ctx, NDList list) {
    NDArray probs_seq = list.singletonOrThrow();
    Pair pair = CTCGreedyDecoder.greedyDecoder(ctx.getNDManager(), probs_seq, vocabulary, 0);
    return pair;
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }
}
