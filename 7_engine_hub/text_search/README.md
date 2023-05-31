
### Download the Model and Place It in the Models Directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distiluse-base-multilingual-cased-v1.zip

### Text Search

This example provides text search, supports uploading CSV files, extracts features using sentence vector models, and performs subsequent searches based on the Milvus vector engine.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/arc.png)


### Main Features

- Underlying feature vector similarity search
  -Millisecond-level search of billions of data on a single server
  -Near-real-time search, supporting distributed deployment
  -Insert, delete, search, update data anytime

### Sentence Vector Model [Supports 15 Languages]

Sentence vectors refer to real number vectors that map sentences to fixed dimensions. Representing variable-length sentences as fixed-length vectors serves downstream NLP tasks.
Supports 15 languages:
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.

- Sentence Vector
  ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Sentence Vector Applications:

- Semantic Search, retrieve the most matching text in the corpus with the query through sentence vector similarity
  -Text Clustering, convert text to fixed-length vectors, and unsupervisedly cluster similar text through a clustering model
  -Text Classification, represent as sentence vectors, and directly train text classifiers with a simple classifier

### 1. Front-end Deployment

### 1.1 Installation and Running:
```bash
#Install dependencies
npm install
#Run
npm run dev
```

#### 1.2 Build Dist Installation Package:
```bash
npm run build:prod
```

#### 1.3 Nginx Deployment and Running (Mac Environment):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
#Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/Documents/text_search/dist/;
index  index.html index.htm;
        }
......

#Reload Configuration:
sudo nginx -s reload

#Restart after deploying the application:
cd /usr/local/Cellar/nginx/1.19.6/bin

#Quick Stop
sudo nginx -s stop

#Start
sudo nginx
   
```

## 2. Backend Jar Deployment

### 2.1 Environment Requirements:

- System JDK 1.8+
- application.yml   
```bash
#File Storage Path
file:
  mac:
    path: ~/file/
  linux:
    path: /home/aias/file/
  windows:
    path: file:/D:/aias/file/
  #File Size /M
  maxSize: 3000
    ...
```

#### 2.2 Running the Program:
```bash
# Run the Program

java -jar text-search-0.1.0.jar

```

## 3. Backend Vector Engine Deployment (Milvus 2.0)

### 3.1 Environment Requirements:

- Need to install docker runtime environment, Mac environment can use Docker Desktop

### 3.2 Pull Milvus Vector Engine Image (Used for Calculating Feature Value Vector Similarity)

Download the milvus-standalone-docker-compose.yml configuration file and save it as docker-compose.yml    
[Standalone Installation Documentation](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        
```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 Start Docker Container
```bash
sudo docker-compose up -d
```

#### 3.4 Edit Vector Engine Connection Configuration Information
- application.yml
- Edit the vector engine connection IP address 127.0.0.1 according to your needs to the IP address of the host where the container is located
```bash
##################Vector Engine ################
search:
  host: 127.0.0.1
  port: 19530
```

## 4. Open the Browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Upload CSV Data File
  1). Click the upload button to upload the CSV file.
[Test Data](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/example.csv)
  2). Click the feature extraction button.
  Wait for the CSV file to be parsed, feature extraction, and feature storage in the vector engine. Progress information can be seen through the console.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/storage.png)

- Text Search
  Enter text, click query, and see the returned list sorted by similarity.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png)

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

