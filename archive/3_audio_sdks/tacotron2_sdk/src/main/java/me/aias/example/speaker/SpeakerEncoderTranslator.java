package me.aias.example.speaker;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
                                                      
public class SpeakerEncoderTranslator implements Translator<NDArray, NDArray> {

    public SpeakerEncoderTranslator() {

    }
    /** {@inheritDoc} */
    @Override
    public NDList processInput(TranslatorContext ctx, NDArray input) {  
        return new NDList(input);
    }

    /** {@inheritDoc} */
    @Override
    public NDArray processOutput(TranslatorContext ctx, NDList list) {
    	NDArray array = list.singletonOrThrow();
        return array;
    }

    /** {@inheritDoc} */
    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }

}
