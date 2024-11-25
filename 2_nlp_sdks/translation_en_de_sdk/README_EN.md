
### Download the model and put it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/translation_en_de.zip

### English to German Translation SDK
Machine translation, also known as automatic translation, is the process of using computers to convert one natural language (source language) into another natural language (target language). It is a branch of computational linguistics and one of the ultimate goals of artificial intelligence, with significant scientific research value. At the same time, machine translation also has important practical value. With the rapid development of economic globalization and the Internet, machine translation technology has played an increasingly important role in promoting political, economic, and cultural exchanges.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg)

### SDK algorithm:

In 2017, the Google Machine Translation team proposed a brand-new network structure called Transformer for completing sequence-to-sequence (Seq2Seq) learning tasks such as machine translation in their published paper "Attention Is All You Need". The Transformer network completely uses the attention mechanism to achieve sequence-to-sequence modeling and has achieved good results. This SDK model contains a 6-layer transformer structure and has been pre-trained on the WMT'14 EN-DE dataset. It can be directly used for prediction after import and provides the ability to translate English to German.

### Running Example - TranslationExample

After successful operation, the command line should see the following information:
```text
...
# The English sentence to be translated
[INFO] - input Sentence: What are you doing now?

# Translation results sorted by confidence:
[INFO] - T0:  Was tun Sie jetzt ?
[INFO] - T1:  Was machen Sie jetzt ?
[INFO] - T2:  Was tun Sie nun ?
[INFO] - T3:  Was machen Sie nun ?
[INFO] - T4:  Was unternehmen Sie jetzt ?

```

### Open-source algorithm

### 1. Open-source algorithm used by SDK:
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
