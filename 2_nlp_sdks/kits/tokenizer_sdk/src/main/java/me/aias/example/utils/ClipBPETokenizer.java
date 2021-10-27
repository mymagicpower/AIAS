/*
 * Copyright 2021 Calvin, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License.
 *
 */
package me.aias.example.utils;

import ai.djl.modality.nlp.NlpUtils;
import ai.djl.modality.nlp.preprocess.SimpleTokenizer;
import ai.djl.modality.nlp.preprocess.TextCleaner;
import ai.djl.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CLIP Tokenizer
 * https://github.com/openai/CLIP/blob/main/clip/simple_tokenizer.py
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */

public class ClipBPETokenizer extends SimpleTokenizer {
    private List<Pair<Integer, Character>> byteEncoder;
    private List<Pair<Character, Integer>> byteDecoder = new ArrayList<>();
    private List<Pair<String, Integer>> encoder = new ArrayList<>();
    private List<Pair<Integer, String>> decoder = new ArrayList<>();
    private List<Pair<String[], Integer>> bpe_ranks = new ArrayList<>();
    private Map<String, String> cache = new ConcurrentHashMap<>();
    private String pat = null;

    public ClipBPETokenizer(String filePath) throws FileNotFoundException {
        this.byteEncoder = bytesToUnicode();
        for (Pair<Integer, Character> pair : byteEncoder) {
            byteDecoder.add(new Pair<>(pair.value, pair.key));
        }

        File file = new File(filePath);
        InputStream is = new FileInputStream(file);
        List<String> words = Utils.readLines(is, true);
        final List<String[]> tmpMerges = new ArrayList<>();
        words.stream()
                .filter(word -> (word != null && word != ""))
                .forEach(
                        word -> {
                            String[] ws = word.split(" ");
                            tmpMerges.add(ws);
                        });
        List<String[]> merges = tmpMerges.subList(1, 49152 - 256 - 2 + 1); // 48894
        List<String> vocab = new ArrayList<>();
        byteEncoder.stream()
                .forEach(
                        pair -> {
                            vocab.add("" + pair.value);
                        });
        List<String> vocabPartB = new ArrayList<>();
        vocab.stream()
                .forEach(
                        value -> {
                            vocabPartB.add(value + "</w>");
                        });
        vocab.addAll(vocabPartB);

        merges.stream()
                .forEach(
                        item -> {
                            vocab.add(StringUtils.join(item, ""));
                        });

        vocab.add("<|startoftext|>");
        vocab.add("<|endoftext|>");

        Stream.iterate(0, i -> i + 1)
                .limit(vocab.size())
                .forEach(
                        index -> {
                            encoder.add(new Pair<>(vocab.get(index), index));
                            decoder.add(new Pair<>(index, vocab.get(index)));
                        });

        Stream.iterate(0, i -> i + 1)
                .limit(merges.size())
                .forEach(
                        index -> {
                            bpe_ranks.add(new Pair<>(merges.get(index), index));
                        });

        cache.put("<|startoftext|>", "<|startoftext|>");
        cache.put("<|endoftext|>", "<|endoftext|>");

        pat =
                "<\\|startoftext\\|>|<\\|endoftext\\|>|'s|'t|'re|'ve|'m|'ll|'d|[\\p{L}]+|[\\p{N}]|[^\\s\\p{L}\\p{N}]+";


    }

    /**
     * Get unicode for char
     *
     * @param text
     * @return
     */
    public Integer ord(String text) {
        return text.codePointAt(0);
    }

    public List<Integer> range(Integer start, Integer stop, Integer step) {
        if (stop == null) {
            stop = start;
            start = 0;
        }

        if ((step > 0 && start >= stop) || (step < 0 && start <= stop)) {
            return new ArrayList<Integer>();
        }

        List<Integer> result = new ArrayList<Integer>();
        for (int i = start; step > 0 ? i < stop : i > stop; i += step) {
            result.add(i);
        }
        return result;
    }

    public List<Pair<Integer, Character>> bytesToUnicode() {
        List<Integer> bs = new ArrayList<Integer>();
        bs.addAll(range(ord("!"), ord("~") + 1, 1));
        bs.addAll(range(ord("¡"), ord("¬") + 1, 1));
        bs.addAll(range(ord("®"), ord("ÿ") + 1, 1));

        List<Integer> cs = bs.stream().collect(Collectors.toList());

        int n = 0;
        for (int i = 0; i < (int) Math.pow(2, 8); i++) {
            if (!bs.contains(i)) {
                bs.add(i);
                cs.add((int) Math.pow(2, 8) + n);
                n += 1;
            }
        }

        List<Pair<Integer, Character>> result = new ArrayList<>();
        for (int i = 0; i < bs.size(); i++) {
            result.add(new Pair<>(bs.get(i), (char) cs.get(i).intValue()));
        }
        return result;
    }

    public List<Pair<String, String>> getPairs(List<String> word) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        String prevChar = word.get(0);
        for (int i = 1; i < word.size(); i++) {
            pairs.add(new Pair<>(prevChar, word.get(i)));
            prevChar = word.get(i);
        }
        return pairs;
    }

    public String basicClean(String text) {
        return text.trim();
    }

    public String whitespaceClean(String text) {
        List<String> s = new ArrayList<>();
        s.add(text);
        TextCleaner cleaner = new TextCleaner(NlpUtils::isWhiteSpace, ' ');
        text = cleaner.preprocess(s).get(0);
        text = text.trim();
        return text;
    }

    public String bpe(String token) {
        if (cache.containsKey(token)) {
            return cache.get(token);
        }

        String[] tmpWord = token.split("");
        List<String> word = new ArrayList<>();
        for (String value : tmpWord) {
            word.add(value);
        }
        word.set(word.size() - 1, word.get(word.size() - 1) + "</w>");

        List<Pair<String, String>> pairs = getPairs(word);

        if (pairs.size() == 0) {
            return token + "</w>";
        }

        while (true) {

            Pair<String, String> bigram = null;
            int minRank = Integer.MAX_VALUE;

            for (Pair<String, String> pair : pairs) {
                for (Pair<String[], Integer> item : bpe_ranks) {
                    if (item.key[0].equals(pair.key) && item.key[1].equals(pair.value)) {
                        if (item.value < minRank) {
                            minRank = item.value;
                            bigram = pair;
                        }
                    }
                }
            }

            if (bigram == null) {
                break;
            }

            String first = bigram.key;
            String second = bigram.value;
            List<String> newWord = new ArrayList<>();

            int i = 0;
            while (i < word.size()) {
                int j = -1;
                for (int index = i; index < word.size(); index++) {
                    if (first.equals(word.get(index))) {
                        j = index;
                        break;
                    }
                }

                if (j == -1) {
                    newWord.addAll(word.subList(i, word.size()));
                    break;
                }

                newWord.addAll(word.subList(i, j));
                i = j;

                if (word.get(i).equals(first) && i < word.size() - 1 && word.get(i + 1).equals(second)) {
                    newWord.add(first + second);
                    i += 2;
                } else {
                    newWord.add(word.get(i));
                    i += 1;
                }
            }

            word = newWord;
            if (word.size() == 1) {
                break;
            } else {
                pairs = getPairs(word);
            }
        }
        String joinWord = String.join(" ", word);
        cache.put(token, joinWord);
        return joinWord;
    }

    public List<Integer> encode(String text) {
        List<Integer> bpeTokens = new ArrayList<>();
        text = whitespaceClean(basicClean(text)).toLowerCase();

        Pattern p = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String token = m.group();
            String[] chars = token.split("");
            StringBuffer tokenBuffer = new StringBuffer();
            for (String item : chars) {
                int code = item.codePointAt(0);
                for (Pair<Integer, Character> pair : byteEncoder) {
                    if (pair.key == code) {
                        tokenBuffer.append(pair.value);
                    }
                }
            }

            String[] bpeResult = this.bpe(tokenBuffer.toString()).split(" ");
            for (String item : bpeResult) {
                for (Pair<String, Integer> pair : encoder) {
                    if (pair.key.equals(item)) {
                        bpeTokens.add(pair.value);
                    }
                }
            }
        }
        return bpeTokens;
    }

    public String decode(List<Integer> tokens) {

        StringBuffer textBuffer = new StringBuffer();
        for (Integer item : tokens) {
            for (Pair<Integer, String> pair : decoder) {
                if (pair.key == item) {
                    textBuffer.append(pair.value);
                }
            }
        }
        String text = textBuffer.toString();

        String[] chars = text.split("");
        textBuffer = new StringBuffer();
        for (String item : chars) {
            Character character = item.charAt(0);
            for (Pair<Character, Integer> pair : byteDecoder) {
                if (pair.key == character) {
                    textBuffer.append((char) pair.value.intValue());
                }
            }
        }

        text = textBuffer.toString().replace("</w>", " ");
        return text;
    }
}
