package top.aias.code2vec.utils;

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
 * 向量模型前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class codet5p2VecTranslator implements Translator<String, float[]> {
    private final HuggingFaceTokenizer tokenizer;
    public codet5p2VecTranslator(HuggingFaceTokenizer tokenizer){
        this.tokenizer = tokenizer;
    }

    @Override
    public Batchifier getBatchifier() {
        return new StackBatchifier();
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, String input) {
        Encoding encoding = tokenizer.encode(input);

        NDManager manager = ctx.getNDManager();
        NDArray indicesArray = manager.create(encoding.getIds());
        indicesArray.setName("input_ids");
        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:encoder");
        return new NDList(indicesArray, placeholder);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        NDArray array = list.get(0);
        float[] result = array.toFloatArray();
        return result;
    }


}
