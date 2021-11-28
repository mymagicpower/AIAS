package me.aias.example.utils;

import ai.djl.Model;
import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextTranslator implements Translator<String, float[]> {

  private final int maxSequenceLength = 512;
  private DefaultVocabulary vocabulary;
  private BertFullTokenizer tokenizer;
  private boolean isChinese = false;

  public TextTranslator(boolean isChinese) {
    this.isChinese = isChinese;
  }

  @Override
  public Batchifier getBatchifier() {
    return new StackBatchifier();
  }

  @Override
  public void prepare(TranslatorContext ctx) throws IOException {
    Model model = ctx.getModel();
    URL url = model.getArtifact("vocab.txt");
    vocabulary =
            DefaultVocabulary.builder()
            .optMinFrequency(1)
            .addFromTextFile(url)
            .optUnknownToken("[UNK]")
            .build();
    tokenizer = new BertFullTokenizer(vocabulary, false);
  }

  @Override
  public float[] processOutput(TranslatorContext ctx, NDList list) {
    float[] result = list.get(0).toFloatArray();
    return result;
  }

  @Override
  public NDList processInput(TranslatorContext ctx, String input) {
    List<String> tokens = tokenizer.tokenize(input);
    if (tokens.size() > maxSequenceLength - 2) {
      tokens = tokens.subList(0, maxSequenceLength - 2);
    }
    if(isChinese){
      //原切词tokenizer 中文切词时，没有##
      tokens = tokens.stream().map(e -> e.replace("##", "")).collect(Collectors.toList());
    }
    long[] indices = tokens.stream().mapToLong(vocabulary::getIndex).toArray();
    long[] input_ids = new long[tokens.size() + 2];
    input_ids[0] = vocabulary.getIndex("[CLS]");
    input_ids[input_ids.length - 1] = vocabulary.getIndex("[SEP]");

    System.arraycopy(indices, 0, input_ids, 1, indices.length);

    long[] token_type_ids = new long[input_ids.length];
    Arrays.fill(token_type_ids, 0);
    long[] attention_mask = new long[input_ids.length];
    Arrays.fill(attention_mask, 1);

    NDManager manager = ctx.getNDManager();
    NDArray indicesArray = manager.create(input_ids);
    indicesArray.setName("input.input_ids");

    NDArray tokenIdsArray = manager.create(token_type_ids);
    tokenIdsArray.setName("input.token_type_ids");

    NDArray attentionMaskArray = manager.create(attention_mask);
    attentionMaskArray.setName("input.attention_mask");
    return new NDList(indicesArray, tokenIdsArray, attentionMaskArray);
  }
}
