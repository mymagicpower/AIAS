package top.aias.text2vec.utils;

import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.Vocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模型前后处理
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SentenceTranslator implements Translator<String, float[]> {
    private final int maxSequenceLength = 128;
    private Vocabulary vocabulary;
    private String vocab;
    BertFullTokenizer tokenizer;


    public SentenceTranslator() throws IOException {
        this.vocab = "vocab.txt";
    }

    @Override
    public Batchifier getBatchifier() {
        return new StackBatchifier();
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        vocabulary =
                DefaultVocabulary.builder()
                        .optMinFrequency(1)
                        .addFromTextFile(ctx.getModel().getArtifact(vocab))
                        .optUnknownToken("[UNK]")
                        .build();

        tokenizer = new BertFullTokenizer(vocabulary, false);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, String input) {
        List<String> result = tokenizer.tokenize(input);
        if (result.size() > maxSequenceLength - 2) {
            result = result.subList(0, maxSequenceLength - 2);
        }
        List<String> tokens = new ArrayList<>();
        for (String token : result) {
            tokens.add(token.replace("##",""));
        }
        long[] indices = tokens.stream().mapToLong(vocabulary::getIndex).toArray();
        long[] input_ids = new long[tokens.size() + 2];
        input_ids[0] = vocabulary.getIndex("[CLS]");
        input_ids[input_ids.length - 1] = vocabulary.getIndex("[SEP]");
        System.arraycopy(indices, 0, input_ids, 1, indices.length);

        NDManager manager = ctx.getNDManager();
        NDArray indicesArray = manager.create(input_ids);
        indicesArray.setName("input_ids");

        //    long[] token_type_ids = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        long[] tokenTypeIds = new long[input_ids.length];
        Arrays.fill(tokenTypeIds, 0);
        NDArray tokenIdsArray = manager.create(tokenTypeIds);
        tokenIdsArray.setName("token_type_ids");

        //    long[] attention_mask = new long[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        long[] attentionMask = new long[input_ids.length];
        Arrays.fill(attentionMask, 1);
        NDArray attentionMaskArray = manager.create(attentionMask);
        attentionMaskArray.setName("attention_mask");

        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:encoder");

        return new NDList(indicesArray, tokenIdsArray, attentionMaskArray, placeholder);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        NDArray array = list.get(0);
        float[] result = array.toFloatArray();
        return result;
    }


}
