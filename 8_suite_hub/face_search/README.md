
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/face_feature.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/ultranet.zip

### Introduction to face big data search
- Person image high-precision search: Face feature extraction (use face feature model to extract 512-dimensional features) First, do face detection, face key point extraction, and face alignment

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/search_engine.png)


####M ain features
-The bottom layer uses feature vector similarity search
-Millisecond level search for billions of data on a single server
-Near real-time search, support distributed deployment
-Insert, delete, search, update, etc. can be performed on data at any time
-Support online user management and server performance monitoring, support limiting single user login

#### Function
-People search: Upload a portrait picture for search
-Data management: Provide image compression package (zip format) upload, portrait feature extraction

### 1. Front-end deployment
#### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build dist installation package:
```bash
npm run build:prod
```

#### 1.3 Deploy and run nginx (mac environment)
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
#Edit nginx.conf

server {
listen 8080;
server_name localhost;

location / {
root/Users/calvin/face_search/dist/;
index index.html index.htm;
}
......

#Reload configuration:
sudo nginx -s reload

#After deploying the application, restart:
cd /usr/local/Cellar/nginx/1.19.6/bin

#Fast stop
sudo nginx -s stop

#Start
sudo nginx
```

## 2. Backend jar deployment
#### 2.1 Environmental requirements:
-System JDK 1.8+
-Need to install redis
-Need to install MySQL database

#### 2.2 Running the program:
```bash
java -jar face-search-1.0.jar

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
- Edit the vector engine connection IP address 127.0.0.1 to the host IP of the container as needed

```bash
#####################向量引擎 ###############################
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 256
  nlist: 16384
  dimension: 512 #dimension of each vector
  collectionName: face_search #collection name

```

##4. Open the browser
-Enter the address: [http://localhost:8089](http://localhost:8089/)

- Upload pictures
  1). Click the upload button to upload the zip package.
  2). Click the Extract Face Features button.
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/data.png)

- People search
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/search.png)


- Reset the Milvus vector engine (clear data):
```bash
me.calvin.modules.search.tools.MilvusInit.java
```

