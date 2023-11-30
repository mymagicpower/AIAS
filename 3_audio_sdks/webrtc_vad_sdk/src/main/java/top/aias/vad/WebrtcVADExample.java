package top.aias.vad;

import org.jitsi.webrtcvadwrapper.WebRTCVad;
import org.jitsi.webrtcvadwrapper.audio.ByteSignedPcmAudioSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.vad.utils.SoundUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 语音活动检测(Voice Activity Detection,VAD)
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class WebrtcVADExample {
    private static final Logger logger = LoggerFactory.getLogger(WebrtcVADExample.class);

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("src/test/resources/test.wav");

        List<byte[]> frames = generateFrames(path, 30);

        WebRTCVad vad = new WebRTCVad(16000, 3);

        for (byte[] frame : frames) {
            ByteSignedPcmAudioSegment segment = new ByteSignedPcmAudioSegment(frame);
            int[] converted = segment.to16bitPCM();
            boolean isSpeech = vad.isSpeech(Arrays.copyOf(converted, 480)); // 160, 320 or 480 integer values

            logger.info("isSpeech: " + isSpeech);
        }

    }

    public static List<byte[]> generateFrames(Path path, int frame_duration_ms) throws Exception {
        float sampleRate = SoundUtils.getSampleRate(path.toFile());
        byte[] bytes = SoundUtils.convertAsByteArray(path.toFile(), SoundUtils.WAV_PCM_SIGNED);
        List<byte[]> frames = SoundUtils.frameGenerator(bytes, frame_duration_ms, sampleRate);
        return frames;
    }


}
