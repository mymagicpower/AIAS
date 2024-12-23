package top.aias.asr.vad;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.TranslateException;

import java.util.*;

/**
 * 对音频预处理的工具: 静音切除，音频分段
 * Tools for audio preprocessing: silence removal, audio segmentation
 *
 * @author Calvin <179209347@qq.com>
 */
public class SileroVadUtils {

    /**
     * 生成音频帧
     *
     * @param data
     * @param frameDurationMs
     * @param sampleRate
     * @return
     */
    public static List<float[]> generateFrames(float[] data, int frameDurationMs, float sampleRate) {
        List<float[]> list = new ArrayList<>();
        int offset = 0;
        // 每帧数据量：sampleRate 采样率是每秒钟采样的数量，frameDurationMs 是每帧的时间长度，单位是毫秒
        int n = (int) (sampleRate * (frameDurationMs / 1000.0));
        int length = data.length;
        while (offset + n < length) {
            float[] frame = Arrays.copyOfRange(data, offset, offset + n);
            offset += n;
            list.add(frame);
        }
        return list;
    }

    /**
     *
     * @param manager ndArray 管理器，自动管理内存
     * @param vadModel 静音检测模型
     * @param data 声音数据
     * @param frame_duration_ms 帧时间长度
     * @param num_unvoiced 静音片段数量
     * @return
     * @throws TranslateException
     */
    public static List<float[]> generateSegments(NDManager manager, SileroVADModel vadModel, float[] data, int frame_duration_ms , int num_unvoiced) throws TranslateException {
        // window_size_samples [512, 1024, 1536] for 16000 sampling_rate
        // window_size_samples = (frame_duration_ms / 10000) * sampling_rate
        // frame_duration_ms = window_size_samples / (sampling_rate / 1000)
        // frame_duration_ms: 512/16 = 32, 1024/16 = 64, 1536/16 = 96
        // frame_duration_ms 只能取 32, 64, 96

        // 30秒采样数据量 ：480000 = 16000 * 30 ( maxLength = sampleRate * 30 )
        int  maxLength = 480000;
        // Segment 不能超过 30 秒的采样数据量，不足 30 秒，DecoderTranslator PadOrTrim(480000) 会自动补齐。
        // frame_duration_ms 取 32 : 480000 / 32 = 15000
        // frame_duration_ms 取 64 : 480000 / 64 = 7500
        // frame_duration_ms 取 96 : 480000 / 96 = 5000
        int slidingWindow = maxLength / frame_duration_ms;

        List<float[]> frames = SileroVadUtils.generateFrames(data, frame_duration_ms, 16000);

        float test_threshold = 0.5f;
        NDArray hOrt = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);
        NDArray cOrt = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);

        List<VADFrame> vadFrames = new ArrayList<>();

        for (float[] frame : frames) {
            boolean isSpeech = false;
            NDArray audioFeature = manager.create(frame).reshape(1, frame.length).toType(DataType.FLOAT32, true);
            NDArray sampling_rate = manager.create(new int[]{16000}).toType(DataType.INT64, true);
            NDList list = new NDList(audioFeature, sampling_rate, hOrt, cOrt);
            NDList result = vadModel.vad(list);

            NDArray output = result.get(0);
            float[] scores = output.toFloatArray();
            if (scores[0] >= test_threshold) {
                isSpeech = true;
            }

            hOrt = result.get(1);
            cOrt = result.get(2);

            vadFrames.add(new VADFrame(frame, isSpeech));
        }

        List<VADFrame> slidingWindowFrames;
        List<float[]> segments = new ArrayList<>();

        if (vadFrames.size() > slidingWindow) {
            slidingWindowFrames = vadFrames.subList(0, slidingWindow);
        } else {
            segments.add(data);
            return segments;
        }

        while (vadFrames.size() > slidingWindow) {
            // 判断末尾的 num_unvoiced 个 frame 是否是静音
            int length = slidingWindowFrames.size();
            if (length < num_unvoiced) {
                break;
            }

            for (int i = length; i >= num_unvoiced; i = i - num_unvoiced) {
                List<VADFrame> subList = slidingWindowFrames.subList(length - num_unvoiced, i);

                boolean pass = true;
                for (VADFrame item : subList) {
                    if (item.isSpeech()) {
                        pass = false;
                        break;
                    }
                }

                if(pass){
                    List<VADFrame> list = slidingWindowFrames.subList(0, i);
                    float[] segment = generateSegment(list);
                    segments.add(segment);

                    // 切除已处理的数据
                    vadFrames = vadFrames.subList(i, vadFrames.size());

                    if (vadFrames.size() > slidingWindow) {
                        slidingWindowFrames = vadFrames.subList(0, slidingWindow);
                    } else {
                        slidingWindowFrames = vadFrames;
                    }

                    break;
                }
            }
        }


        return segments;
    }

    /**
     * 生成声音切片
     *
     * @param frames
     * @return
     */
    private static float[] generateSegment(List<VADFrame> frames) {
        int size = 0;
        for (VADFrame item : frames) {
            size += item.getFrame().length;
        }

        float[] audioData = new float[size];
        int index = 0;
        for (VADFrame item : frames) {
            for (int i = 0; i < item.getFrame().length; i++) {
                audioData[index++] = item.getFrame()[i];
            }
        }

        return audioData;
    }

    /**
     * 静音切除, 并切片
     *
     * @param manager
     * @param vadModel
     * @param data
     * @param padding_duration_ms
     * @param frame_duration_ms
     * @return
     * @throws Exception
     */
    public static Queue<float[]> cropAudioVad(
            NDManager manager, SileroVADModel vadModel, float[] data, int padding_duration_ms, int frame_duration_ms) throws Exception {
        List<float[]> frames = SileroVadUtils.generateFrames(data, frame_duration_ms, 16000);
        Queue<float[]> segments = SileroVadUtils.vadCollector(manager, vadModel, frames, padding_duration_ms, frame_duration_ms);
        return segments;
    }

    /**
     * 静音切除
     * Filters out non-voiced audio frames.
     *
     * @param manager
     * @param vadModel
     * @param frames
     * @param padding_duration_ms
     * @param frame_duration_ms
     * @return
     * @throws TranslateException
     */
    public static Queue<float[]> vadCollector(
            NDManager manager, SileroVADModel vadModel, List<float[]> frames, int padding_duration_ms, int frame_duration_ms) throws TranslateException {
        Queue<float[]> segments = new LinkedList<>();
        Queue<float[]> voicedFrames = new LinkedList<>();

        int num_padding_frames = (int) (padding_duration_ms / frame_duration_ms);
        // We use a fixed queue for our sliding window/ring buffer.
        FixedQueue<float[]> fixedQueue = new FixedQueue<float[]>(num_padding_frames);

        // We have two states: TRIGGERED and NOTTRIGGERED. We start in the NOTTRIGGERED state.
        boolean triggered = false;
        int num_voiced = 0;
        int num_unvoiced = 0;

        float test_threshold = 0.5f;
        NDArray h_ort = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);
        NDArray c_ort = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);

        for (float[] frame : frames) {
            boolean isSpeech = false;
            NDArray audioFeature = manager.create(frame).reshape(1, frame.length).toType(DataType.FLOAT32, true);
            NDArray sampling_rate = manager.create(new int[]{16000}).toType(DataType.INT64, true);
            NDList list = new NDList(audioFeature, sampling_rate, h_ort, c_ort);
            NDList result = vadModel.vad(list);

            NDArray output = result.get(0);
            float[] f1 = output.toFloatArray();
            if (f1[0] >= test_threshold) {
                isSpeech = true;
            }

            h_ort = result.get(1);
            c_ort = result.get(2);


            if (!triggered) {
                fixedQueue.offer(frame);
                if (isSpeech) {
                    num_voiced = num_voiced + 1;
                }
                // If we're NOTTRIGGERED and more than 90% of the frames in
                // the ring buffer are voiced frames, then enter the
                // TRIGGERED state.
                if (num_voiced > 0.9 * fixedQueue.getSize()) {
                    triggered = true;
                    for (float[] bytes : fixedQueue.getQueue()) {
                        voicedFrames.add(bytes);
                    }
                    fixedQueue.clear();
                    num_voiced = 0;
                }
            } else {
                // We're in the TRIGGERED state, so collect the audio data
                // and add it to the ring buffer.
                voicedFrames.add(frame);
                fixedQueue.offer(frame);
                if (!isSpeech) {
                    num_unvoiced = num_unvoiced + 1;
                }
                // If more than 90% of the frames in the ring buffer are
                // unvoiced, then enter NOTTRIGGERED and yield whatever
                // audio we've collected.
                if (num_unvoiced > 0.9 * fixedQueue.getSize()) {
                    triggered = false;
                    int len = 0;
                    for (float[] item : voicedFrames) {
                        len = len + item.length;
                    }
                    float[] voicedFramesBytes = new float[len];
                    int index = 0;
                    for (float[] item : voicedFrames) {
                        for (float value : item) {
                            voicedFramesBytes[index++] = value;
                        }
                    }

                    segments.add(voicedFramesBytes);
                    fixedQueue.clear();
                    voicedFrames.clear();
                    num_unvoiced = 0;
                }
            }
        }
        // If we have any leftover voiced audio when we run out of input, yield it.
        if (voicedFrames.size() > 0) {
            int len = 0;
            for (float[] item : voicedFrames) {
                len = len + item.length;
            }
            float[] voicedFramesBytes = new float[len];
            int index = 0;
            for (float[] item : voicedFrames) {
                for (float value : item) {
                    voicedFramesBytes[index++] = value;
                }
            }
            segments.add(voicedFramesBytes);
        }

        return segments;
    }

}
