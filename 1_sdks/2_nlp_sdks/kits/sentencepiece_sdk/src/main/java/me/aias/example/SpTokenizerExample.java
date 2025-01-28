package me.aias.example;

import me.aias.example.utils.SpProcessor;
import me.aias.example.utils.SpTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SpTokenizerExample {
    private static final Logger logger = LoggerFactory.getLogger(SpTokenizerExample.class);

    private SpTokenizerExample() {
    }

    public static void main(String[] args) throws IOException {
        Path modelPath = Paths.get("build/test/models/sentencepiece.bpe.model");
        
        logger.info("Test Tokenize");
        testTokenize(modelPath);

        logger.info("Test Encode & Decode");
        testEncodeDecode(modelPath);

        logger.info("Test GetToken");
        testGetToken(modelPath);

        logger.info("Test GetId");
        testGetId(modelPath);

    }

    public static void testTokenize(Path modelPath) throws IOException {
        try (SpTokenizer tokenizer = new SpTokenizer(modelPath)) {
            String original = "This framework generates embeddings for input sentence";
            logger.info("Input sentence: " + original);
            List<String> tokens = tokenizer.tokenize(original);
            String[] strs = tokens.toArray(new String[]{});
            logger.info("Tokens: " + Arrays.toString(strs));
            String recovered = tokenizer.buildSentence(tokens);
            logger.info("Recovered sentence: " + recovered);
        }
    }

    public static void testEncodeDecode(Path modelPath) throws IOException {
        try (SpTokenizer tokenizer = new SpTokenizer(modelPath)) {
            String original = "This framework generates embeddings for input sentence";
            logger.info("Input sentence: " + original);
            SpProcessor processor = tokenizer.getProcessor();
            int[] ids = processor.encode(original);
            logger.info("Ids: " + Arrays.toString(ids));
            String recovered = processor.decode(ids);
            logger.info("Recovered sentence: " + recovered);
        }
    }

    public static void testGetToken(Path modelPath) throws IOException {
        try (SpTokenizer tokenizer = new SpTokenizer(modelPath)) {
            String original = "This framework generates embeddings for input sentence";
            SpProcessor processor = tokenizer.getProcessor();
            int[] ids = processor.encode(original);
            logger.info("ids: " + Arrays.toString(ids));
            for (int i = 0; i < ids.length; i++) {
                logger.info(processor.getToken(ids[i]));
            }
        }
    }

    public static void testGetId(Path modelPath) throws IOException {
        try (SpTokenizer tokenizer = new SpTokenizer(modelPath)) {
            String original = "This framework generates embeddings for input sentence";
            List<String> tokens = tokenizer.tokenize(original);
            String[] strs = tokens.toArray(new String[]{});
            logger.info("tokens: " + Arrays.toString(strs));
            SpProcessor processor = tokenizer.getProcessor();
            for (String token : tokens
            ) {
                logger.info("" + processor.getId(token));
            }
        }
    }
}
