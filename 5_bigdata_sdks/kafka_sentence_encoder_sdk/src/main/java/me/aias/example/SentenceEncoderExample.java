package me.aias.example;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.SentenceEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SentenceEncoderExample {

    private static final String TOPIC = "sentence-data";

    public static void main(String[] args)
            throws MalformedModelException, ModelNotFoundException, IOException {
        SentenceEncoder sentenceEncoder = new SentenceEncoder();
        ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
        Predictor<String, float[]> predictor = model.newPredictor();

        int numConsumers = 1;
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
