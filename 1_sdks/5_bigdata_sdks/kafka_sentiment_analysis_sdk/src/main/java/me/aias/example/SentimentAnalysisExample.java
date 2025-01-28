package me.aias.example;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SentimentAnalysisExample {

    private static final String TOPIC = "twitter-data";

    public static void main(String[] args)
            throws MalformedModelException, ModelNotFoundException, IOException {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
        Criteria<String, Classifications> criteria = sentimentAnalysis.criteria();
        // Load model
        ZooModel<String, Classifications> model = criteria.loadModel();
        // Create predictor
        Predictor<String, Classifications> predictor = model.newPredictor();

        int numConsumers = 3;
        List<String> topics = Collections.singletonList(TOPIC);
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        // setup consumer
        final List<ConsumerLoop> consumers = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
            ConsumerLoop consumer = new ConsumerLoop(i, topics, predictor);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    for (ConsumerLoop consumer : consumers) {
                                        consumer.shutdown();
                                    }
                                    executor.shutdown();
                                    try {
                                        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }));
    }
}
