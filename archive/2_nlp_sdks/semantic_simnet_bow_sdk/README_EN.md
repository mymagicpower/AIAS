
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/lac.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/simnet_bow.zip

### Text-Short Text Similarity SDK [Chinese]

Calculate the cosine similarity between two sentences:
Based on the user input of two texts, the similarity score can be calculated.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

### SDK algorithm:

The model is based on SimNet, which is a model for calculating sentence similarity.

### Running example - SemanticExample

After running successfully, the command line should display the following information:
```text
...
[INFO ] - 句子 1: 这个棋局太难了
# Chinese word segmentation
[INFO ] - Words : [这个, 棋局, 太难, 了]
# Part of speech tagging
[INFO ] - Tags : [r, n, a, xc]

[INFO ] - 句子 2: 这个棋局不简单
# Chinese word segmentation
[INFO ] - Words : [这个, 棋局, 不, 简单]
# Part of speech tagging
[INFO ] - Tags : [r, n, d, a]

[INFO ] - 句子 3: 这个棋局很有意思
# Chinese word segmentation
[INFO ] - Words : [这个, 棋局, 很, 有意思]
# Part of speech tagging
[INFO ] - Tags : [r, n, d, a]

# Calculate short text similarity
[INFO ] - 句子 1: 这个棋局太难了
[INFO ] - 句子 2: 这个棋局不简单
[INFO ] - 相似度 : 0.8542996

[INFO ] - 句子 1: 这个棋局太难了
[INFO ] - 句子 3: 这个棋局很有意思
[INFO ] - 相似度 : 0.8260221

```

### Open source algorithm

### 1. Open source algorithm used by SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
