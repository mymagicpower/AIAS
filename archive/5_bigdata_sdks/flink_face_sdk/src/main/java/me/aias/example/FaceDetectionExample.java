package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.LightFaceDetection;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

/**
 * Flink & Kafka - Face Detection
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceDetectionExample {

    public static void main(String[] args) throws Exception {

        // the host and the port to connect to
        final String hostname = "127.0.0.1";
        final int port = 9000;

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-consumer-group");

        // get input data by connecting to the kafka
        FlinkKafkaConsumer<String> consumer =
                new FlinkKafkaConsumer<>("face-data", new SimpleStringSchema(), props);

        // Run inference with Flink streaming
        DataStream<String> detection = env
                .addSource(consumer)
                .flatMap(new SEFlatMap());

        // print the results with a single thread, rather than in parallel
        detection.print().setParallelism(1);
        env.execute("FaceDetection");
    }

    public static class SEFlatMap implements FlatMapFunction<String, String> {

        private static Predictor<Image, DetectedObjects> predictor;

        private Predictor<Image, DetectedObjects> getOrCreatePredictor()
                throws ModelException, IOException {
            if (predictor == null) {
                Criteria<Image, DetectedObjects> criteria = new LightFaceDetection().criteria();
                ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria);
                predictor = model.newPredictor();
            }
            return predictor;
        }

        @Override
        public void flatMap(String value, Collector<String> out) throws Exception {
            Predictor<Image, DetectedObjects> predictor = getOrCreatePredictor();
            if (!StringUtils.isNullOrWhitespaceOnly(value)) {
                BufferedImage bufferedImage = ImageUtils.base64ToBufferedImage(value);
                if (bufferedImage != null) {
                    Image image = ImageFactory.getInstance().fromImage(bufferedImage);
                    out.collect(predictor.predict(image).toString());
                }
            }
        }
    }
}
