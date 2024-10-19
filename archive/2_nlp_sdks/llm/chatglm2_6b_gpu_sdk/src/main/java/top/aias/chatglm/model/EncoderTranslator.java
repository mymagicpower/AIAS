package top.aias.chatglm.model;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import top.aias.chatglm.utils.CausalLMOutput;

import java.util.Arrays;

/**
 *
 */
public class EncoderTranslator implements NoBatchifyTranslator<int[], CausalLMOutput> {

    private String tupleName;

    public EncoderTranslator() {
        tupleName = "past_key_values(" + 28 + ',' + 2 + ')';
    }

    @Override
    public NDList processInput(TranslatorContext ctx, int[] input) throws Exception {
        NDManager manager = ctx.getNDManager();
        // 64790, 64792
        // prefix_tokens = [self.get_command("[gMASK]"), self.get_command("sop")]
        // {'[MASK]': 64789, '[gMASK]': 64790, '[sMASK]': 64791, 'eop': 64793, 'sop': 64792}
        int[] inputIds = new int[input.length + 2];
        inputIds[0] = 64790;
        inputIds[1] = 64792;
        System.arraycopy(input, 0, inputIds, 2, input.length);
        NDArray inputIdArray = manager.create(inputIds).expandDims(0).toType(DataType.INT64, false);
        inputIdArray.setName("input_ids");

        NDArray positionIds =
                manager.arange(0, inputIdArray.getShape().getLastDimension(), 1, DataType.INT64)
                        .expandDims(0);

        long[] attentionMask = new long[inputIds.length];
        Arrays.fill(attentionMask, 1);
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);
        attentionMaskArray.setName("attention_mask");

        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:encoder");

        return new NDList(inputIdArray, positionIds, attentionMaskArray, placeholder);
    }

    @Override
    public CausalLMOutput processOutput(TranslatorContext ctx, NDList list) {
        NDArray logitsOutput = list.get(0);
        NDList pastKeyValuesOutput = list.subNDList(1, 28 * 2 + 1);
        for (NDArray array : pastKeyValuesOutput) {
            array.setName(tupleName);
        }
        logitsOutput.detach();
        pastKeyValuesOutput.detach();
        return new CausalLMOutput(logitsOutput, pastKeyValuesOutput);
    }

}