package top.aias.model.generate;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
/**
 * beam 搜索张量对象列表
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class BeamBatchTensorList {
    private NDArray nextInputIds;
    private NDArray encoderHiddenStates;
    private NDArray attentionMask;
    private NDList pastKeyValues;


    public BeamBatchTensorList() {
    }

    public BeamBatchTensorList(NDArray nextInputIds, NDArray attentionMask, NDArray encoderHiddenStates, NDList pastKeyValues) {
        this.nextInputIds = nextInputIds;
        this.attentionMask = attentionMask;
        this.pastKeyValues = pastKeyValues;
        this.encoderHiddenStates = encoderHiddenStates;
    }

    public NDArray getNextInputIds() {
        return nextInputIds;
    }

    public void setNextInputIds(NDArray nextInputIds) {
        this.nextInputIds = nextInputIds;
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

    public NDList getPastKeyValues() {
        return pastKeyValues;
    }

    public void setPastKeyValues(NDList pastKeyValues) {
        this.pastKeyValues = pastKeyValues;
    }
}
