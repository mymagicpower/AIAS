package top.aias.vad;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ZooModel;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.vad.utils.SileroVAD;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 语音活动检测(Voice Activity Detection,VAD)
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class VADExample {
    private static final Logger logger = LoggerFactory.getLogger(VADExample.class);

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("src/test/resources/test.wav");
        Audio audio =
                AudioFactory.newInstance()
                        .setChannels(1)
                        .setSampleRate(16000)
                        .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                        .fromFile(path);

        // window_size_samples [512, 1024, 1536] for 16000 sampling_rate
        // window_size_samples = (frame_duration_ms / 10000) * sampling_rate
        // frame_duration_ms = window_size_samples / (sampling_rate / 1000)
        // frame_duration_ms: 512/16 = 32, 1024/16 = 64, 1536/16 = 96
        List<float[]> frames = generateFrames(audio.getData(), 32, 16000);

        SileroVAD vad = new SileroVAD();
        try (ZooModel<NDList, NDList> model = vad.criteria().loadModel();
             Predictor<NDList, NDList> predictor = model.newPredictor();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {

            float test_threshold = 0.5f;

            NDArray h_ort = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);
            NDArray c_ort = manager.zeros(new Shape(2, 1, 64), DataType.FLOAT32);
            for (float[] frame : frames) {
                NDArray audioFeature = manager.create(frame).reshape(1, frame.length).toType(DataType.FLOAT32, true);
                NDArray sampling_rate = manager.create(new int[]{16000}).toType(DataType.INT64, true);
                NDList list = new NDList(audioFeature, sampling_rate, h_ort, c_ort);

                NDList result = predictor.predict(list);

                NDArray output = result.get(0);
                float score = output.toFloatArray()[0];
                if (score >= test_threshold) {
                    logger.info("score: " + score);
                }

                h_ort = result.get(1);
                c_ort = result.get(2);
            }
        }
    }


    public static List<float[]> generateFrames(float[] data, int frameDurationMs, float sampleRate) {
        List<float[]> list = new ArrayList<>();
        int offset = 0;
        int n = (int) (sampleRate * (frameDurationMs / 1000.0));
        int length = data.length;
        while (offset + n < length) {
            float[] frame = Arrays.copyOfRange(data, offset, offset + n);
            offset += n;
            list.add(frame);
        }
        return list;
    }

}
