package me.aias.example;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;

import java.time.Duration;
import java.util.*;

public class ConsumerLoop implements Runnable {
    private final KafkaConsumer<Void, String> consumer;
    private final List<String> topics;
    private final int id;
    Predictor<String, float[]> predictor;
    
    public ConsumerLoop(int id, List<String> topics, Predictor<String, float[]> predictor) {
        this.id = id;
        this.topics = topics;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-consumer-group");
        props.put("key.deserializer", VoidDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
        this.predictor = predictor;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<Void, String> records =
                        consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
                for (ConsumerRecord<Void, String> record : records) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("partition", record.partition());
                    data.put("offset", record.offset());
                    data.put("value", record.value());
                    float[] embedding = predictor.predict(record.value());
                    System.out.println("content: " + data.get("value"));
                    System.out.println("Vector dimensions: " + embedding.length);
                    System.out.println("Sentence embeddings: " + Arrays.toString(embedding));
                }
            }
        } catch (WakeupException | TranslateException e) {
            e.printStackTrace();
            // ignore for shutdown
        } finally {
            consumer.close();
            predictor.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
