## SDK for Phoneme-related Text Processing

A toolbox for phoneme-related text processing. It is suitable for Chinese, English, and mixed Chinese and English phonemes. The phonetic transcriptions of Chinese characters use the phonemes of Tsinghua University, and English characters are divided into letters and words.

### Tools included in the SDK:

### SequenceUtils:

- Text to Pinyin
- Pinyin to Phoneme
- Text to Phoneme
- Text to ID list

### NumberUtils:

Number Pronunciation - read by digit or by value.

### ConvertUtils

Text Conversion - Full-width and half-width conversion, Simplified and Traditional Chinese conversion.

## Running Example - SequenceExample

After running successfully, the command line should display the following information:
```text
...
# SequenceUtils.text2pinyin("文本转为拼音。")
wen2 ben3 zhuan3 wei4 pin1 yin1。

# SequenceUtils.pinyin2phoneme(SequenceUtils.text2pinyin("拼音转为音素。")
[p, in, 1, -, ii, in, 1, -, zh, uan, 3, -, uu, ui, 4, -, ii, in, 1, -, s, u, 4, -, ., -, ~, _]

# SequenceUtils.text2phoneme("文本转为音素。")
[uu, un, 2, -, b, en, 2, -, zh, uan, 3, -, uu, ui, 4, -, ii, in, 1, -, s, u, 4, -, ., -, ~, _]

# SequenceUtils.text2sequence("文本转为ID列表。")
[25, 63, 72, 2, 4, 37, 72, 2, 29, 59, 73, 2, 25, 62, 74, 2, 2, 15, 45, 74, 2, 4, 44, 73, 2, 130, 2, 1, 0]
```

## Running Example - NumberExample

After running successfully, the command line should display the following information:
```text
...
# NumberUtils.sayDigit("1234567890123456")
一二三四五六七八九零一二三四五六

# NumberUtils.sayNumber("123456")
十二万三千四百五十六

# NumberUtils.sayDecimal("3.14")
三点一四
```

## Running Example - ConvertExample
After running successfully, the command line should display the following information:
```text
...
# Half-width to Full-width ConvertUtils.ban2quan("aA1 ,:$。、")
ａＡ１　，：＄。、

# Full-width to Half-width ConvertUtils.quan2ban("ａＡ１　，：＄。、")
aA1 ,:$。、

# Simplified to Traditional ConvertUtils.jian2fan("中国语言")
中國語言

# Traditional to Simplified ConvertUtils.fan2jian("中國語言")
中国语言


```
