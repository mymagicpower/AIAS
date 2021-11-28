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

public class QATranslator implements Translator<String[], float[]> {

  //  private Vocabulary vocabulary;
  //  private BertTokenizer tokenizer; //不切分subword
  private final int maxSequenceLength = 128;
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
    //    tokenizer = new BertTokenizer();
    tokenizer = new BertFullTokenizer(vocabulary, true);
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
  public NDList processInput(TranslatorContext ctx, String input[]) {
    long[] indices = null;
    long[] input_ids = null;
    List<String> tokens = null;
    if (input.length == 1) {
      tokens = tokenizer.tokenize(input[0]);
      if (tokens.size() > maxSequenceLength - 2) {
        tokens = tokens.subList(0, maxSequenceLength - 2);
      }
      indices = tokens.stream().mapToLong(vocabulary::getIndex).toArray();
      input_ids = new long[tokens.size() + 2];
      input_ids[0] = vocabulary.getIndex("[CLS]");
      input_ids[input_ids.length - 1] = vocabulary.getIndex("[SEP]");

      System.arraycopy(indices, 0, input_ids, 1, indices.length);
    }else{
      //[title, text]
      //input[0] = title
      //input[1] = text
      tokens = tokenizer.tokenize(input[0]);
      if (tokens.size() > maxSequenceLength - 2) {
        tokens = tokens.subList(0, maxSequenceLength - 2);
      }

      List<String> textTokens = tokenizer.tokenize(input[1]);
      if (textTokens.size() > maxSequenceLength - 2) {
        textTokens = textTokens.subList(0, maxSequenceLength - 2);
      }
      indices = tokens.stream().mapToLong(vocabulary::getIndex).toArray();
      long[] textIndices = textTokens.stream().mapToLong(vocabulary::getIndex).toArray();
      input_ids = new long[tokens.size() + textTokens.size() + 3];
      input_ids[0] = vocabulary.getIndex("[CLS]");
      input_ids[tokens.size() + 1] = vocabulary.getIndex("[SEP]");
      input_ids[input_ids.length - 1] = vocabulary.getIndex("[SEP]");
      System.arraycopy(indices, 0, input_ids, 1, indices.length);
      System.arraycopy(textIndices, 0, input_ids, tokens.size() + 2, textIndices.length);
    }

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
