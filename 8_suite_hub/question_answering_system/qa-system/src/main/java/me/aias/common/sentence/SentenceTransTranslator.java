package me.aias.common.sentence;

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
 * 模型预处理/后处理
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public class SentenceTransTranslator implements Translator<String, float[]> {

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
        tokenizer = new BertFullTokenizer(vocabulary, false);
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
        List<String> tokens = tokenizer.tokenize(input);
        if (tokens.size() > maxSequenceLength - 2) {
            tokens = tokens.subList(0, maxSequenceLength - 2);
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
        //        input_features = {'input_ids': input_ids, 'token_type_ids': input_type_ids,
        // 'attention_mask': input_mask}
        //        input_ids
        //        tensor([[  101 [CLS],  2023 this,  7705 framework, 19421 generates,  7861 em,
        //        8270 ##bed,  4667 ##ding,  2015 ##s,  2005 for,  2169 each, 7953 input,  6251
        // sentence,   102 [SEP]]])
        //        token_type_ids
        //        tensor([[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]])
        //        attention_mask
        //        tensor([[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]])

        //    long[] input_ids =
        //        new long[] {101, 2023, 7705, 19421, 7861, 8270, 4667, 2015, 2005, 2169, 7953, 6251,
        // 102};
        NDArray indicesArray = manager.create(input_ids);
        indicesArray.setName("input.input_ids");

        //    long[] token_type_ids = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        NDArray tokenIdsArray = manager.create(token_type_ids);
        tokenIdsArray.setName("input.token_type_ids");

        //    long[] attention_mask = new long[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        NDArray attentionMaskArray = manager.create(attention_mask);
        attentionMaskArray.setName("input.attention_mask");
        return new NDList(indicesArray, attentionMaskArray);
    }
}
