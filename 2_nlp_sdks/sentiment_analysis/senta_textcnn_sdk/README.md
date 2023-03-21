
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/lac.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/senta_textcnn.zip

### Text-Dialogue Emotion Recognition SDK [Chinese]

Dialogue Emotion Recognition (Emotion Detection) focuses on recognizing the emotions of users in intelligent dialogue scenes.
For user text in intelligent dialogue scenarios, the SDK automatically determines the emotional category of the text and gives the corresponding confidence.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK Algorithm:

This model is based on TextCNN (multi-convolutional neural network model), which can better capture the local correlation of sentences.
Model size: about 120M
Emotion types are classified as:

- Negative (negative)
- Neutral (neutral)
- Positive (positive)

#### Running example - SentaBilstmExample
After successful operation, the command line should see the following information:
```text
...
[INFO ] - input Sentence: 今天天气真好
#Chinese word segmentation
[INFO ] - Words : [今天, 天气, 真好]

#Part of speech tagging
[INFO ] - Tags : [TIME, n, a]

#Sentiment analysis probability
[INFO ] - [0.0018637113, 0.07143357, 0.9267028]

[INFO ] - negative : 0.0018637113
[INFO ] - neutral : 0.07143357
[INFO ] - positive : 0.9267028

```

### Open source algorithm

### 1. Open source algorithm used by the SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)

