package top.aias.asr.vad;

import lombok.Data;

@Data
public class Segment {
    private float[] frame;
    private double time;

    public Segment(float[] frame, double time) {
        this.frame = frame;
        this.time = time;
    }
}