# Sentencepiece分词的Java实现
Sentencepiece是google开源的文本Tokenzier工具，其主要原理是利用统计算法，
在语料库中生成一个类似分词器的工具，外加可以将词token化的功能。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/wordpiece.jpeg)

## 运行例子 - SpTokenizerExample
运行成功后，命令行应该看到下面的信息:
```text

#测试token生成，并根据token还原句子
[INFO ] - Test Tokenize
[INFO ] - Input sentence: Hello World
[INFO ] - Tokens: [▁He, ll, o, ▁, W, or, l, d]
[INFO ] - Recovered sentence: Hello World

#测试Encode生成ids，并根据ids还原句子
[INFO ] - Test Encode & Decode
[INFO ] - Input sentence: Hello World
[INFO ] - Ids: [151, 88, 21, 4, 321, 54, 31, 17]
[INFO ] - Recovered sentence: Hello World

#测试GetToken，根据id获取token
[INFO ] - Test GetToken
[INFO ] - ids: [151, 88, 21, 4, 321, 54, 31, 17]
[INFO ] - ▁He
[INFO ] - ll
[INFO ] - o
[INFO ] - ▁
[INFO ] - W
[INFO ] - or
[INFO ] - l
[INFO ] - d

#测试GetId，根据token获取id
[INFO ] - Test GetId
[INFO ] - tokens: [▁He, ll, o, ▁, W, or, l, d]
[INFO ] - 151
[INFO ] - 88
[INFO ] - 21
[INFO ] - 4
[INFO ] - 321
[INFO ] - 54
[INFO ] - 31
[INFO ] - 17

```

### 如何训练模型？ 
参考：https://github.com/google/sentencepiece/blob/master/README.md
### 1. 安装编译sentencepiece：
```text
% git clone https://github.com/google/sentencepiece.git 
% cd sentencepiece
% mkdir build
% cd build
% cmake ..
% make -j $(nproc)
% sudo make install
% sudo ldconfig -v

```
### 2. 训练模型：
```text
% spm_train --input=<input> --model_prefix=<model_name> --vocab_size=8000 --character_coverage=1.0 --model_type=<type>
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   