package me.aias.example.utils;

import ai.djl.Model;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import me.aias.ClipBPETokenizer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextTranslator implements Translator<String, float[]> {

  private final int sequenceLength = 77;
  me.aias.ClipBPETokenizer tokenizer;
  @Override
  public Batchifier getBatchifier() {
    return new StackBatchifier();
  }

  @Override
  public void prepare(TranslatorContext ctx) throws IOException {
    Model model = ctx.getModel();
    URL url = model.getArtifact("bpe_simple_vocab_16e6.txt");
    tokenizer = new ClipBPETokenizer(url.getPath());
  }

  @Override
  public float[] processOutput(TranslatorContext ctx, NDList list) {
    float[] result = list.get(0).toFloatArray();
    return result;
  }

  @Override
  public NDList processInput(TranslatorContext ctx, String input) {
    List<Integer> tokens = tokenizer.encode(input);
    
    if (tokens.size() > sequenceLength - 2) {
      tokens = tokens.subList(0, sequenceLength - 2);
    }
    
    List<Integer> sot_token = tokenizer.encode("<|startoftext|>");
    List<Integer> eot_token = tokenizer.encode("<|endoftext|>");

    List<Integer> allTokens = new ArrayList<>();
    allTokens.addAll(sot_token);
    allTokens.addAll(tokens);
    allTokens.addAll(eot_token);
    
    int[] indices = allTokens.stream().mapToInt(i->i).toArray();

    int[] input_ids = new int[sequenceLength];
    Arrays.fill(input_ids, 0);
    System.arraycopy(indices, 0, input_ids, 0, indices.length);

    NDManager manager = ctx.getNDManager();
    NDArray indicesArray = manager.create(input_ids);
    return new NDList(indicesArray);
  }
}
