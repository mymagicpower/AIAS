package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

public class WaveGlowTranslator implements Translator<NDArray, NDArray> {

    public WaveGlowTranslator() {

    }
    @Override
    public NDList processInput(TranslatorContext ctx, NDArray input) {
        NDArray sigma = ctx.getNDManager().create(1.0);
        NDList list = new NDList();
        list.add(input);
        list.add(sigma);
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
