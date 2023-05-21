
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distiluse-base-multilingual-cased-v1.zip

## Question Answering System

A Question Answering System (QA) is a more advanced form of an information retrieval system that can accurately and concisely answer questions posed by users in natural language. Its research has risen mainly due to the need for rapid and accurate information acquisition. The Question Answering System is a research direction that is highly concerned and has broad development prospects in the field of artificial intelligence and natural language processing.

### Text Search Engine

This example is based on a text search engine that supports uploading CSV files, using the sentence vector model to extract features, and subsequent retrieval based on the Milvus vector engine.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/8_suite_hub/question_answering_system/arc.png)


### Key Features

- Low-level feature vector similarity search
  -Millisecond-level search of billions of data on a single server
  -Nearly real-time search, supporting distributed deployment
  -Insertion, deletion, search, update and other operations can be performed on data at any time

### Sentence Vector Model [Supports 15 Languages]

A sentence vector refers to a real-valued vector that maps a sentence to a fixed dimension. Representing variable-length sentences with fixed-length vectors provides services for downstream NLP tasks.
Supports 15 languages:
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.

- Sentence Vector   
  ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Sentence Vector Applications:

- Semantic search/Question Answering System, retrieve the most matching text in the corpus with the query through sentence vector similarity
  -Text clustering, text is transformed into a fixed-length vector, and similar text can be clustered without supervision through a clustering model
  -Text classification, represented as sentence vectors, and trained text classifiers directly with simple classifiers

### 1. Frontend Deployment

### 1.1 Installation and Running:
```bash
# Install dependencies
npm install
# Run
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 Nginx deployment operation (Mac environment example):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen 8080;
server_name localhost;

location / {
root /Users/calvin/Documents/qa_system/dist/;
index index.html index.htm;
        }
......

# Reload configuration:
sudo nginx -s reload

# Restart after deploying the application:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Quick stop
sudo nginx -s stop

# Start
sudo nginx
```

### 2. Backend Jar Deployment

### 2.1 Environmental Requirements:

- System JDK 1.8+
- application.yml   
```bash
# File storage path
file:
mac:
path: ~/file/
linux:
path: /home/aias/file/
windows:
path: file:/D:/aias/file/
# File size /M
maxSize: 3000
    ...
```

#### 2.2 Run the Program:
```bash
java -jar qa-system-0.1.0.jar

```

### 3. Backend Vector Engine Deployment (Milvus 2.0)

### 3.1 Environmental Requirements:

- Docker runtime environment is required, Docker Desktop can be used in Mac environment

### 3.2 Pull Milvus Vector Engine Image (used to calculate feature vector similarity)

Download the milvus-standalone-docker-compose.yml configuration file and save it as docker-compose.yml      
[Standalone installation documentation](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        
```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 Start Docker Container
```bash
sudo docker-compose up -d
```

#### 3.4 Edit the vector engine connection configuration information
- application.yml
- Edit the vector engine connection IP address 127.0.0.1 to the IP address of the host where the container is located as needed
```bash
################## Vector Engine ################
search:
  host: 127.0.0.1
  port: 19530
```

### 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Upload CSV data file
  1). Click the upload button to upload the CSV file.
[Test data](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/8_suite_hub/question_answering_system/example.csv)
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/8_suite_hub/question_answering_system/data.png)


2). Click the feature extraction button.
Wait for CSV file parsing, feature extraction, and feature storage into the vector engine. Progress information can be seen through the console.


![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/8_suite_hub/question_answering_system/storage.png)

- Text search
Enter the text, click the query, and you can see the returned list sorted by similarity.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/8_suite_hub/question_answering_system/search.png)

### 5. Help information

- swagger interface document:
  http://localhost:8089/swagger-ui.html
  ![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/swagger.png)

- Initialize the vector engine (clear data):

```bash
me.aias.tools.MilvusInit.java 
       ...
```

- Milvus vector engine reference link
  [Milvus vector engine official website](https://milvus.io/)      
  [Milvus vector engine Github](https://github.com/milvus-io)

