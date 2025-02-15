package top.aias.platform.model.vad;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.TranslateException;
import ai.onnxruntime.OrtException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SileroVadDetector implements AutoCloseable{
    private final SileroVadModel model;
    private final float threshold;
    private final float negThreshold;
    private final int samplingRate;
    private final int windowSizeSample;
    private final float minSpeechSamples;
    private final float speechPadSamples;
    private final float maxSpeechSamples;
    private final float minSilenceSamples;
    private final float minSilenceSamplesAtMaxSpeech;
    private int audioLengthSamples;
    private static final float THRESHOLD_GAP = 0.15f;
    private static final Integer SAMPLING_RATE_8K = 8000;
    private static final Integer SAMPLING_RATE_16K = 16000;

    // Define private variable Session
    private float[][] context;
    // Define the last sample rate
    private int lastSr = 0;
    // Define the last batch size
    private int lastBatchSize = 0;
    // Define a list of supported sample rates
    private static final List<Integer> SAMPLE_RATES = Arrays.asList(8000, 16000);
    private NDArray stateArray;
    private NDManager manager;

    /**
     * Constructor
     * @param model silero-vad onnx model
     * @param threshold threshold for speech start
     * @param samplingRate audio sampling rate, only available for [8k, 16k]
     * @param minSpeechDurationMs Minimum speech length in millis, any speech duration that smaller than this value would not be considered as speech
     * @param maxSpeechDurationSeconds Maximum speech length in millis, recommend to be set as Float.POSITIVE_INFINITY
     * @param minSilenceDurationMs Minimum silence length in millis, any silence duration that smaller than this value would not be considered as silence
     * @param speechPadMs Additional pad millis for speech start and end
     * @throws OrtException
     */
    public SileroVadDetector(SileroVadModel model, float threshold, int samplingRate,
                             int minSpeechDurationMs, float maxSpeechDurationSeconds,
                             int minSilenceDurationMs, int speechPadMs) throws OrtException {
        if (samplingRate != SAMPLING_RATE_8K && samplingRate != SAMPLING_RATE_16K) {
            throw new IllegalArgumentException("Sampling rate not support, only available for [8000, 16000]");
        }
        this.model = model;
        this.samplingRate = samplingRate;
        this.threshold = threshold;
        this.negThreshold = threshold - THRESHOLD_GAP;
        if (samplingRate == SAMPLING_RATE_16K) {
            this.windowSizeSample = 512;
        } else {
            this.windowSizeSample = 256;
        }
        this.minSpeechSamples = samplingRate * minSpeechDurationMs / 1000f;
        this.speechPadSamples = samplingRate * speechPadMs / 1000f;
        this.maxSpeechSamples = samplingRate * maxSpeechDurationSeconds - windowSizeSample - 2 * speechPadSamples;
        this.minSilenceSamples = samplingRate * minSilenceDurationMs / 1000f;
        this.minSilenceSamplesAtMaxSpeech = samplingRate * 98 / 1000f;
        this.manager = NDManager.newBaseManager(Device.cpu(), "PyTorch");

        this.reset();
    }

    /**
     * Get speech segment list by given wav-format file
     * @param wavFile wav file
     * @return list of speech segment
     */
    public List<SileroSpeechSegment> getSpeechSegmentList(File wavFile) {
        reset();
        try (AudioInputStream audioInputStream =  AudioSystem.getAudioInputStream(wavFile)){
            List<Float> speechProbList = new ArrayList<>();
            this.audioLengthSamples = audioInputStream.available() / 2;
            byte[] data = new byte[this.windowSizeSample * 2];
            int numBytesRead = 0;

            while ((numBytesRead = audioInputStream.read(data)) != -1) {
                if (numBytesRead <= 0) {
                    break;
                }
                // Convert the byte array to a float array
                float[] audioData = new float[data.length / 2];
                for (int i = 0; i < audioData.length; i++) {
                    audioData[i] = ((data[i * 2] & 0xff) | (data[i * 2 + 1] << 8)) / 32767.0f;
                }

                float speechProb = 0;
                try {
                    speechProb = this.call(new float[][]{audioData}, samplingRate)[0];
                    speechProbList.add(speechProb);
                } catch (OrtException e) {
                    throw e;
                }
            }
            return calculateProb(speechProbList);
        } catch (Exception e) {
            throw new RuntimeException("SileroVadDetector getSpeechTimeList with error", e);
        }
    }

    /**
     * Method to reset the state
     */
    public void reset() {
        resetStates();
    }

    /**
     * Get speech segment list by given wav-format file
     * @param url wav url
     * @return list of speech segment
     */
    public List<SileroSpeechSegment> getSpeechSegmentList(URL url) {
        reset();
        try (AudioInputStream audioInputStream =  AudioSystem.getAudioInputStream(url)){
            List<Float> speechProbList = new ArrayList<>();
            this.audioLengthSamples = audioInputStream.available() / 2;
            byte[] data = new byte[this.windowSizeSample * 2];
            int numBytesRead = 0;

            while ((numBytesRead = audioInputStream.read(data)) != -1) {
                if (numBytesRead <= 0) {
                    break;
                }
                // Convert the byte array to a float array
                float[] audioData = new float[data.length / 2];
                for (int i = 0; i < audioData.length; i++) {
                    audioData[i] = ((data[i * 2] & 0xff) | (data[i * 2 + 1] << 8)) / 32767.0f;
                }

                float speechProb = 0;
                try {
                    speechProb = this.call(new float[][]{audioData}, samplingRate)[0];
                    speechProbList.add(speechProb);
                } catch (OrtException e) {
                    throw e;
                }
            }
            return calculateProb(speechProbList);
        } catch (Exception e) {
            throw new RuntimeException("SileroVadDetector getSpeechTimeList with error", e);
        }
    }

    /**
     * Get speech segment list by given wav-format file
     * @param inputStream wav file inputStream
     * @return list of speech segment
     */
    public List<SileroSpeechSegment> getSpeechSegmentList(InputStream inputStream) {
        reset();
        try (AudioInputStream audioInputStream =  AudioSystem.getAudioInputStream(inputStream)){
            List<Float> speechProbList = new ArrayList<>();
            this.audioLengthSamples = audioInputStream.available() / 2;
            byte[] data = new byte[this.windowSizeSample * 2];
            int numBytesRead = 0;

            while ((numBytesRead = audioInputStream.read(data)) != -1) {
                if (numBytesRead <= 0) {
                    break;
                }
                // Convert the byte array to a float array
                float[] audioData = new float[data.length / 2];
                for (int i = 0; i < audioData.length; i++) {
                    audioData[i] = ((data[i * 2] & 0xff) | (data[i * 2 + 1] << 8)) / 32767.0f;
                }

                float speechProb = 0;
                try {
                    speechProb = this.call(new float[][]{audioData}, samplingRate)[0];
                    speechProbList.add(speechProb);
                } catch (OrtException e) {
                    throw e;
                }
            }
            return calculateProb(speechProbList);
        } catch (Exception e) {
            throw new RuntimeException("SileroVadDetector getSpeechTimeList with error", e);
        }
    }

    /**
     * Calculate speech segement by probability
     * @param speechProbList speech probability list
     * @return list of speech segment
     */
    private List<SileroSpeechSegment> calculateProb(List<Float> speechProbList) {
        List<SileroSpeechSegment> result = new ArrayList<>();
        boolean triggered = false;
        int tempEnd = 0, prevEnd = 0, nextStart = 0;
        SileroSpeechSegment segment = new SileroSpeechSegment();

        for (int i = 0; i < speechProbList.size(); i++) {
            Float speechProb = speechProbList.get(i);
            if (speechProb >= threshold && (tempEnd != 0)) {
                tempEnd = 0;
                if (nextStart < prevEnd) {
                    nextStart = windowSizeSample * i;
                }
            }

            if (speechProb >= threshold && !triggered) {
                triggered = true;
                segment.setStartOffset(windowSizeSample * i);
                continue;
            }

            if (triggered && (windowSizeSample * i) - segment.getStartOffset() > maxSpeechSamples) {
                if (prevEnd != 0) {
                    segment.setEndOffset(prevEnd);
                    result.add(segment);
                    segment = new SileroSpeechSegment();
                    if (nextStart < prevEnd) {
                        triggered = false;
                    }else {
                        segment.setStartOffset(nextStart);
                    }
                    prevEnd = 0;
                    nextStart = 0;
                    tempEnd = 0;
                }else {
                    segment.setEndOffset(windowSizeSample * i);
                    result.add(segment);
                    segment = new SileroSpeechSegment();
                    prevEnd = 0;
                    nextStart = 0;
                    tempEnd = 0;
                    triggered = false;
                    continue;
                }
            }

            if (speechProb < negThreshold && triggered) {
                if (tempEnd == 0) {
                    tempEnd = windowSizeSample * i;
                }
                if (((windowSizeSample * i) - tempEnd) > minSilenceSamplesAtMaxSpeech) {
                    prevEnd = tempEnd;
                }
                if ((windowSizeSample * i) - tempEnd < minSilenceSamples) {
                    continue;
                }else {
                    segment.setEndOffset(tempEnd);
                    if ((segment.getEndOffset() - segment.getStartOffset()) > minSpeechSamples) {
                        result.add(segment);
                    }
                    segment = new SileroSpeechSegment();
                    prevEnd = 0;
                    nextStart = 0;
                    tempEnd = 0;
                    triggered = false;
                    continue;
                }
            }
        }

        if (segment.getStartOffset() != null && (audioLengthSamples - segment.getStartOffset()) > minSpeechSamples) {
            segment.setEndOffset(audioLengthSamples);
            result.add(segment);
        }

        for (int i = 0; i < result.size(); i++) {
            SileroSpeechSegment item = result.get(i);
            if (i == 0) {
                item.setStartOffset((int)(Math.max(0,item.getStartOffset() - speechPadSamples)));
            }
            if (i != result.size() - 1) {
                SileroSpeechSegment nextItem = result.get(i + 1);
                Integer silenceDuration = nextItem.getStartOffset() - item.getEndOffset();
                if(silenceDuration < 2 * speechPadSamples){
                    item.setEndOffset(item.getEndOffset() + (silenceDuration / 2 ));
                    nextItem.setStartOffset(Math.max(0, nextItem.getStartOffset() - (silenceDuration / 2)));
                } else {
                    item.setEndOffset((int)(Math.min(audioLengthSamples, item.getEndOffset() + speechPadSamples)));
                    nextItem.setStartOffset((int)(Math.max(0,nextItem.getStartOffset() - speechPadSamples)));
                }
            }else {
                item.setEndOffset((int)(Math.min(audioLengthSamples, item.getEndOffset() + speechPadSamples)));
            }
        }

        return mergeListAndCalculateSecond(result, samplingRate);
    }

    private List<SileroSpeechSegment> mergeListAndCalculateSecond(List<SileroSpeechSegment> original, Integer samplingRate) {
        List<SileroSpeechSegment> result = new ArrayList<>();
        if (original == null || original.size() == 0) {
            return result;
        }
        Integer left = original.get(0).getStartOffset();
        Integer right = original.get(0).getEndOffset();
        if (original.size() > 1) {
            original.sort(Comparator.comparingLong(SileroSpeechSegment::getStartOffset));
            for (int i = 1; i < original.size(); i++) {
                SileroSpeechSegment segment = original.get(i);

                if (segment.getStartOffset() > right) {
                    result.add(new SileroSpeechSegment(left, right,
                            calculateSecondByOffset(left, samplingRate), calculateSecondByOffset(right, samplingRate)));
                    left = segment.getStartOffset();
                    right = segment.getEndOffset();
                } else {
                    right = Math.max(right, segment.getEndOffset());
                }
            }
            result.add(new SileroSpeechSegment(left, right,
                    calculateSecondByOffset(left, samplingRate), calculateSecondByOffset(right, samplingRate)));
        }else {
            result.add(new SileroSpeechSegment(left, right,
                    calculateSecondByOffset(left, samplingRate), calculateSecondByOffset(right, samplingRate)));
        }
        return result;
    }

    private Float calculateSecondByOffset(Integer offset, Integer samplingRate) {
        float secondValue = offset * 1.0f / samplingRate;
        return (float) Math.floor(secondValue * 1000.0f) / 1000.0f;
    }

    /**
     * 调用模型
     *
     * @param x
     * @param sr
     * @return
     * @throws OrtException
     */
    public float[] call(float[][] x, int sr) throws OrtException {
        ValidationResult result = validateInput(x, sr);
        x = result.x;
        sr = result.sr;
        int numberSamples = 256;
        if (sr == 16000) {
            numberSamples = 512;
        }

        if (x[0].length != numberSamples) {
            throw new IllegalArgumentException("Provided number of samples is " + x[0].length + " (Supported values: 256 for 8000 sample rate, 512 for 16000)");
        }

        int batchSize = x.length;

        int contextSize = 32;
        if (sr == 16000) {
            contextSize = 64;
        }

        if (lastBatchSize == 0) {
            resetStates();
        }
        if (lastSr != 0 && lastSr != sr) {
            resetStates();
        }
        if (lastBatchSize != 0 && lastBatchSize != batchSize) {
            resetStates();
        }

        if (context.length == 0) {
            context = new float[batchSize][contextSize];
        }

        x = concatenate(context, x);

        try {
            NDArray audioFeature = manager.create(x).toType(DataType.FLOAT32, true);
            audioFeature.setName("input");
            NDArray sampling_rate = manager.create(new long[]{16000}).toType(DataType.INT64, true);
            sampling_rate.setName("sr");
            stateArray.setName("state");
            NDList list = new NDList(audioFeature, sampling_rate, stateArray);

            NDList predictResult = model.predict(list);

            NDArray output = predictResult.get(0);
            float[] outputArr = output.toFloatArray();

            stateArray  = predictResult.get(1);

            context = getLastColumns(x, contextSize);
            lastSr = sr;
            lastBatchSize = batchSize;
            return outputArr;

        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重置状态
     */
    public void resetStates() {
        stateArray = manager.zeros(new Shape(2, 1, 128), DataType.FLOAT32);
        context = new float[0][];
        lastSr = 0;
        lastBatchSize = 0;
    }

    public void close(){
        model.close();
        manager.close();
    }

    public static class ValidationResult {
        public final float[][] x;
        public final int sr;

        // Constructor
        public ValidationResult(float[][] x, int sr) {
            this.x = x;
            this.sr = sr;
        }
    }

    /**
     * Function to validate input data
     */
    private ValidationResult validateInput(float[][] x, int sr) {
        // Process the input data with dimension 1
        if (x.length == 1) {
            x = new float[][]{x[0]};
        }
        // Throw an exception when the input data dimension is greater than 2
        if (x.length > 2) {
            throw new IllegalArgumentException("Incorrect audio data dimension: " + x[0].length);
        }

        // Process the input data when the sample rate is not equal to 16000 and is a multiple of 16000
        if (sr != 16000 && (sr % 16000 == 0)) {
            int step = sr / 16000;
            float[][] reducedX = new float[x.length][];

            for (int i = 0; i < x.length; i++) {
                float[] current = x[i];
                float[] newArr = new float[(current.length + step - 1) / step];

                for (int j = 0, index = 0; j < current.length; j += step, index++) {
                    newArr[index] = current[j];
                }

                reducedX[i] = newArr;
            }

            x = reducedX;
            sr = 16000;
        }

        // If the sample rate is not in the list of supported sample rates, throw an exception
        if (!SAMPLE_RATES.contains(sr)) {
            throw new IllegalArgumentException("Only supports sample rates " + SAMPLE_RATES + " (or multiples of 16000)");
        }

        // If the input audio block is too short, throw an exception
        if (((float) sr) / x[0].length > 31.25) {
            throw new IllegalArgumentException("Input audio is too short");
        }

        // Return the validated result
        return new ValidationResult(x, sr);
    }

    private static float[][] concatenate(float[][] a, float[][] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("The number of rows in both arrays must be the same.");
        }

        int rows = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        float[][] result = new float[rows][colsA + colsB];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(a[i], 0, result[i], 0, colsA);
            System.arraycopy(b[i], 0, result[i], colsA, colsB);
        }

        return result;
    }

    private static float[][] getLastColumns(float[][] array, int contextSize) {
        int rows = array.length;
        int cols = array[0].length;

        if (contextSize > cols) {
            throw new IllegalArgumentException("contextSize cannot be greater than the number of columns in the array.");
        }

        float[][] result = new float[rows][contextSize];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(array[i], cols - contextSize, result[i], 0, contextSize);
        }

        return result;
    }

}