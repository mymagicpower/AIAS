
### Download the model and put it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/face_feature.zip

### Video Search

This example provides portrait search, which uses face detection algorithm and face feature extraction algorithm.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/arc.png)


### Main Features

- Low-level feature vector similarity search
- Millisecond-level search of billions of data on a single server
- Near real-time search, support for distributed deployment
- Insert, delete, search, update and other operations on data at any time

### 1. Front-end deployment

### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build dist installation package:
```bash
npm run build:prod
```

#### 1.3 Nginx deployment operation (mac environment):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/Documents/video_search/dist/;
index  index.html index.htm;
        }
......

# Reload configuration:
sudo nginx -s reload

# After deploying the application, restart:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Quick stop
sudo nginx -s stop

# Start
sudo nginx
```

## 2. Back-end jar deployment

### 2.1 Environmental requirements:

- System JDK 1.8+
- application.yml
  1). Edit the image upload root path imageRootPath as required
```bash
#File storage path
file:
  mac:
    ...
    imageRootPath: ~/file/image_root/ #Image file root directory
  linux:
    ....
    imageRootPath: /home/aias/file/image_root/ #Image file root directory
  windows:
    ...
    imageRootPath: file:/D:/aias/file/image_root/ ##Image file root directory
    ...
```

2). Edit the image baseurl as required
```bash
image:
  #baseurl is the prefix of the image address
baseurl: <http://127.0.0.1:8089/images/>
```

#### 2.2 Run the program:
```bash
java -jar video-search-0.1.0.jar

```

## 3. Back-end vector engine deployment (Milvus 2.0)

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

#### 3.4 Edit vector engine connection configuration information
- application.yml
- Edit the vector engine connection IP address 127.0.0.1 to the host IP address of the container as required
```bash
##################Vector engine ################
search:
  host: 127.0.0.1
  port: 19530
```

## 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Video upload
  1). Click the upload button to upload the video file.
[Test video](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/test.mp4)
  2). Click the button to extract facial features.
  Wait for image frame parsing, face detection, feature extraction, and feature storage in the vector engine. You can see the progress information through the console.
  In the imageRootPath directory, you can see the extracted image frames and detected target images.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/storage.png)

- Video Search
  Upload pictures, click search, and you can see the list of returned pictures sorted by similarity.

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/search.png)

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

