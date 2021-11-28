package me.aias.example;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.AudioProcess;
import me.aias.example.utils.SpeechRecognition;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 预测短语音
 *
 * <p>https://github.com/yeyupiaoling/PaddlePaddle-DeepSpeech
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SpeechRecognitionExample {
    private static final Logger logger = LoggerFactory.getLogger(SpeechRecognitionExample.class);

    private SpeechRecognitionExample() {
    }

    public static void main(String[] args) throws Exception {

        NDManager manager = NDManager.newBaseManager(Device.cpu());
        NDArray audioFeature = AudioProcess.processUtterance(manager, "src/test/resources/test.wav");
        // System.out.println(audioFeature.toDebugString(1000000000, 1000, 10, 1000));

        SpeechRecognition speakerEncoder = new SpeechRecognition();
        Criteria<NDArray, Pair> criteria = speakerEncoder.criteria();

        try (ZooModel<NDArray, Pair> model = criteria.loadModel();
             Predictor<NDArray, Pair> predictor = model.newPredictor()) {

            logger.info("input audio: {}", "src/test/resources/test.wav");

            Pair result = predictor.predict(audioFeature);
            logger.info("Score : " + result.getLeft());
            logger.info("Words : " + result.getRight());
        }
    }
}
