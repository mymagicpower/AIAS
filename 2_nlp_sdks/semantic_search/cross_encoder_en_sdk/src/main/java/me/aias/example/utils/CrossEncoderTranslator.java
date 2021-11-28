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

/**
 * https://www.sbert.net/docs/pretrained_cross-encoders.html
 * https://www.sbert.net/docs/pretrained-models/ce-msmarco.html
 */
public class CrossEncoderTranslator implements Translator<String[], Float> {

  private final int maxSequenceLength = 512;
  private DefaultVocabulary vocabulary;
  private BertFullTokenizer tokenizer;

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
    tokenizer = new BertFullTokenizer(vocabulary, true);
  }

  @Override
  public Float processOutput(TranslatorContext ctx, NDList list) {
    float[] result = list.get(0).toFloatArray();
    return result[0];
  }

  @Override
  public NDList processInput(TranslatorContext ctx, String input[]) {
    // [query, paragraph]
    // input[0] = query
    // input[1] = paragraph
    List<String> tokens = tokenizer.tokenize(input[0]);
    List<String> textTokens = tokenizer.tokenize(input[1]);
    if ((tokens.size() + textTokens.size()) > maxSequenceLength - 3) {
      textTokens = textTokens.subList(0, maxSequenceLength - tokens.size() - 3);
    }

    long[] indices = tokens.stream().mapToLong(vocabulary::getIndex).toArray();
    long[] textIndices = textTokens.stream().mapToLong(vocabulary::getIndex).toArray();
    long[] input_ids = new long[tokens.size() + textTokens.size() + 3];

    input_ids[0] = vocabulary.getIndex("[CLS]");
    input_ids[tokens.size() + 1] = vocabulary.getIndex("[SEP]");
    input_ids[input_ids.length - 1] = vocabulary.getIndex("[SEP]");
    System.arraycopy(indices, 0, input_ids, 1, indices.length);
    System.arraycopy(textIndices, 0, input_ids, tokens.size() + 2, textIndices.length);

    long[] token_type_query = new long[tokens.size() + 2];
    Arrays.fill(token_type_query, 0);
    long[] token_type_ids = new long[input_ids.length];
    Arrays.fill(token_type_ids, 1);

    System.arraycopy(token_type_query, 0, token_type_ids, 0, token_type_query.length);
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
