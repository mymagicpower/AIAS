package top.aias.vad.utils;

public class Audio {

    private byte[] data;
    private float sampleRate;
    private int channels;

    public Audio(byte[] data) {
        this.data = data;
    }

    /**
     * Constructs a new {@code Audio} instance.
     *
     * @param data the wrapped float array data
     * @param sampleRate the sample rate
     * @param channels number of channels
     */
    public Audio(byte[] data, float sampleRate, int channels) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    /**
     * Returns the float array data.
     *
     * @return The float array data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the sample rate.
     *
     * @return sample rate.
     */
    public float getSampleRate() {
        return sampleRate;
    }

}