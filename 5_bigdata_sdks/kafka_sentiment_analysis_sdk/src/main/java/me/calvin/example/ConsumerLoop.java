/*
 * Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package me.calvin.example;

import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;

public class ConsumerLoop implements Runnable {
    private final KafkaConsumer<Void, String> consumer;
    private final List<String> topics;
    private final int id;
    Predictor<String, Classifications> predictor;

    public ConsumerLoop(int id, List<String> topics, Predictor<String, Classifications> predictor) {
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
                    // make prediction on text data
                    Classifications result = predictor.predict(record.value());
                    data.put("prediction", result.toString());
                    System.out.println("content: " + data.get("value"));
                    System.out.println("prediction: " + data.get("prediction"));
                }
            }
        } catch (WakeupException | TranslateException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
