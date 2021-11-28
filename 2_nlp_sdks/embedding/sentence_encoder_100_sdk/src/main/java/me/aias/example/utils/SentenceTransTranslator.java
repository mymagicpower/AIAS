package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.Arrays;

public class SentenceTransTranslator implements Translator<String, float[]> {

  private SpProcessor processor;
  private final int maxSequenceLength = 128;

  @Override
  public Batchifier getBatchifier() {
    return new StackBatchifier();
  }

  public SentenceTransTranslator(SpProcessor processor) {
    this.processor = processor;
  }

  @Override
  public float[] processOutput(TranslatorContext ctx, NDList list) {
    NDArray array = null;
    // 下面的排序非固定，每次运行顺序可能会变
    //  input_ids
    //  token_type_ids
    //  attention_mask
    //  token_embeddings: (13, 384) cpu() float32
    //  cls_token_embeddings: (384) cpu() float32
    //  sentence_embedding: (384) cpu() float32

    for (NDArray ndArray : list) {
      String name = ndArray.getName();
      if (name.equals("sentence_embedding")) {
        array = ndArray;
        break;
      }
    }

    float[] result = array.toFloatArray();
    return result;
  }

  @Override
  public NDList processInput(TranslatorContext ctx, String input) {
    int[] ids = processor.encode(input);

    int length = ids.length;
    if (ids.length > maxSequenceLength - 2) {
      length = maxSequenceLength - 2;
    }
    long[] input_ids = new long[length + 2];
    input_ids[0] = 0;
    input_ids[input_ids.length - 1] = 2;

    //编号跟原算法编号差1，可能是由于首行处理不同造成的
    long[] newIds = Arrays.stream(ids).map(e -> (e + 1)).mapToLong(t->(long)t).toArray();

    System.arraycopy(newIds, 0, input_ids, 1, length);

    long[] attention_mask = new long[input_ids.length];
    Arrays.fill(attention_mask, 1);

    NDManager manager = ctx.getNDManager();

    NDArray indicesArray = manager.create(input_ids);
    indicesArray.setName("input.input_ids");

    NDArray attentionMaskArray = manager.create(attention_mask);
    attentionMaskArray.setName("input.attention_mask");
    return new NDList(indicesArray, attentionMaskArray);
  }
}
