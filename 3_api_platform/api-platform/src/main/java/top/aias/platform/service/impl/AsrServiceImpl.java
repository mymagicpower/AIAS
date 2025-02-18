package top.aias.platform.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.translate.TranslateException;
import com.hankcs.hanlp.HanLP;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.platform.model.asr.WhisperModel;
import top.aias.platform.model.vad.SileroSpeechSegment;
import top.aias.platform.model.vad.SileroVadDetector;
import top.aias.platform.model.vad.SileroVadModel;
import top.aias.platform.service.AsrService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 语音识别服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class AsrServiceImpl implements AsrService {
    private Logger logger = LoggerFactory.getLogger(AsrServiceImpl.class);
    private static final int SAMPLE_RATE = 16000;
    private static final float THRESHOLD = 0.5f;
    private static final int MIN_SPEECH_DURATION_MS = 250;
    private static final float MAX_SPEECH_DURATION_SECONDS = Float.POSITIVE_INFINITY;
    private static final int MIN_SILENCE_DURATION_MS = 100;
    private static final int SPEECH_PAD_MS = 30;

    @Autowired
    private WhisperModel whisperModel;

    @Autowired
    private SileroVadModel sileroVadModel;


    public String speechToText(Audio audio, Boolean isChinese) throws TranslateException, ModelException, IOException {
        String output = whisperModel.asr(audio, isChinese);
        return output;
    }

    public String longSpeechToText(Path tempAudioFilePath, Boolean isChinese) throws TranslateException, IOException, ModelException {

        Audio audio =
                AudioFactory.newInstance()
                        .setChannels(1)
                        .setSampleRate(16000)
                        .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                        .fromFile(tempAudioFilePath);

        // 获取 AudioInputStream
        File wavFile = tempAudioFilePath.toFile();
        // 计算音频数据的大小
        long audioFileLength = wavFile.length() / 2;
        float[] data = audio.getData();
        // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
        float ratio = (float) data.length / audioFileLength;

        SileroVadDetector sileroVadDetector = new SileroVadDetector(sileroVadModel, THRESHOLD, SAMPLE_RATE,
                MIN_SPEECH_DURATION_MS, MAX_SPEECH_DURATION_SECONDS, MIN_SILENCE_DURATION_MS, SPEECH_PAD_MS);

        List<SileroSpeechSegment> speechTimeList = sileroVadDetector.getSpeechSegmentList(wavFile);

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
            String result = this.speechToText(new Audio(segment), isChinese);
            if(isChinese){
                // 确保是简体中文
                result = HanLP.convertToSimplifiedChinese(result);
            }
            logger.info("第{}个分割音频, 识别结果: {}", count, result);

            texts = texts + " " + result;

            count++;
        }

        logger.info("最终识别结果:" + texts);

        return texts;
    }

    /**
     * 根据最大时间窗分组（whisper 最大支持 30 秒语音，所以建议设置 30 ）
     *
     * @param speechTimeList
     * @param maxSum
     * @return
     */
    private List<List<SileroSpeechSegment>> groupSpeechTimes(List<SileroSpeechSegment> speechTimeList, float maxSum) {
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
