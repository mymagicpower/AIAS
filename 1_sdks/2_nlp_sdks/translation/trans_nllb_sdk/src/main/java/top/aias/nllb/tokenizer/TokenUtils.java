package top.aias.nllb.tokenizer;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import top.aias.nllb.generate.SearchConfig;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Token工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TokenUtils {

    private TokenUtils() {
    }

    /**
     * 语言解码
     *
     * @param tokenizer
     * @param output
     * @return
     */
    public static String decode(SearchConfig config, HuggingFaceTokenizer tokenizer, NDArray output) {
        long[] outputIds = output.toLongArray();
        ArrayList<Long> outputIdsList = new ArrayList<>();

        for (long id : outputIds) {
            if (id == config.getEosTokenId() || id==config.getSrcLangId() || id==config.getForcedBosTokenId()) {
                continue;
            }
            outputIdsList.add(id);
        }

        Long[] objArr =  outputIdsList.toArray(new Long[0]);
        long[] ids = new long[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            ids[i] = objArr[i];
        }
        String text = tokenizer.decode(ids);

        return text;
    }
}