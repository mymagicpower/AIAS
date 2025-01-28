## 音素相关的文本处理 SDK
音素相关的文本处理工具箱。适用于中文、英文和中英混合的音素，其中汉字拼音采用清华大学的音素，
英文字符分字母和英文。

### SDK包含的工具：
#### SequenceUtils:
- 文本转为拼音
- 拼音转为音素
- 文本转为音素
- 文本转为ID列表

#### NumberUtils:
数字读法 - 按数值大小读，一个一个数字读。

#### ConvertUtils
文本转换 - 全角半角转换，简体繁体转换。


## 运行例子 - SequenceExample
运行成功后，命令行应该看到下面的信息:
```text
...
# SequenceUtils.text2pinyin("文本转为拼音。")
wen2 ben3 zhuan3 wei4 pin1 yin1 。

# SequenceUtils.pinyin2phoneme(SequenceUtils.text2pinyin("拼音转为音素。")
[p, in, 1, -, ii, in, 1, -, zh, uan, 3, -, uu, ui, 4, -, ii, in, 1, -, s, u, 4, -, ., -, ~, _]

# SequenceUtils.text2phoneme("文本转为音素。")
[uu, un, 2, -, b, en, 2, -, zh, uan, 3, -, uu, ui, 4, -, ii, in, 1, -, s, u, 4, -, ., -, ~, _]

# SequenceUtils.text2sequence("文本转为ID列表。")
[25, 63, 72, 2, 4, 37, 72, 2, 29, 59, 73, 2, 25, 62, 74, 2, 2, 15, 45, 74, 2, 4, 44, 73, 2, 130, 2, 1, 0]
```

## 运行例子 - NumberExample
运行成功后，命令行应该看到下面的信息:
```text
...
# NumberUtils.sayDigit("1234567890123456")
一二三四五六七八九零一二三四五六

# NumberUtils.sayNumber("123456")
十二万三千四百五十六

# NumberUtils.sayDecimal("3.14")
三点一四
```

## 运行例子 - ConvertExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 半角转全角 ConvertUtils.ban2quan("aA1 ,:$。、") 
ａＡ１　，：＄。、

# 全角转半角 ConvertUtils.quan2ban("ａＡ１　，：＄。、")
aA1 ,:$。、

# 简体转繁体 ConvertUtils.jian2fan("中国语言")
中國語言

# 繁体转简体 ConvertUtils.fan2jian("中國語言")
中国语言

```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
