package top.aias.asr.vad;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.Map;

/**
 * 语音检测前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SileroVADTranslator implements Translator<NDList, NDList> {
    public SileroVADTranslator(Map<String, ?> arguments) {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList list) {
        return list;
    }

    @Override
    public NDList processOutput(TranslatorContext ctx, NDList list) {
        NDArray output = list.get(0);
        float[] f1 = output.toFloatArray();

        NDArray hn  = list.get(1);
        float[] f2 = hn.toFloatArray();

        NDArray cn  = list.get(2);
        float[] f3 = cn.toFloatArray();

        return list;
    }

    @Override
    public Batchifier getBatchifier() {
        return null;
    }
}
