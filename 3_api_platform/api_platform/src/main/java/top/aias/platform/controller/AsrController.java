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
import top.aias.platform.service.AsrService;

import java.io.InputStream;

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

    @ApiOperation(value = "英文语音识别-URL")
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

    @ApiOperation(value = "中文语音识别-URL")
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

    @ApiOperation(value = "英文语音识别-音频文件")
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

    @ApiOperation(value = "中文语音识别-音频文件")
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
}
