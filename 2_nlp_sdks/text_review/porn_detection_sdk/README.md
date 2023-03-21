
### Download the model and put it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/review_detection_lstm.zip

### Text-Text Review SDK [Chinese]

The porn detection model can automatically determine whether the text is yellow and give the corresponding confidence level. It can identify pornographic descriptions, vulgar dating, and obscene text.

Application scenarios:
Applicable to video live broadcast barrage, community forum messages, etc., to detect user comment information. Once illegal content is found, automatic review and real-time filtering are performed to ensure a good user experience.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/text_review.jpeg)


### SDK algorithm:

The LSTM network structure is used and the word granularity is used for segmentation, which has high classification accuracy. The maximum sentence length of the model is 256 words.

### Running example- ReviewExample

After successful operation, the command line should see the following information:
```text
...

[INFO ] - 测试1: 黄色内容
[INFO ] - [0.03643743, 0.96356255]
[INFO ] - not_porn : 0.03643743
[INFO ] - porn : 0.96356255

[INFO ] - 测试2: 黄牛党
[INFO ] - [0.9998598, 1.4011623E-4]
[INFO ] - not_porn : 0.9998598
[INFO ] - porn : 1.4011623E-4
```

### Open source algorithm

### 1. Open source algorithm used by the SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)

