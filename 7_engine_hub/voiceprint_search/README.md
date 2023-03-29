
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/voiceprint.zip

### Audio search-voiceprint search

### Voiceprint Recognition

The so-called voiceprint is a sound wave frequency spectrum that carries speech information displayed by electro-acoustic instruments. The generation of human language is a complex physiological and physical process between the human language center and the pronunciation organ. The size and shape of the pronunciation organs used by humans when speaking - the tongue, teeth, larynx, lungs, and nasal cavity vary greatly from person to person, so there are differences between the voiceprints of any two people. Voiceprint Recognition (VPR), also known as Speaker Recognition, has two types, Speaker Identification and Speaker Verification. The former is used to determine which of several people a certain speech segment is spoken by, which is a "multiple-choice" problem, while the latter is used to confirm whether a certain speech segment is spoken by a designated person, which is a "one-to-one discrimination" problem. Different tasks and applications will use different voiceprint recognition technologies, such as identification technology when narrowing the scope of criminal investigation, and confirmation technology when conducting bank transactions. Whether it is identification or confirmation, the speaker's voiceprint needs to be modeled first, which is the so-called "training" or "learning" process.

This example provides voiceprint search, which uses the short-time Fourier transform algorithm and the voiceprint feature extraction algorithm.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/arc.png)


### Main Features

- Using feature vector similarity search at the bottom layer
- Millisecond-level search for 1 billion-level data on a single server
- Near-real-time search, supporting distributed deployment
- Insert, delete, search, update data anytime

### 1. Front-end deployment

### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 Nginx deployment operation (mac environment is used as an example):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/Documents/voiceprint_search/dist/;
index  index.html index.htm;
        }
......

# Reload the configuration:
sudo nginx -s reload

# After deploying the application, restart:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Quick stop
sudo nginx -s stop

# Start up
sudo nginx
  
```

## 2. Backend jar deployment

### 2.1 Environment requirements:

- System JDK 1.8+
- application.yml
  1). Edit the audio_root according to the needs of the image upload root path
```bash
# File storage path
file:
  mac:
    path: ~/file/
    rootPath: ~/file/audio_root/ # Unzip the folder
  linux:
    path: /home/aias/file/
    rootPath: /home/aias/file/audio_root/ # Unzip the folder
  windows:
    path: file:/D:/aias/file/
    rootPath: file:/D:/aias/file/audio_root/ # Unzip the folder
    ...
```

2). Edit the baseUrl according to the needs of the image
```bash
image:
  #baseurl is the address prefix of the picture
baseUrl: <http://127.0.0.1:8089/files/>
```

#### 2.2 Run the program:
```bash
java -jar voiceprint-search-0.1.0.jar

```

## 3. Backend vector engine deployment
#### 3.1 Environmental requirements:
-Need to install docker operating environment, Mac environment can use Docker Desktop

#### 3.2 Pull Milvus vector engine image (used to calculate feature vector similarity)
Download the milvus-standalone-docker-compose.yml configuration file and save it as docker-compose.yml     

[Standalone Installation Document](https://milvus.io/docs/install_standalone-docker.md)   
[Configure Milvus](https://milvus.io/docs/install_standalone-docker.md)        


##### Please refer to the latest version of the official website
- Milvus vector engine reference link
  [Milvus vector engine official website](https://milvus.io/)      
  [Milvus vector engine Github](https://github.com/milvus-io)

```bash
# example - v2.2.4
wget https://github.com/milvus-io/milvus/releases/download/v2.2.4/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 Start Docker container
```bash
sudo docker-compose up -d
```

#### 3.4 Edit the vector engine connection configuration information
- application.yml
- Edit the vector engine connection ip address 127.0.0.1 according to the needs of the container host ip
```bash
################## Vector Engine ################
search:
  host: 127.0.0.1
  port: 19530
```

## 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Video upload
  1). Click the upload button to upload the video file.
[Test voiceprint audio](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/a_1.wav)
  2). Click the feature extraction button.
  Wait for the audio file to be parsed, sound sampling, short-time Fourier transform to extract sound spectrum, feature extraction, and feature storage into the vector engine. The progress information can be seen through console.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/storage.png)

- Audio search
  Upload wav format audio file, click query, you can see the returned list, sorted by similarity.   
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/search.png)

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

