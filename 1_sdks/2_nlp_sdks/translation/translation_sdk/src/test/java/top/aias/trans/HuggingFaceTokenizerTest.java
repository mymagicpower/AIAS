package top.aias.trans;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HuggingFaceTokenizerTest {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceTokenizerTest.class);

    private HuggingFaceTokenizerTest() {
    }

    public static void main(String[] args) {

        String input = "DeepMind Company is";
        try (HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance("Helsinki-NLP/opus-mt-en-zh")) {

            Encoding encoding = tokenizer.encode(input);
            long[] inputIds = encoding.getIds();

            logger.info("{}", inputIds);
        }
    }
}