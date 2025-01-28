package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.SentimentAnalysis;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.Arrays;

/**
 * Flink - Sentiment Analysis
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SentimentAnalysisExample {

    public static void main(String[] args) throws Exception {

        // the host and the port to connect to
        final String hostname = "127.0.0.1";
        final int port = 9000;

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream(hostname, port, "\n");

        // Run inference with Flink streaming
        DataStream<String> embedding = text.flatMap(new SEFlatMap());

        // print the results with a single thread, rather than in parallel
        embedding.print().setParallelism(1);
        env.execute("SentimentAnalysis");
    }

    public static class SEFlatMap implements FlatMapFunction<String, String> {

        private static Predictor<String, Classifications> predictor;

        private Predictor<String, Classifications> getOrCreatePredictor()
                throws ModelException, IOException {
            if (predictor == null) {
                Criteria<String, Classifications> criteria = new SentimentAnalysis().criteria();
                ZooModel<String, Classifications> model = criteria.loadModel();
                predictor = model.newPredictor();
            }
            return predictor;
        }

        @Override
        public void flatMap(String value, Collector<String> out) throws Exception {
            Predictor<String, Classifications> predictor = getOrCreatePredictor();
            out.collect(predictor.predict(value).toString());
        }
    }
}
