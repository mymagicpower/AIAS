package top.aias.nllb.generate;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

/**
 * 贪婪搜索张量对象列表
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class GreedyBatchTensorList extends BatchTensorList {
    // [batch, 1]
    private NDArray nextInputIds;

    private NDArray pastOutputIds;

    private NDArray encoderHiddenStates;
    private NDArray attentionMask;
    private NDList pastKeyValues;

    public GreedyBatchTensorList(
            NDArray nextInputIds,
            NDArray pastOutputIds,
            NDList pastKeyValues,
            NDArray encoderHiddenStates,
            NDArray attentionMask) {
        this.nextInputIds = nextInputIds;
        this.pastKeyValues = pastKeyValues;
        this.pastOutputIds = pastOutputIds;
        this.attentionMask = attentionMask;
        this.encoderHiddenStates = encoderHiddenStates;
    }

    public GreedyBatchTensorList() {}

    public BatchTensorList fromList(NDList inputList, long[] seqDimOrder) {
        return new GreedyBatchTensorList();
    }

    public NDList getList() {
        return new NDList();
    }

    public NDArray getNextInputIds() {
        return nextInputIds;
    }

    public void setNextInputIds(NDArray nextInputIds) {
        this.nextInputIds = nextInputIds;
    }
    public NDArray getPastOutputIds() {
        return pastOutputIds;
    }

    public void setPastOutputIds(NDArray pastOutputIds) {
        this.pastOutputIds = pastOutputIds;
    }

    public NDList getPastKeyValues() {
        return pastKeyValues;
    }

    public void setPastKeyValues(NDList pastKeyValues) {
        this.pastKeyValues = pastKeyValues;
    }

    public NDArray getEncoderHiddenStates() {
        return encoderHiddenStates;
    }

    public void setEncoderHiddenStates(NDArray encoderHiddenStates) {
        this.encoderHiddenStates = encoderHiddenStates;
    }

    public NDArray getAttentionMask() {
        return attentionMask;
    }

    public void setAttentionMask(NDArray attentionMask) {
        this.attentionMask = attentionMask;
    }
}