package top.aias.platform.model.trans;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;

import java.util.Arrays;

/**
 * 编码器前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class EncoderTranslator implements NoBatchifyTranslator<long[], NDArray> {


    public EncoderTranslator() {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, long[] input) throws Exception {
        NDManager manager = ctx.getNDManager();

        NDArray inputIdArray = manager.create(input).expandDims(0);
        inputIdArray.setName("input_ids");

        long[] attentionMask = new long[input.length];
        Arrays.fill(attentionMask, 1);
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);
        attentionMaskArray.setName("attention_mask");

        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:encoder");

        return new NDList(inputIdArray, attentionMaskArray, placeholder);
    }

    @Override
    public NDArray processOutput(TranslatorContext ctx, NDList list) {
        NDArray encoderHiddenStates = list.get(0);
        encoderHiddenStates.detach();
        return encoderHiddenStates;
    }

}