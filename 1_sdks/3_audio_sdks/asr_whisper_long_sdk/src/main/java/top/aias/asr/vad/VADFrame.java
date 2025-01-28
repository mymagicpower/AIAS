package top.aias.asr.vad;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

import java.util.Map;

public class VADFrame {
    private float[] frame;
    private boolean isSpeech;

    public VADFrame(float[] frame, boolean isSpeech) {
        this.frame = frame;
        this.isSpeech = isSpeech;
    }

    public float[] getFrame() {
        return frame;
    }

    public void setFrame(float[] frame) {
        this.frame = frame;
    }

    public boolean isSpeech() {
        return isSpeech;
    }

    public void setSpeech(boolean speech) {
        isSpeech = speech;
    }
}