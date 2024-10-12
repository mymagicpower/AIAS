package me.aias.example.utils;

import org.jtransforms.fft.DoubleFFT_1D;

/**
 * A Fast Fourier Transform wrapper for jTransforms to provide similar functionality to numpy.fft
 * functions used by the Blowhole Python implementation.
 */
public class FFT {

    /**
     * Compute the fast fourier transform
     *
     * @param raw the raw signal
     * @return the computed fast fourier transform
     */
    public static double[] fft(double[] raw) {
        double[] in = raw;
        DoubleFFT_1D fft = new DoubleFFT_1D(in.length);
        fft.realForward(in);
        return in;
    }

    /**
     * Computes the physical layout of the fast fourier transform.
     * See jTransform documentation for more information.
     * http://incanter.org/docs/parallelcolt/api/edu/emory/mathcs/jtransforms/fft/DoubleFFT_1D.html#realForward(double[])
     *
     * @param fft the fast fourier transform
     */
    public static float[][] rfft(double[] fft) {
        float[][] result = null;

        int n = fft.length;
        if (n % 2 == 0) {
            // n is even
            result = new float[2][n / 2 + 1];
            for (int i = 0; i < n / 2; i++) {
                result[0][i] = (float) fft[2 * i]; //the real part fo the fast fourier transform
                result[1][i] = (float) fft[2 * i + 1]; //the imaginary part of the fast fourier transform
            }
            result[1][0] = 0;
            result[0][n / 2] = (float) fft[1];
        } else {
            // n is odd
            result = new float[2][(n + 1) / 2];
            for (int i = 0; i < n / 2; i++) {
                result[0][i] = (float) fft[2 * i];  //the real part fo the fast fourier transform
                result[1][i] = (float) fft[2 * i + 1];  //the imaginary part of the fast fourier transform
            }
            result[1][0] = 0;
            result[1][(n - 1) / 2] = (float) fft[1];

        }

        return result;
    }


    public static float[] abs(float[][] complex) {
        float[] re = complex[0]; //the real part fo the fast fourier transform
        float[] im = complex[1]; //the imaginary part of the fast fourier transform
        float[] abs = new float[re.length];
        for (int i = 0; i < re.length; i++) {
            abs[i] = (float) Math.hypot(re[i], im[i]);
        }
        return abs;
    }

    /**
     * Returns the Discrete Fourier Transform sample frequencies.
     * See numpy.fft.rfftfreq for more information.
     *
     * @param n Window length
     * @param d Sample spacing
     * @return Array of length n + 1 containing the sample frequencies
     */
    public static double[] rfftfreq(int n, double d) {
        double val = 1.0 / (n * d);
        int N = n / 2 + 1;
        double[] results = new double[N];
        for (int i = 0; i < N; i++) {
            results[i] = i * val;
        }
        return results;
    }
}