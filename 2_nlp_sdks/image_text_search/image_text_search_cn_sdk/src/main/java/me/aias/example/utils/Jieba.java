package me.aias.example.utils;

import me.aias.jieba.SegToken;
import me.aias.jieba.JiebaSegmenter;

import java.util.List;

public final class Jieba {
    private JiebaSegmenter segmenter;

    public Jieba() {
        segmenter = new JiebaSegmenter();
    }

    public String[] cut(String sentence) {
        List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
        String[] words = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            words[i] = tokens.get(i).word;
        }
        return words;
    }
}
