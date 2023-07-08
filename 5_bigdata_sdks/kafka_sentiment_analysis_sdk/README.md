
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distilbert_sst_english.zip

### Kafka Sentiment Analysis [English] SDK

Sentiment Analysis (Sentiment Classification)
For text with subjective descriptions, it can automatically determine the sentiment polarity category of the text and give the corresponding confidence level, which can help enterprises understand user consumption habits, analyze hot topics and crisis public opinion monitoring, and provide favorable decision support for enterprises.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)


### SDK algorithm:

- The sentiment polarity is divided into two categories
  -Negative
  -Positive

### 1. Start zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. Start kafka:

`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. Create a topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic twitter-data`

#### 4. Download and import test data (twitter data.txt file under src/test/resources directory)
[Click to download test data](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/data.txt)

`kafka-console-producer --broker-list localhost:9092 --topic twitter-data < data.txt`

#### 5. Run the example- SentimentAnalysisExample
After successful operation, the command line should see the following information:
```bash
...
content: is alone downstairs...working 
prediction: [
	class: "Negative", probability: 0.98781
	class: "Positive", probability: 0.01218
]
content: I feel bad for doing it 
prediction: [
	class: "Negative", probability: 0.99725
	class: "Positive", probability: 0.00274
]
content: @RyanSeacrest is it just me, or she hates anoop. i mean seriously, she's kinda mean to him. 
prediction: [
	class: "Negative", probability: 0.99816
	class: "Positive", probability: 0.00183
]
...
```

#### Install Kafka in Mac environment
```bash
brew install kafka
```