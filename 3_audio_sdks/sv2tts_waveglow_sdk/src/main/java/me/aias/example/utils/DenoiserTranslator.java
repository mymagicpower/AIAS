package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

public class DenoiserTranslator implements Translator<NDArray, NDArray> {

    public DenoiserTranslator() {

    }
    @Override
    public NDList processInput(TranslatorContext ctx, NDArray input) {
        NDArray denoiser_strength = ctx.getNDManager().create(1.0f);
        NDList list = new NDList();

		/*NDList dim = new NDList();
		dim.add(wav);
		NDArray adddim = NDArrays.stack(dim);*/

        list.add(input);
        list.add(denoiser_strength);
        return list;
    }

    @Override
    public NDArray processOutput(TranslatorContext ctx, NDList list) {  
    	NDArray ret = list.singletonOrThrow(); 
    	ret.detach();
        return ret;
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }

}
