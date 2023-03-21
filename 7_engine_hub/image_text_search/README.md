
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-IMAGE.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/M-BERT-Base-ViT-B.zip

### Cross-modal similarity comparison and retrieval for images and text [supports 40 languages]

This example demonstrates the ability to search for images through text (the model itself also supports searching for text through images, or mixed search).

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/arc.png)


### Main Features

- Uses feature vector similarity search at the bottom level
- Millisecond-level search for billions of data on a single server
- Near-real-time search, supports distributed deployment
- Can insert, delete, search, update, and other operations on data at any time

### Background introduction

OpenAI has released two new neural networks: CLIP and DALL·E. They combine NLP (natural language recognition) with image recognition and have a better understanding of images and language in daily life.
Previously, text was used to search for text, and images were used to search for images. Now, with the CLIP model, text can be used to search for images, and images can be used to search for text. The implementation idea is to map images and text to the same vector space. In this way, cross-modal similarity comparison and retrieval of images and text can be realized.

- Feature vector space (composed of images and text)
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

### CLIP - "Alternative" Image Recognition

Currently, most models learn to recognize images from labeled examples in labeled datasets, while CLIP learns to recognize images and their descriptions obtained from the Internet, thus understanding images through a description rather than word labels such as "cat" and "dog."
To do this, CLIP learns to associate a large number of objects with their names and descriptions, and can thus recognize objects outside the training set.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip.png)
As shown in the figure above, the CLIP network workflow: pre-trains the image encoder and text encoder to predict which images are paired with which text in the dataset.
Then, CLIP is converted to a zero-shot classifier. In addition, all classifications in the dataset are converted into labels such as "a photo of a dog" and the best match image is predicted.

CLIP model address:
https://github.com/openai/CLIP/blob/main/README.md

#### Supported language list:
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/languages.png)

### 1. Front-end deployment

### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 Nginx deployment and operation (mac environment as an example):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/Documents/image_text_search/dist/;
index  index.html index.htm;
        }
......

# Reload the configuration:
sudo nginx -s reload

# After deploying the application, restart it:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Fast stop
sudo nginx -s stop

# Start
sudo nginx
```

### 2. Back-end jar deployment

### 2.1 Environment requirements:

- System JDK 1.8+
- application.yml
  1). Edit the image upload root path rootPath according to your needs  
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
    # folder for unzip files
    rootPath: ~/file/data_root/
  linux:
    path: /home/aias/file/
    rootPath: /home/aias/file/data_root/
  windows:
    path: file:/D:/aias/file/
    rootPath: file:/D:/aias/file/data_root/
    ...
```

2). Edit the image baseurl according to your needs
```bash
image:
  #baseurl is the address prefix of the image
baseurl: <http://127.0.0.1:8089/files/>
```

#### 2.2 Running the program:
```bash
# run the program

java -jar image-text-search-0.1.0.jar

```

### 3. Backend vector engine deployment (Milvus 2.0)

### 3.1 Environment requirements:

- Docker runtime environment is required. Mac environment can use Docker Desktop

### 3.2 Pull Milvus vector engine image (used to calculate feature vector similarity)

Download the milvus-standalone-docker-compose.yml configuration file and save it as docker-compose.yml      
[Standalone installation document](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        
```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 Start Docker container
```bash
sudo docker-compose up -d
```

#### 3.4 Edit vector engine connection configuration information
- application.yml
- Edit the vector engine connection IP address 127.0.0.1 to the IP of the host where the container is located as needed
```bash
################## Vector engine ################
search:
  host: 127.0.0.1
  port: 19530
```

### 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)

### 4.1 Image upload

1). Click the upload button to upload the file.
[Test image data](https://github.com/mymagicpower/AIAS/releases/download/apps/images.zip)
2). Click the feature extraction button.
Wait for the image feature extraction and store it in the vector engine. The progress information can be seen through the console.
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/storage.png)

### 4.2 Cross-modal search-text search image

Enter the text description and click the query button to see the returned list of images sorted by similarity.

- Example 1, enter text: car
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search1.png)

- Example 2, enter text: two dogs on the snow
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search2.png)

#### 4.3 Cross-modal search-image search image
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png)

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

