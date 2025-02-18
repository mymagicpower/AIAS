package top.aias.platform.controller;

import ai.djl.ModelException;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.translate.TranslateException;
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
import top.aias.platform.utils.AudioUtils;
import top.aias.platform.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

            String text = asrService.speechToText(audio, false);

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

            String text = asrService.speechToText(audio, true);
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

            String text = asrService.speechToText(audio, false);

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

            String text = asrService.speechToText(audio, true);
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
        Path tempAudioFilePath = null;
        Path tempConvertedAudioFilePath = null;
        try {
            // 解析文件扩展名
            String fileExtension = FileUtils.getFileExtension(url);

            // 下载音频文件
            String tempFileName = UUID.randomUUID() + "." + fileExtension;
            tempAudioFilePath = Files.createTempFile("audio_", tempFileName);

            try (InputStream inputStream = new URL(url).openStream()) {
                Files.copy(inputStream, tempAudioFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("File saved at: " + tempAudioFilePath);

            if (!"wav".equalsIgnoreCase(fileExtension)) {
                tempFileName = UUID.randomUUID() + ".wav";
                tempConvertedAudioFilePath = Files.createTempFile("audio_", tempFileName);
                AudioUtils.convert(tempAudioFilePath.toString(), tempConvertedAudioFilePath.toString());
            } else {
                tempConvertedAudioFilePath = Paths.get(tempAudioFilePath.toString());
            }

            // 进行音频转录操作
            String texts = asrService.longSpeechToText(tempConvertedAudioFilePath, false);

            return ResultBean.success().add("result", texts);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResultBean.failure().add("message", "下载或处理音频文件时发生错误: " + e.getMessage());
        } catch (TranslateException e) {
            logger.error(e.getMessage(), e);
            return ResultBean.failure().add("message", "语音识别失败: " + e.getMessage());
        } catch (ModelException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } finally {
            // 删除临时文件
            if (tempAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempAudioFilePath, e);
                }
            }
            // 删除临时文件
            if (tempConvertedAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempConvertedAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempConvertedAudioFilePath, e);
                }
            }
        }
    }

    @ApiOperation(value = "中文长语音识别-URL")
    @GetMapping(value = "/zhAsrForLongAudioUrl", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForLongAudioUrl(@RequestParam(value = "url") String url) {
        Path tempAudioFilePath = null;
        Path tempConvertedAudioFilePath = null;
        try {
            // 解析文件扩展名
            String fileExtension = FileUtils.getFileExtension(url);

            // 下载音频文件
            String tempFileName = UUID.randomUUID() + "." + fileExtension;
            tempAudioFilePath = Files.createTempFile("audio_", tempFileName);

            try (InputStream inputStream = new URL(url).openStream()) {
                Files.copy(inputStream, tempAudioFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("File saved at: " + tempAudioFilePath);

            if (!"wav".equalsIgnoreCase(fileExtension)) {
                tempFileName = UUID.randomUUID() + ".wav";
                tempConvertedAudioFilePath = Files.createTempFile("audio_", tempFileName);
                AudioUtils.convert(tempAudioFilePath.toString(), tempConvertedAudioFilePath.toString());
            } else {
                tempConvertedAudioFilePath = Paths.get(tempAudioFilePath.toString());
            }

            // 进行音频转录操作
            String texts = asrService.longSpeechToText(tempConvertedAudioFilePath, true);

            return ResultBean.success().add("result", texts);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResultBean.failure().add("message", "下载或处理音频文件时发生错误: " + e.getMessage());
        } catch (TranslateException e) {
            logger.error(e.getMessage(), e);
            return ResultBean.failure().add("message", "语音识别失败: " + e.getMessage());
        } catch (ModelException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } finally {
            // 删除临时文件
            if (tempAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempAudioFilePath, e);
                }
            }
            // 删除临时文件
            if (tempConvertedAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempConvertedAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempConvertedAudioFilePath, e);
                }
            }
        }
    }

    @ApiOperation(value = "英文长语音识别-音频文件")
    @PostMapping(value = "/enAsrForLongAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean enAsrForAudioLongFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        String fileExtension = FileUtils.getFileExtension(audioFile.getOriginalFilename());

//        if (!"wav".equalsIgnoreCase(fileExtension) && !"mp3".equalsIgnoreCase(fileExtension)) {
//            return ResultBean.failure().add("message", "仅支持 WAV, mp3 格式");
//        }

        Path tempAudioFilePath = null;
        Path tempConvertedAudioFilePath = null;
        try {
            String tempFileName = UUID.randomUUID() + fileExtension; // ".wav"
            tempAudioFilePath = Files.createTempFile("audio_", tempFileName);
            try (InputStream inputStream = audioFile.getInputStream()) {
                Files.copy(inputStream, tempAudioFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("File saved at: " + tempAudioFilePath);

            if (!"wav".equalsIgnoreCase(fileExtension)) {
                tempFileName = UUID.randomUUID() + ".wav";
                tempConvertedAudioFilePath = Files.createTempFile("audio_", tempFileName);
                AudioUtils.convert(tempAudioFilePath.toString(), tempConvertedAudioFilePath.toString());
            } else {
                tempConvertedAudioFilePath = Paths.get(tempAudioFilePath.toString());
            }

            // 这里可以进行音频处理或转录操作
            String texts = asrService.longSpeechToText(tempConvertedAudioFilePath, false);

            return ResultBean.success().add("result", texts);

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } catch (TranslateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } catch (ModelException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } finally {
            // 确保文件最终被删除
            if (tempAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempAudioFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 删除临时文件
            if (tempConvertedAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempConvertedAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempConvertedAudioFilePath, e);
                }
            }
        }
    }

    @ApiOperation(value = "中文长语音识别-音频文件")
    @PostMapping(value = "/zhAsrForLongAudioFile", produces = "application/json;charset=utf-8")
    public ResultBean zhAsrForLongAudioFile(@RequestParam(value = "audioFile") MultipartFile audioFile) {
        String fileExtension = FileUtils.getFileExtension(audioFile.getOriginalFilename());

//        if (!"wav".equalsIgnoreCase(fileExtension) && !"mp3".equalsIgnoreCase(fileExtension)) {
//            return ResultBean.failure().add("message", "仅支持 WAV, mp3 格式");
//        }


        Path tempAudioFilePath = null;
        Path tempConvertedAudioFilePath = null;
        try {
            String tempFileName = UUID.randomUUID() + fileExtension; // ".wav"
            tempAudioFilePath = Files.createTempFile("audio_", tempFileName);
            try (InputStream inputStream = audioFile.getInputStream()) {
                Files.copy(inputStream, tempAudioFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("File saved at: " + tempAudioFilePath);

            if (!"wav".equalsIgnoreCase(fileExtension)) {
                tempFileName = UUID.randomUUID() + ".wav";
                tempConvertedAudioFilePath = Files.createTempFile("audio_", tempFileName);
                AudioUtils.convert(tempAudioFilePath.toString(), tempConvertedAudioFilePath.toString());
            } else {
                tempConvertedAudioFilePath = Paths.get(tempAudioFilePath.toString());
            }

            // 这里可以进行音频处理或转录操作
            String texts = asrService.longSpeechToText(tempConvertedAudioFilePath, true);

            return ResultBean.success().add("result", texts);

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } catch (TranslateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } catch (ModelException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        } finally {
            // 确保文件最终被删除
            if (tempAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempAudioFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 删除临时文件
            if (tempConvertedAudioFilePath != null) {
                try {
                    Files.deleteIfExists(tempConvertedAudioFilePath);
                } catch (IOException e) {
                    logger.warn("无法删除临时文件: " + tempConvertedAudioFilePath, e);
                }
            }
        }
    }

}
