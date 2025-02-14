package top.aias.platform.controller;

import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import com.hankcs.hanlp.HanLP;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.platform.bean.ResultBean;
import top.aias.platform.model.asr.vad.SileroSpeechSegment;
import top.aias.platform.model.asr.vad.SileroVadDetector;
import top.aias.platform.model.color.DdcolorModel;
import top.aias.platform.service.AsrService;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 语音识别
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "语音识别 -ASR")
@RestController
@RequestMapping("/api/asr")
public class AsrController {
    private Logger logger = LoggerFactory.getLogger(AsrController.class);

    @Autowired
    private AsrService asrService;

    @Autowired
    private SileroVadDetector sileroVadDetector;

    @ApiOperation(value = "英文短语音识别-URL")
    @GetMapping(value = "/enAsrForAudioUrl", produces = "application/json;charset=utf-8")
    public ResultBean enAsrForAudioUrl(@RequestParam(value = "url") String url) {
        try {
            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromUrl(url);

            String text = asrService.enSpeechToText(audio);

            return ResultBean.success().add("result", text);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "中文短语音识别-URL")
    @GetMapping(value = "/zhAsrForAudioUrl", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForAudioUrl(@RequestParam(value = "url") String url) {
        try {
            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromUrl(url);

            String text = asrService.zhSpeechToText(audio);
            // 确保是简体中文
            text = HanLP.convertToSimplifiedChinese(text);
            return ResultBean.success().add("result", text);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "英文短语音识别-音频文件")
    @PostMapping(value = "/enAsrForAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean enAsrForAudioFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        try (InputStream inputStream = audioFile.getInputStream()) {
            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromInputStream(inputStream);

            String text = asrService.enSpeechToText(audio);

            return ResultBean.success().add("result", text);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "中文短语音识别-音频文件")
    @PostMapping(value = "/zhAsrForAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForAudioFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        try (InputStream inputStream = audioFile.getInputStream()) {
            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromInputStream(inputStream);

            String text = asrService.zhSpeechToText(audio);
            // 确保是简体中文
            text = HanLP.convertToSimplifiedChinese(text);

            return ResultBean.success().add("result", text);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "英文长语音识别-URL")
    @GetMapping(value = "/enAsrForLongAudioUrl", produces = "application/json;charset=utf-8")
    public ResultBean enAsrForLongAudioUrl(@RequestParam(value = "url") String url) {
        try {
            Audio audioNoSampleRate =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .fromUrl(url);
            // 计算音频数据的大小
            long audioFileLength = audioNoSampleRate.getData().length;

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromUrl(url);

            float[] data = audio.getData();
            // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
            float ratio = (float) data.length / audioFileLength;

            List<SileroSpeechSegment> speechTimeList = sileroVadDetector.getSpeechSegmentList(new URL(url));

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

            String texts = "";
            for (float[] segment : segments) {
                String result = asrService.enSpeechToText(new Audio(segment));
                texts = texts + " " + result;
            }

            return ResultBean.success().add("result", texts);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "中文长语音识别-URL")
    @GetMapping(value = "/zhAsrForLongAudioUrl", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForLongAudioUrl(@RequestParam(value = "url") String url) {
        try {
            Audio audioNoSampleRate =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .fromUrl(url);
            // 计算音频数据的大小
            long audioFileLength = audioNoSampleRate.getData().length;

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromUrl(url);

            float[] data = audio.getData();
            // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
            float ratio = (float) data.length / audioFileLength;

            List<SileroSpeechSegment> speechTimeList = sileroVadDetector.getSpeechSegmentList(new URL(url));

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

            String texts = "";
            for (float[] segment : segments) {
                String result = asrService.zhSpeechToText(new Audio(segment));
                // 确保是简体中文
                result = HanLP.convertToSimplifiedChinese(result);
                texts = texts + " " + result;
            }

            return ResultBean.success().add("result", texts);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "英文长语音识别-音频文件")
    @PostMapping(value = "/enAsrForLongAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean enAsrForAudioLongFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        try (InputStream inputStream = audioFile.getInputStream()) {
            Audio audioNoSampleRate =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .fromInputStream(inputStream);
            // 计算音频数据的大小
            long audioFileLength = audioNoSampleRate.getData().length;

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromInputStream(inputStream);


            float[] data = audio.getData();
            // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
            float ratio = (float) data.length / audioFileLength;

            List<SileroSpeechSegment> speechTimeList = sileroVadDetector.getSpeechSegmentList(inputStream);

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

            String texts = "";
            for (float[] segment : segments) {
                String result = asrService.enSpeechToText(new Audio(segment));
                texts = texts + " " + result;
            }

            return ResultBean.success().add("result", texts);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "中文长语音识别-音频文件")
    @PostMapping(value = "/zhAsrForLongAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForLongAudioFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        try (InputStream inputStream = audioFile.getInputStream()) {
            Audio audioNoSampleRate =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .fromInputStream(inputStream);
            // 计算音频数据的大小
            long audioFileLength = audioNoSampleRate.getData().length;

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromInputStream(inputStream);

            float[] data = audio.getData();
            // data 是设置采样率后读取的数据，wavFile.length 是原始数据长度。
            float ratio = (float) data.length / audioFileLength;

            List<SileroSpeechSegment> speechTimeList = sileroVadDetector.getSpeechSegmentList(inputStream);

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

            String texts = "";
            for (float[] segment : segments) {
                String result = asrService.zhSpeechToText(new Audio(segment));
                // 确保是简体中文
                result = HanLP.convertToSimplifiedChinese(result);
                texts = texts + " " + result;
            }

            return ResultBean.success().add("result", texts);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
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
