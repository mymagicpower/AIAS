package top.aias.gpt2;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private Test() {
    }

    public static void main(String[] args) {

        System.out.println(Float.MIN_VALUE > 0);

    }
}