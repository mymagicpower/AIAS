package top.aias.asr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.asr.vad.SileroVADModel;
import top.aias.asr.vad.SileroVadUtils;
import top.aias.asr.whisper.WhisperModel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;

public final class SpeechToTextEn {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextEn.class);

    private SpeechToTextEn() {}

    public static void main(String[] args)
            throws Exception {

        try (WhisperModel whisperModel = new WhisperModel("models/whisper/traced_whisper_base.pt");
             SileroVADModel vadModel = new SileroVADModel();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {

            Path audioPath = Paths.get("src/test/resources/jfk.flac");

            Audio audio =
                    AudioFactory.newInstance()
                            .setChannels(1)
                            .setSampleRate(16000)
                            .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                            .fromFile(audioPath);

            // 每 30 秒切割一段（可以少于30秒）
            List<float[]> segments = SileroVadUtils.generateSegments(manager, vadModel, audio.getData(), 64, 1);

            // 此类模型图优化占时过长，关闭
            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            int count = 1;
            String texts = "";
            for (float[] segment : segments) {
                String result = whisperModel.asr(new Audio(segment), false);
                texts = texts + " " + result;
                logger.info("第{}个分割音频, 识别结果: {}", count++, result);
            }

            logger.info("最终识别结果:" + texts);

        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}
