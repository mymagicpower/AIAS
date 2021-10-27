package me.aias.example.utils;

/**
 * The silence detector.
 */
public class SilenceDetector {
    //db
    public static final double DEFAULT_SILENCE_THRESHOLD = -70.0;
    private final double threshold;

    public SilenceDetector() {
        this(DEFAULT_SILENCE_THRESHOLD);
    }

    /**
     * Create a new silence detector with a defined threshold.
     *
     * @param silenceThreshold The threshold which defines when a buffer is silent (in dB).
     *                         Normal values are [-70.0,-30.0] dB SPL.
     */
    public SilenceDetector(final double silenceThreshold) {
        this.threshold = silenceThreshold;
    }

    /**
     * Calculates and returns the root mean square of the signal.
     *
     * @param floatBuffer The audio buffer to calculate the RMS for.
     * @return The <a
     * href="http://en.wikipedia.org/wiki/Root_mean_square">RMS</a> of
     * the signal present in the current buffer.
     */
    public static double calculateRMS(float[] floatBuffer) {
        double rms = 0.0;
        for (int i = 0; i < floatBuffer.length; i++) {
            rms += floatBuffer[i] * floatBuffer[i];
        }
        rms = rms / Double.valueOf(floatBuffer.length);
        rms = Math.sqrt(rms);
        return rms;
    }

    /**
     * Returns the dBSPL for a buffer.
     *
     * @param buffer The buffer with audio information.
     * @return The dBSPL level for the buffer.
     */
    private static double soundPressureLevel(final float[] buffer) {
        double rms = calculateRMS(buffer);
        return linearToDecibel(rms);
    }

    /**
     * Converts a linear to a dB value.
     *
     * @param value The value to convert.
     * @return The converted value.
     */
    private static double linearToDecibel(final double value) {
        return 20.0 * Math.log10(value);
    }

    double currentSPL = 0;

    public double currentSPL() {
        return currentSPL;
    }

    /**
     * Checks if the dBSPL level in the buffer falls below a certain threshold.
     *
     * @param buffer           The buffer with audio information.
     * @param silenceThreshold The threshold in dBSPL
     * @return True if the audio information in buffer corresponds with silence,
     * false otherwise.
     */
    public boolean isSilence(final float[] buffer, final double silenceThreshold) {
        currentSPL = soundPressureLevel(buffer);
        return currentSPL < silenceThreshold;
    }

    public boolean isSilence(final float[] buffer) {
        return isSilence(buffer, threshold);
    }

}