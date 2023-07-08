
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distilbert_sst_english.zip

### Flink-Sentiment Analysis [English] SDK

Sentiment analysis (Sentiment Classification)
For text with subjective descriptions, it can automatically determine the sentiment polarity category of the text and give the corresponding confidence level,
It can help enterprises understand user consumption habits, analyze hot topics and monitor crisis public opinion, and provide favorable decision support for enterprises.
 
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)


### SDK Algorithm:

- Sentiment polarity is divided into two categories
  -Negative
  -Positive

### Environment Preparation

Flink connects to the server port and reads data from the port. We use the lightest netcat for testing.
NC (netcat) is known as the Swiss Army Knife in network tools, small in size but powerful in function.

### 1. Linux/Mac
```
nc -l 9000
```
#### 2. Windows
```
nc -l -p 9000
```

### Running Example - SentenceEncoderExample
[Click to download test data](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/data.txt)
#### Enter the statement on the nc command line
```bash
...
CalvindeMacBook-Pro:~ calvin$ nc -l 9000
is alone downstairs...working
I feel bad for doing it
@RyanSeacrest is it just me, or she hates anoop. i mean seriously, she's kinda mean to him.
```
#### The corresponding statement sentiment classification result can be seen on the IDE command line
```bash
[
	class: "Negative", probability: 0.98781
	class: "Positive", probability: 0.01218
]
[
	class: "Negative", probability: 0.99725
	class: "Positive", probability: 0.00274
]
[
	class: "Negative", probability: 0.99816
	class: "Positive", probability: 0.00183
]
```

#### Mac environment install netcat
```bash
brew install nc
```