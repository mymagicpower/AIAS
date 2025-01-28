package me.aias;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String sentence = "今天是个好日子";
        Jieba parser = new Jieba();
        for (int i = 0; i < 100; i++) {
            String[] words = parser.cut(sentence);
            System.out.println(Arrays.toString(words));
        }


    }
}
