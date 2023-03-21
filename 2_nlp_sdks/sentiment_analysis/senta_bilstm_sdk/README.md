
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/lac.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/senta_bilstm.zip
- Link 3: https://github.com/mymagicpower/AIAS/releases/download/apps/senta_lstm.zip

### Text-Sentiment Analysis SDK [Chinese]

Sentiment Orientation Analysis (Sentiment Classification, referred to as Senta) is aimed at Chinese text with subjective descriptions.
It can automatically judge the sentiment polarity category of the text and give the corresponding confidence, which can help enterprises understand user consumption habits,
Analyze hot topics and crisis public opinion monitoring, and provide favorable decision-making support for enterprises.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK algorithm:

The SDK contains two models:
-SentaLstm - This model (about 650M) is based on an LSTM structure, and the sentiment types are:
-Negative (negative)
-Positive (positive)

- SentaBilstm - This model (about 650M) is based on a bidirectional LSTM structure, and the sentiment types are:
  -Negative (negative)
  -Positive (positive)

### Running example- SentaLstmExample

After successful operation, the command line should see the following information:
```text
...
[INFO ] - 输入句子: 这家餐厅很好吃
#Chinese word segmentation
[INFO ] - Words : [这家, 餐厅, 很好吃]

#Part of speech tagging
[INFO ] - Tags : [r, n, a]

#Sentiment analysis probability
[INFO ] - [0.07149485, 0.9285052]

[INFO ] - negative : 0.07149485
[INFO ] - positive : 0.9285052

```

#### Running example- SentaBilstmExample
After successful operation, the command line should see the following information:
```text
...
[INFO ] - 输入句子: 这家餐厅很好吃
#Chinese word segmentation
[INFO ] - Words : [这家, 餐厅, 很好吃]

#Part of speech tagging
[INFO ] - Tags : [r, n, a]

#Sentiment analysis probability
[INFO ] - [0.059312407, 0.9406876]

[INFO ] - negative : 0.059312407
[INFO ] - positive : 0.9406876

```

### Open source algorithm

### 1. Open source algorithm used by the SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)

