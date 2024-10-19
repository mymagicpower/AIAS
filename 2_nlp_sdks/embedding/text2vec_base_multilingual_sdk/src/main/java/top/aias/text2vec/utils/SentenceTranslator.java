package top.aias.text2vec.utils;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.StackBatchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

/**
 * 句向量模型前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SentenceTranslator implements Translator<String, float[]> {
    private final HuggingFaceTokenizer tokenizer;

    public SentenceTranslator(HuggingFaceTokenizer tokenizer) {
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
