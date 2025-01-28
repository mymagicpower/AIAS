package me.aias.example;

import me.aias.Jieba;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
/**
 * 中文切词 jieba
 * Chinese Tokenization
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class JiebaExample {

    private static final Logger logger = LoggerFactory.getLogger(JiebaExample.class);

    private JiebaExample() {
    }

    public static void main(String[] args) {

        String input = "今天是个好日子";


        logger.info("input Sentence: {}", input);
        Jieba parser = new Jieba();
        String[] result = parser.cut(input);

        logger.info("Words : " + Arrays.toString(result));

    }
}
