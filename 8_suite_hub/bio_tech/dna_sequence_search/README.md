
### DNA Sequence Search

This example provides DNA sequence search, supporting file uploads, using Spark Mlib to extract features, and subsequent retrieval based on the Milvus vector engine.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/arc.png"  width = "600"/>
</div> 

### Engine Features

- Underlying feature vector similarity search
  -Millisecond-level search of billions of data on a single server
  -Near real-time search, supporting distributed deployment
  -The ability to insert, delete, search, and update data at any time

### DNA Background Introduction

Deoxyribonucleic acid (DNA) is one of four kinds of biomolecules in biological cells. DNA carries the genetic information necessary for the synthesis of RNA and proteins, and is an essential biomolecule for the development and normal operation of organisms.
DNA sequencing methods include optical sequencing and chip sequencing.

### Feature Extraction

Text (DNA sequence) feature extraction: The process of converting text data into feature vectors. The most common text feature representation method is the bag-of-words method.

The CountVectorizer is a common feature numerical calculation class and a text feature extraction method. For each training text, it only considers the frequency of each vocabulary in the training text.
CountVectorizer will convert the words in the text into a word frequency matrix, and it calculates the number of occurrences of each word through the fit function.
CountVectorizer is designed to convert a document into a vector by counting. When there is no prior dictionary, Countvectorizer extracts vocabulary as an estimator for training, and generates a CountVectorizerModel to store the corresponding vocabulary vector space. The model produces a sparse representation of the document with respect to the words.
During the training process of CountVectorizerModel, CountVectorizer selects the vocabulary according to the frequency from high to low in the corpus, and the maximum content of the vocabulary list is specified by the vocabsize hyperparameter. The hyperparameter minDF specifies that the words in the vocabulary list must appear in at least how many different documents.
The model training and inference used Spark Mlib:

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/spark_mlib.png"  width = "400"/>
</div> 

- [Algorithm Detailed Introduction](http://spark.apache.org/docs/latest/ml-features.html#countvectorizer)


#### Vector Engine Indexing Strategy
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/milvus.png"  width = "600"/>
</div> 


### 1. Front-End Deployment

### 1.1 Install and Run:
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

#### 1.3 Nginx Deployment Operation (Mac Environment):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen 8080;
server_name localhost;

location / {
root /Users/calvin/Documents/dna_sequence_search/dist/;
index index.html index.htm;
}
......

# Reload configuration:
sudo nginx -s reload

# After deploying the application, restart it:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Quick stop
sudo nginx -s stop

# Start up
sudo nginx

```

### 2. Back-End Jar Deployment

### 2.1 Environmental Requirements:

- System JDK 1.8+ (recommended 1.8, 11)
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

#### 2.2 Running the Program:
```bash
java -jar dna-sequence-search-0.1.0.jar

```

### 3. Back-End Vector Engine Deployment (Milvus 2.0)

### 3.1 Environmental requirements:

- A docker running environment is required. The Mac environment can use Docker Desktop.

### 3.2 Pull Milvus Vector Engine image (for calculating feature vector similarity):

Download the milvus-standalone-docker-compose.yml configuration file and save it as docker-compose.yml.

[Standalone Installation Document](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        
```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 Start the Docker container:
```bash
sudo docker-compose up -d
```

#### 3.4 Edit vector engine connection configuration information
- application.yml
- Edit the vector engine connection IP address 127.0.0.1 according to your needs to the IP address of the host where the container is located.
```bash
################## Vector Engine ################
search:
  host: 127.0.0.1
  port: 19530
```

### 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Upload data files
  1). Click the upload button to upload files.
[Test Data](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/human_data.txt)
  2). Click the feature extraction button.
  Wait for the file to be parsed, the model to be trained, the feature to be extracted, and the feature to be stored in the vector engine. You can see progress information through the console.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/storage.png"  width = "600"/>
</div> 

- DNA sequence search
Enter text and click query to see the returned list sorted by similarity.
```text
ATGCCCCAACTAAATACTACCGTATGGCCCACCATAATTACCCCCATACTCCTTACACTATTCCTCATCACCCAACTAAAAATATTAAACACAAACTACCACCTACCTCCCTCACCAAAGCCCATAAAAATAAAAAATTATAACAAACCCTGAGAACCAAAATGAACGAAAATCTGTTCGCTTCATTCATTGCCCCCACAATCCTAG
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/search.png"  width = "600"/>
</div> 

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

