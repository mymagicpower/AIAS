package me.aias.example;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import me.aias.example.utils.ImageUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.VoidSerializer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MyKafkaProducer {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "0");
        props.put("retries", "1");
        props.put("batch.size", "20971520");
        props.put("linger.ms", "33");
        props.put("max.request.size", "10485760"); //10M
        props.put("compression.type", "gzip");
        props.put("kafka.topic", "face-data");
        props.put("key.serializer", VoidSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        props.put("buffer.memory", 2147483647);
        props.put("timeout.ms", 3000000);
        props.put("request.timeout.ms", 30000000);

        KafkaProducer<Void, String> producer = new KafkaProducer<>(props);

        Path facePath = Paths.get("src/test/resources/faces.jpg");
        Image img = ImageFactory.getInstance().fromFile(facePath);
        String base64 = ImageUtils.bufferedImageToBase64((BufferedImage) img.getWrappedImage());
        int l = base64.length();
        producer.send(new ProducerRecord("face-data", base64));
        producer.close();
    }
}