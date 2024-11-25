package top.aias.chatglm.tokenizer;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private Test() {
    }

    public static void main(String[] args) throws ModelException, IOException,
            TranslateException {

        Path tokenizerPath = Paths.get("models/tokenizer.model");

        try (SpTokenizer tokenizer = new SpTokenizer(tokenizerPath)) {
            SpProcessor processor = tokenizer.getProcessor();
            int[] intArray = new int[]{36474, 54591, 31404, 33030, 34797, 42481, 22011, 10461, 30944, 30943, 30941, 30978, 30949, 31123, 48895, 35214, 54622, 31123, 32616, 39905, 31901, 31639, 31155, 2};
            String result = processor.decode(intArray);
            logger.info("{}", result);
        }
    }
}