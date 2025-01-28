package top.aias.platform.generate;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
/**
 * 解码输出对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class LMOutput {
    private NDArray logits;
    private NDList pastKeyValuesList;

    public LMOutput(NDArray logits, NDList pastKeyValues) {
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