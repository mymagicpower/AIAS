package me.aias.example;


import me.aias.example.utils.SequenceUtils;

/**
 * 文本转为拼音
 * 拼音转为音素
 * 文本转为音素
 * 文本转为ID列表
 */
public class SequenceExample {
    public static void main(String[] args) {
        System.out.println(SequenceUtils.text2pinyin("文本转为拼音。"));
        System.out.println(SequenceUtils.pinyin2phoneme(SequenceUtils.text2pinyin("拼音转为音素。")));
        // 文本转为音素，用中文音素方案。
        // 中文转为拼音，按照清华大学方案转为音素，分为辅音、元音、音调。
        System.out.println(SequenceUtils.text2phoneme("文本转为音素。"));
        System.out.println(SequenceUtils.text2sequence("文本转为ID列表。"));
    }
}