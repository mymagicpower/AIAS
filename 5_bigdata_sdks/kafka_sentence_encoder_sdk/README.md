
### Download the model and place it in the 'models' directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distiluse-base-multilingual-cased-v1.zip

## Kafka- Sentence Vector Extraction [Supports 15 Languages] SDK

Sentence vectors refer to real number vectors that map sentences to fixed dimensions.
Representing sentences of varying lengths as fixed-length vectors provides downstream NLP tasks with a service.
Supports 15 languages:
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.

- Sentence Vector   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

Applications of Sentence Vectors:

- Semantic search: Retrieving the most matching text from the corpus with the query based on similarity of sentence vectors.
- Text clustering: Converting text into fixed-length vectors and clustering similar texts in an unsupervised fashion using clustering models.
- Text classification: Representing text as sentence vectors and training a text classifier using a simple classifier.

### SDK Features:

- Sentence vector extraction
- Reading Kafka topic

### 1. Start Zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. Start Kafka:

`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. Create a topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic sentence-data`

#### 4. View the created topic:

`kafka-topics --list --zookeeper localhost:2181`

#### 5. Enter test data
```bash
kafka-console-producer --broker-list localhost:9092 --topic sentence-data
>This restaurant is very delicious
```

### 6. Run the example- SentenceEncoderExample

After running successfully, the command line should display the following information:
```bash
...
content: How many people live in Berlin?
Vector dimensions: 512
Sentence1 embeddings: [-0.025924467, -0.0054853377, ..., -0.02703922, -0.024842339]

content: This restaurant is very delicious
Vector dimensions: 512
Sentence1 embeddings: [-0.0035688172, -0.017706484, ..., 0.0061081746, -0.023076165]
...
```

#### Kafka installation on Mac
```bash
brew install kafka
```