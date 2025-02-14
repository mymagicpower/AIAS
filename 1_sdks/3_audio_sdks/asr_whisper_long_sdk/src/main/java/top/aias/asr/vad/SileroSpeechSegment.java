package top.aias.asr.vad;

import lombok.Data;

@Data
public class SileroSpeechSegment {
    float[] data;
    Float timeLength;
    Float speechProb;
    private Integer startOffset;
    private Integer endOffset;
    private Float startSecond;
    private Float endSecond;

    public SileroSpeechSegment() {
    }

    public SileroSpeechSegment(Integer startOffset, Integer endOffset, Float startSecond, Float endSecond) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.startSecond = startSecond;
        this.endSecond = endSecond;
    }
}