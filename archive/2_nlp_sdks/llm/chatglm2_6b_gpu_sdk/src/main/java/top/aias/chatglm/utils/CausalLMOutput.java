package top.aias.chatglm.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

public class CausalLMOutput {
    private NDArray logits;
    private NDList pastKeyValuesList;

    public CausalLMOutput(NDArray logits, NDList pastKeyValues) {
        this.logits = logits;
        this.pastKeyValuesList = pastKeyValues;
    }

    public NDArray getLogits() {
        return logits;
    }

    public void setLogits(NDArray logits) {
        this.logits = logits;
    }

    public NDList getPastKeyValuesList() {
        return pastKeyValuesList;
    }
}