package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import com.jlibrosa.audio.JLibrosa;

public class AudioUtils {
    static int mel_window_step = 10;
    static float max_gain_db = 300.0f;
    static int sample_rate = 16000;
    static float eps = 1e-14f;

    /**
     * 创建给定持续时间和采样率的静音音频段
     *
     * @param manager
     * @param duration   : 静音音频段长度，单位 second
     * @param sampleRate : 采样率
     * @return
     */
    public static NDArray makeSilence(NDManager manager, long duration, int sampleRate) {
        NDArray samples = manager.zeros(new Shape(duration * sampleRate));
        return samples;
    }

    /**
     * 在这个音频样本上加一段静音
     *
     * @param manager
     * @param wav
     * @param padLength
     * @param sides     : padding 位置:
     *                  'beginning' - 增加静音片段到开头
     *                  'end'       - 增加静音片段到末尾
     *                  'both'      - 两边都增加静音片段
     * @return
     * @throws Exception
     */
    public static NDArray padSilence(NDManager manager, NDArray wav, long padLength, String sides) throws Exception {
        NDArray pad = manager.zeros(new Shape(padLength));
        if (sides.equals("beginning")) {
            wav = pad.concat(wav);
        } else if (sides.equals("end")) {
            wav = wav.concat(pad);
        } else if (sides.equals("both")) {
            wav = pad.concat(wav);
            wav = wav.concat(pad);
        } else {
            throw new Exception("Unknown value for the sides " + sides);
        }

        return wav;
    }

    /**
     * 将任意数量的语音片段连接在一起
     *
     * @param segments : 要连接的输入语音片段
     * @return
     */
    public static NDArray concatenate(NDList segments) {
        NDArray array = segments.get(0);
        for (int i = 1; i < segments.size(); i++) {
            array = array.concat(segments.get(i));
        }
        return array;
    }

    /**
     * 生成以分贝为单位的音频均方根能量
     * Root mean square energy in decibels.
     *
     * @param samples
     * @return
     */
    public static float rmsDb(NDArray samples) {
        samples = samples.pow(2);
        samples = samples.mean();
        samples = samples.log10().mul(10);
        return samples.toFloatArray()[0];
    }

    /**
     * 将音频归一化，使其具有所需的有效值(以分贝为单位)
     * Target RMS value in decibels. This value should be
     * less than 0.0 as 0.0 is full-scale audio.
     *
     * @param samples
     * @param target_db
     * @return
     * @throws Exception
     */
    public static NDArray normalize(NDArray samples, float target_db) {
        float gain = target_db - rmsDb(samples);
        gain = Math.min(gain, max_gain_db);
        //对音频施加分贝增益
        //Gain in decibels to apply to samples
        float factor = (float) Math.pow(10f, gain / 20f);
        samples = samples.mul(factor);
        return samples;
    }

    /**
     * 用快速傅里叶变换计算线性谱图
     *
     * @param manager
     * @param samples
     * @param stride_ms
     * @param window_ms
     * @return
     */
    public static NDArray linearSpecgram(NDManager manager, NDArray samples, float stride_ms, float window_ms) {
        int strideSize = (int) (0.001 * sample_rate * stride_ms);
        int windowSize = (int) (0.001 * sample_rate * window_ms);
        long truncateSize = (samples.size() - windowSize) % strideSize;
        long len = samples.size() - truncateSize;
        samples = samples.get(":" + len);

        //Shape nshape = new Shape(windowSize, (samples.size() - windowSize) / strideSize + 1);    // 320 ,838
        //nstrides = (samples.strides[0], samples.strides[0] * stride_size)
        //strides[0] = 4 个字节, 由于已经转为float类型，所以对应当前samples中一个元素
        //np.lib.stride_tricks.as_strided(samples, shape=nshape, strides=nstrides)
        int rows = windowSize; //320
        int cols = ((int) samples.size() - windowSize) / strideSize + 1; //838

        float[] floatArray = samples.toFloatArray();
        float[][] windows = new float[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                windows[row][col] = floatArray[row + col * strideSize];
            }
        }

        // 快速傅里叶变换
        float[] weighting = hanningWindow(windowSize);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                windows[row][col] = windows[row][col] * weighting[row];
            }
        }

        double[] arr = null;
        NDList fftList = new NDList();
        for (int col = 0; col < cols; col++) {
            arr = new double[rows];
            for (int row = 0; row < rows; row++) {
                arr[row] = windows[row][col];
            }
            double[] fft = FFT.fft(arr);
            float[][] complex = FFT.rfft(fft);

            NDArray array = manager.create(FFT.abs(complex));
            fftList.add(array);
        }

        NDArray fft = NDArrays.stack(fftList).transpose();
        fft = fft.pow(2);

        NDArray weightingArray = manager.create(weighting);

        weightingArray = weightingArray.pow(2);
        NDArray scale = weightingArray.sum().mul(sample_rate);

        NDArray middle = fft.get("1:-1,:");
        middle = middle.mul(2).div(scale);
        NDArray head = fft.get("0,:").div(scale).reshape(1, fft.getShape().get(1));
        NDArray tail = fft.get("-1,:").div(scale).reshape(1, fft.getShape().get(1));
        NDList list = new NDList(head, middle, tail);
        fft = NDArrays.concat(list, 0);

        NDArray freqsArray = manager.arange(fft.getShape().get(0));
        freqsArray = freqsArray.mul(sample_rate / windowSize);

        float[] freqs = freqsArray.toFloatArray();
        int ind = 0;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] <= (sample_rate / 2)) {
                ind = i;
            } else {
                break;
            }
        }
        ind = ind + 1;

        fft = fft.get(":" + ind + ",:").add(eps);
        fft = fft.log();

//        System.out.println(fft.toDebugString(1000000000, 1000, 10, 1000));
        return fft;
    }

    /**
     * Hanning窗
     * The Hanning window is a taper formed by using a weighted cosine.
     *
     * @param size
     * @return
     */
    public static float[] hanningWindow(int size) {
        float[] data = new float[size];
        for (int n = 1; n < size; n++) {
            data[n] = (float) (0.5 * (1 - Math.cos((2 * Math.PI * n)
                    / (size - 1))));
        }
        return data;
    }

    /**
     * Hanning窗
     * The Hanning window is a taper formed by using a weighted cosine.
     *
     * @param recordedData
     * @return
     */
    public static float[] hanningWindow(float[] recordedData) {
        for (int n = 1; n < recordedData.length; n++) {
            recordedData[n] *= 0.5 * (1 - Math.cos((2 * Math.PI * n)
                    / (recordedData.length - 1)));
        }
        return recordedData;
    }

    /**
     * 从wav提取mel频谱特征值
     *
     * @param samples
     * @param n_fft   1024
     * @param n_mels  40
     * @return
     */
    public static float[][] melSpecgram(NDArray samples, int n_fft, int n_mels) {
        JLibrosa librosa = new JLibrosa();
        float[][] melSpectrogram =
                librosa.generateMelSpectroGram(samples.toFloatArray(), sample_rate, n_fft, n_mels, (sample_rate * mel_window_step / 1000));
        return melSpectrogram;
    }
}
