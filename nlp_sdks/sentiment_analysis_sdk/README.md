# 情感倾向分析SDK
情感倾向分析（Sentiment Classification）
针对带有主观描述的中文文本，可自动判断该文本的情感极性类别并给出相应的置信度，
能够帮助企业理解用户消费习惯、分析热点话题和危机舆情监控，为企业提供有利的决策支持。


[点击下载](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK算法：
-  情感倾向分为两类
-  Negative (消极)
-  Positive (积极)

## 运行例子 - SentimentAnalysisExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Number of inter-op threads is 4
[INFO ] - Number of intra-op threads is 4
[INFO ] - input Sentence: I like AIAS. AIAS is the best DL application suite!
[INFO ] - [
	class: "Positive", probability: 0.99927
	class: "Negative", probability: 0.00072
]
```

### 帮助 
添加依赖库：lib/aias-sentiment-analysis-lib-0.1.0.jar
