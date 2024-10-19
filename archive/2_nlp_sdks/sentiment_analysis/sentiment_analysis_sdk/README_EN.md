
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distilbert_sst_english.zip

### Sentiment Analysis SDK

Sentiment Analysis (Sentiment Classification)
For Chinese texts with subjective descriptions, it can automatically determine the sentiment polarity category of the text and give the corresponding confidence level. It can help companies understand user consumption habits, analyze hot topics and crisis public opinion monitoring, and provide favorable decision support for enterprises.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK algorithm:

- Sentiment is divided into two categories
  -Negative (negative)
  -Positive (positive)

### Run the example- SentimentAnalysisExample

After a successful run, the command line should see the following information:
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