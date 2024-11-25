package me.aias.example.utils;

import ai.djl.Model;
import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.paddlepaddle.engine.PpNDArray;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class ReviewTranslator implements Translator<String, float[]> {
  ReviewTranslator() {}

  private DefaultVocabulary vocabulary;
  private BertFullTokenizer tokenizer;
  private Map<String, String> word2id_dict = new HashMap<String, String>();
  private String unk_id = "";
  private String pad_id = "";
  private int maxLength = 256;

  @Override
  public void prepare(TranslatorContext ctx) throws IOException {
    Model model = ctx.getModel();
    try (InputStream is = model.getArtifact("assets/word_dict.txt").openStream()) {
      List<String> words = Utils.readLines(is, true);
      for (int i = 0; i < words.size(); i++) {
        word2id_dict.put(words.get(i), "" + i); // 文字是key,id是value - Text is the key, ID is the value.
      }
    }
    unk_id = "" + word2id_dict.get("<UNK>"); // 文字是key,id是value - Text is the key, ID is the value.
    pad_id = "" + word2id_dict.get("<PAD>"); // 文字是key,id是value - Text is the key, ID is the value.

    vocabulary =
            DefaultVocabulary.builder()
            .optMinFrequency(1)
            .addFromTextFile(model.getArtifact("assets/vocab.txt"))
            // .addFromTextFile(vocabPath)
            .optUnknownToken("<UNK>")
            .build();
    tokenizer = new BertFullTokenizer(vocabulary, false);
  }

  @Override
  public NDList processInput(TranslatorContext ctx, String input) {

    NDManager manager = ctx.getNDManager();
    List<Long> lodList = new ArrayList<>(0);
    lodList.add(new Long(0));
    List<Long> sh = tokenizeSingleString(manager, input, lodList);

    int size = Long.valueOf(lodList.get(lodList.size() - 1)).intValue();
    long[] array = new long[size];
    for (int i = 0; i < size; i++) {
      if (sh.size() > i) {
        array[i] = sh.get(i);
      } else {
        array[i] = 0;
      }
    }
    NDArray ndArray = manager.create(array, new Shape(lodList.get(lodList.size() - 1), 1));

    ndArray.setName("words");
    long[][] lod = new long[1][2];
    lod[0][0] = 0;
    lod[0][1] = lodList.get(lodList.size() - 1);
    ((PpNDArray) ndArray).setLoD(lod);
    return new NDList(ndArray);
  }

  @Override
  public float[] processOutput(TranslatorContext ctx, NDList list) {
    // index = 0 negative
    // index = 1 positive
    // [0.05931241 0.9406876 ]
    float[] result = list.get(0).toFloatArray();
    return result;
  }

  private List<Long> tokenizeSingleString(NDManager manager, String input, List<Long> lod) {
    List<Long> word_ids = new ArrayList<>();
    List<String> list = tokenizer.tokenize(input);
    for (String word : list) {
      word = word.replace("#", "");
      String word_id = word2id_dict.get(word);
      word_ids.add(Long.valueOf(StringUtils.isBlank(word_id) ? unk_id : word_id));
    }
    if (word_ids.size() < maxLength) {
      int diff = maxLength - word_ids.size();
      for (int i = 0; i < diff; i++) {
        word_ids.add(Long.parseLong(pad_id));
      }
    }
    lod.add((long) word_ids.size());
    return word_ids;
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }
}
