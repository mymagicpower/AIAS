package top.aias.m3e.utils;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;

/**
 * @author Calvin
 * @email 179209347@qq.com
 **/

public class SentenceTranslator implements Translator<String, float[]> {

    private final HuggingFaceTokenizer tokenizer;
    public SentenceTranslator(HuggingFaceTokenizer tokenizer){
        this.tokenizer = tokenizer;
    }

    @Override
    public Batchifier getBatchifier() {
        return new StackBatchifier();
    }

    @Override
    public void prepare(TranslatorContext ctx) {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, String input) {
        Encoding encoding = tokenizer.encode(input);

        NDManager manager = ctx.getNDManager();
        NDArray indicesArray = manager.create(encoding.getIds());
        indicesArray.setName("input_ids");

        //    long[] token_type_ids = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        NDArray tokenIdsArray = manager.create(encoding.getTypeIds());
        tokenIdsArray.setName("token_type_ids");

        //    long[] attention_mask = new long[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        NDArray attentionMaskArray = manager.create(encoding.getAttentionMask());
        attentionMaskArray.setName("attention_mask");

        return new NDList(indicesArray, tokenIdsArray, attentionMaskArray);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        NDArray array = list.get(0);
        float[] result = array.toFloatArray();
        return result;
    }


}
