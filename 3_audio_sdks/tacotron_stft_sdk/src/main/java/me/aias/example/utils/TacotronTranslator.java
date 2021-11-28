package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import sun.net.www.content.audio.wav;

public class TacotronTranslator implements Translator<NDArray, NDArray> {

    public TacotronTranslator() {

    }
    /** {@inheritDoc} */
    @Override
    public NDList processInput(TranslatorContext ctx, NDArray input) {
        input = input.expandDims(0);
        return new NDList(input.get(0));
    }

    /** {@inheritDoc} */
    @Override
    public NDArray processOutput(TranslatorContext ctx, NDList list) {
    	NDArray melspec = list.singletonOrThrow();
        melspec = melspec.squeeze(0);
        return melspec;
    }

    /** {@inheritDoc} */
    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }

}
