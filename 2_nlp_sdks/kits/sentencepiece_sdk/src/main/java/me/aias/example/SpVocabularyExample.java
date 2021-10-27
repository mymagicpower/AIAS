package me.aias.example;

import me.aias.example.utils.SpTokenizer;
import me.aias.example.utils.SpVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpVocabularyExample {
    private static final Logger logger = LoggerFactory.getLogger(SpVocabularyExample.class);

    private SpVocabularyExample() {
    }

    public static void main(String[] args) throws IOException {
        Path modelPath = Paths.get("build/test/models/sententpiece_test_model.model");

        logger.info("Test TokenIdConversion");
        testTokenIdConversion(modelPath);

    }

    public static void testTokenIdConversion(Path modelPath) throws IOException {
        try (SpTokenizer tokenizer = new SpTokenizer(modelPath)) {
            SpVocabulary vocabulary = SpVocabulary.from(tokenizer);
            //根据id获取词表中的词
            logger.info(vocabulary.getToken(1));
            //获取词表中的词对应的id
            logger.info("" + vocabulary.getIndex("<s>"));
            for(int i=0;i<100;i++)
                logger.info(vocabulary.getToken(i));
        }
    }
}      