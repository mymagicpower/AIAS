
### Download the model and put it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/translation_zh_en.zip

### Chinese to English Translation SDK

Machine translation, also known as automatic translation, is the process of using a computer to convert one natural language (source language) to another natural language (target language).
It is a branch of computational linguistics and one of the ultimate goals of artificial intelligence, with significant scientific research value.
At the same time, machine translation also has important practical value. With the rapid development of economic globalization and the Internet, machine translation technology plays an increasingly important role in promoting political, economic, and cultural exchanges.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg)

### SDK algorithm:

In 2017, the Google machine translation team proposed a brand new network structure called Transformer in its published paper Attention Is All You Need, which is used to complete sequence-to-sequence (Seq2Seq) learning tasks such as machine translation (Machine Translation). Transformer network uses attention mechanism to model sequence-to-sequence completely, and has achieved good results.
This SDK model includes a 6-layer transformer structure, with 8 heads, 512 hidden layer parameters, and 64M parameters.
It provides the ability to translate Chinese to English.

### Running example- TranslationExample

After the operation is successful, the command line should see the following information:
```text
...
[INFO ] - 输入句子: 今天天气怎么样？
#Chinese word segmentation
[INFO ] - Words : [今天, 天气, 怎么样, ？]

#Part of speech tagging
[INFO ] - Tags : [TIME, n, r, w]

#Translation results, sorted by confidence:
[INFO ] - T0:  What's the weather like today?
[INFO ] - T1:  How is the weather today?
[INFO ] - T2:  How's the weather today?
[INFO ] - T3:  How was the weather today?
[INFO ] - T4:  What is the weather like today?

```

### Open source algorithm

### 1. Open source algorithm used by SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
