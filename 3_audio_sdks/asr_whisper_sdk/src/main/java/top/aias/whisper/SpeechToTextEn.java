package top.aias.whisper;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.whisper.utils.DecoderOutput;
import top.aias.whisper.utils.WhisperModel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SpeechToTextEn {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextEn.class);

    private SpeechToTextEn() {
    }

    public static void main(String[] args)
            throws ModelException, IOException, TranslateException, InterruptedException {

        try (WhisperModel whisperModel = new WhisperModel("models/whisper/traced_whisper_base.pt")) {
            // 此类模型图优化占时过长，关闭
            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            Path audioPath = Paths.get("src/test/resources/jfk.flac");
            String output = whisperModel.asr(audioPath, false);
            System.out.println(output);

        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}
