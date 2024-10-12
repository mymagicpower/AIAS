package top.aias.whisper;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import com.hankcs.hanlp.HanLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.whisper.utils.DecoderOutput;
import top.aias.whisper.utils.WhisperModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class SpeechToTextZh {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextZh.class);

    private SpeechToTextZh() {
    }

    public static void main(String[] args)
            throws ModelException, IOException, TranslateException, InterruptedException {
        try (WhisperModel whisperModel = new WhisperModel("models/whisper/traced_whisper_small.pt")) {
            // 此类模型图优化占时过长，关闭
            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            Path audioPath = Paths.get("src/test/resources/test.wav");
            String output = whisperModel.asr(audioPath, true);

            // 确保是简体中文
            output = HanLP.convertToSimplifiedChinese(output);

            System.out.println(output);

        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}
