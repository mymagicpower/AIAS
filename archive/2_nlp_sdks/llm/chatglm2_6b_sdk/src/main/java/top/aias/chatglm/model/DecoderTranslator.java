package top.aias.chatglm.model;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import top.aias.chatglm.utils.CausalLMOutput;

public class DecoderTranslator implements NoBatchifyTranslator<NDList, CausalLMOutput> {
    private String tupleName;

    public DecoderTranslator() {
        tupleName = "past_key_values(" + 28 + ',' + 2 + ')';
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList input) {

        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:decoder");

        input.add(placeholder);

        return input;
    }

    @Override
    public CausalLMOutput processOutput(TranslatorContext ctx, NDList output) {
        NDArray logitsOutput = output.get(0);
        NDList pastKeyValuesOutput = output.subNDList(1, 28 * 2 + 1);

//        if (ctx.getAttachment("initialCall") != null) {
//            NDIndex index2 = new NDIndex(":, :, 1:, ...");
//            pastKeyValuesOutput =
//                    new NDList(
//                            pastKeyValuesOutput.stream()
//                                    .map(object -> object.get(index2))
//                                    .collect(Collectors.toList()));
//        }

        for (NDArray array : pastKeyValuesOutput) {
            array.setName(tupleName);
        }

        return new CausalLMOutput(logitsOutput, pastKeyValuesOutput);
    }
}