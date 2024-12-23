package top.aias.whisper.utils;

import ai.djl.modality.nlp.Vocabulary;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

import java.util.Map;

public class DecoderOutput {
    private String text;
    private NDArray outputId;
    private Vocabulary vocabulary;
    Map<String, Integer> byteDecoder;

    private NDList pastKeyValuesList;

    public DecoderOutput(String text, NDArray outputId, Vocabulary vocabulary, Map<String, Integer> byteDecoder, NDList pastKeyValues) {
        this.text = text;
        this.outputId = outputId;
        this.vocabulary = vocabulary;
        this.byteDecoder = byteDecoder;
        this.pastKeyValuesList = pastKeyValues;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Map<String, Integer> getByteDecoder() {
        return byteDecoder;
    }

    public void setByteDecoder(Map<String, Integer> byteDecoder) {
        this.byteDecoder = byteDecoder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NDArray getOutputId() {
        return outputId;
    }

    public void setOutputId(NDArray outputId) {
        this.outputId = outputId;
    }

    public void setPastKeyValuesList(NDList pastKeyValuesList) {
        this.pastKeyValuesList = pastKeyValuesList;
    }

    /**
     * Returns the value of the pastKeyValuesList.
     *
     * @return the value of pastKeyValuesList
     */
    public NDList getPastKeyValuesList() {
        return pastKeyValuesList;
    }
}