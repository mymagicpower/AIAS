
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distiluse-base-multilingual-cased-v1.zip

### Flink-Sentence Vector Extraction SDK [supports 15 languages]

Sentence vectors refer to mapping sentences to fixed-dimensional real vectors.
Representing variable-length sentences as fixed-length vectors to provide services for downstream NLP tasks.
Supports 15 languages: 
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.
 
- Sentence vectors
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Applications of sentence vectors:

- Semantic search, retrieve the text that best matches the query in the corpus based on the similarity of sentence vectors
- Text clustering, convert text to fixed-length vectors, and cluster similar text unsupervisedly through a clustering model
- Text classification, represented as sentence vectors and trained with simple classifiers to classify text

### SDK functions:

- Flink sentence vector extraction

### Environment preparation

Connect to the Flink server port and read data from the port. We use the lightest netcat for testing.
NC (Netcat) is known as the Swiss Army Knife of network tools, small in size but powerful in function.

### 1. Linux/Mac
```
nc -l 9000
```
#### 2. Windows
```
nc -l -p 9000
```

### Running example - SentenceEncoderExample

### Enter the sentence in the nc command line
```bash
...
CalvindeMacBook-Pro:~ calvin$ nc -l 9000
How many people live in Berlin?
This restaurant is delicious
```
#### The corresponding sentence feature vector can be seen in the IDE command line
```bash
[-0.025924467, -0.0054853377, 0.035019025, ..., -0.02703922, -0.024842339]
[-0.0035688172, -0.017706484, 0.050606336, ..., 0.0061081746, -0.023076165]
```

#### Mac environment installation of netcat
```bash
brew install nc
```