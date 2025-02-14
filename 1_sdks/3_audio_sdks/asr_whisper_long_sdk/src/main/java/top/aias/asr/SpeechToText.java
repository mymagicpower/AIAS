package top.aias.asr;

import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import com.hankcs.hanlp.HanLP;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.asr.vad.SileroSpeechSegment;
import top.aias.asr.vad.SileroVadDetector;
import top.aias.asr.whisper.WhisperModel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SpeechToText {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToText.class);

    private static final int SAMPLE_RATE = 16000;
    private static final float THRESHOLD = 0.5f;
    private static final int MIN_SPEECH_DURATION_MS = 250;
    private static final float MAX_SPEECH_DURATION_SECONDS = Float.POSITIVE_INFINITY;
    private static final int MIN_SILENCE_DURATION_MS = 100;
    private static final int SPEECH_PAD_MS = 30;

    private SpeechToText() {
    }

    public static void main(String[] args) throws Exception {
        // 此类模型图优化占时过长，关闭
        System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

        String VAD_MODEL_PATH = "src/main/resources/silero_vad.onnx";
        String EXAMPLE_WAV_FILE = "src/test/resources/1.wav";
        String ASR_MODEL_PATH = "models/whisper/traced_whisper_base.pt";

        try (WhisperModel whisperModel = new WhisperModel(ASR_MODEL_PATH)) {

            Path audioPath = Paths.get(EXAMPLE_WAV_FILE);

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(SAMPLE_RATE)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromFile(audioPath);

            // 获取 AudioInputStream
            File wavFile = new File(EXAMPLE_WAV_FILE);
            // 计算音频数据的大小
            long audioFileLength = wavFile.length() / 2;
            float[] data = audio.getData();
            // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
            float ratio = (float) data.length / audioFileLength;

            SileroVadDetector vadDetector = new SileroVadDetector(VAD_MODEL_PATH, THRESHOLD, SAMPLE_RATE,
                    MIN_SPEECH_DURATION_MS, MAX_SPEECH_DURATION_SECONDS, MIN_SILENCE_DURATION_MS, SPEECH_PAD_MS);
            List<SileroSpeechSegment> speechTimeList = vadDetector.getSpeechSegmentList(wavFile);

            // 每 30 秒切割一段（可以少于30秒）
            List<List<SileroSpeechSegment>> groups = groupSpeechTimes(speechTimeList, 30);

            List<float[]> segments = new ArrayList<>();

            for (List<SileroSpeechSegment> list : groups) {
                // vadDetector offset 是原始数据的偏移量，所以需要通过 ratio 转换成采样后的偏移量。
                int start = (int) (ratio * list.get(0).getStartOffset());
                int end = (int) (ratio * list.get(list.size()-1).getEndOffset());
                float[] dataArr = Arrays.copyOfRange(data, start, end);
                segments.add(dataArr);
            }

            int count = 1;
            String texts = "";
            for (float[] segment : segments) {
                String result = whisperModel.asr(new Audio(segment), true);
                // 确保是简体中文
                result = HanLP.convertToSimplifiedChinese(result);

                logger.info("第{}个分割音频, 识别结果: {}", count, result);

                texts = texts + " " + result;

                count++;
            }

            logger.info("最终识别结果:" + texts);

        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }

    /**
     * 根据最大时间窗分组（whisper 最大支持 30 秒语音，所以建议设置 30 ）
     *
     * @param speechTimeList
     * @param maxSum
     * @return
     */
    public static List<List<SileroSpeechSegment>> groupSpeechTimes(List<SileroSpeechSegment> speechTimeList, float maxSum) {
        List<List<SileroSpeechSegment>> result = new ArrayList<>();
        List<SileroSpeechSegment> currentGroup = new ArrayList<>();
        float currentSum = 0;
        List<Float> timeGroup = new ArrayList<>();
        for (SileroSpeechSegment segment : speechTimeList) {
            float time = segment.getEndSecond() - segment.getStartSecond();
            if (currentSum + time > maxSum) {
                // 开始新的分组
                timeGroup.add(currentSum);
                result.add(new ArrayList<>(currentGroup));
                currentGroup.clear();
                currentSum = 0;
            }
            currentGroup.add(segment);
            currentSum += time;
        }

        // 添加最后一组
        if (!currentGroup.isEmpty()) {
            timeGroup.add(currentSum);
            result.add(currentGroup);
        }

        return result;
    }
}
