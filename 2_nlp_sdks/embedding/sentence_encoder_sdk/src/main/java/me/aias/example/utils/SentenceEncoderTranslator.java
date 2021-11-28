package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class SentenceEncoderTranslator implements Translator<String[], float[][]> {

    public SentenceEncoderTranslator() {}

    @Override
    public NDList processInput(TranslatorContext ctx, String[] inputs) {
      NDManager manager = ctx.getNDManager();
      NDList inputsList =
          new NDList(Arrays.stream(inputs).map(manager::create).collect(Collectors.toList()));
      return new NDList(NDArrays.stack(inputsList));
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
      return null;
    }
  }