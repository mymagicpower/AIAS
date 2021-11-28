package me.aias.example.tacotron2;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

public class TacotronTranslator implements Translator<NDList, NDArray> {

  public TacotronTranslator() {}

  @Override
  public NDList processInput(TranslatorContext ctx, NDList input) {
    return input;
  }

  @Override
  public NDArray processOutput(TranslatorContext ctx, NDList list) {
    NDArray mels = list.get(0);
    NDArray mels_postnet = list.get(1);
    NDArray gates = list.get(2);
    NDArray alignments = list.get(3);
    //    	mels.detach();
    //    	mels_postnet.detach();
    //    	gates.detach();
    //    	alignments.detach();

    alignments = alignments.transpose(1, 0);
    gates = gates.transpose(1, 0);
    NDArray out_gate = gates.get(0);
    NDArray end_idx = out_gate.gt(0.2);
    boolean[] blidx = end_idx.toBooleanArray();
    int idx = 0;
    int size = blidx.length;
    for (int i = 0; i < size; i++) {
      if (blidx[i]) {
        idx = i;
      }
    }
    if (idx == 0) {
      // System.out.println(out_gate.toDebugString(1000000000, 1000, 1000, 1000));
      // 原来的数据是float32 argMax计算后编程了int64 转为int32对应java的int
      NDArray outg = out_gate.argMax().toType(DataType.INT32, false);
      System.out.println(outg.toDebugString(1000000000, 1000, 1000, 1000));
      int[] idxx = outg.toIntArray();
      System.out.println(Arrays.toString(idxx));
      idx = idxx[0];
    }
    if (idx == 0) {
      idx = (int) out_gate.getShape().get(0);
    }

    mels_postnet = mels_postnet.get(":, :" + idx);

    return mels_postnet;
  }

  @Override
  public Batchifier getBatchifier() {
    return Batchifier.STACK;
  }
}
