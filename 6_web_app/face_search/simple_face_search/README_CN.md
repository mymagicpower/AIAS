### 目录：
https://aias.top/



### 下载模型，更新 face_vector_search 的yaml配置文件
- 链接: https://pan.baidu.com/s/1XbMhZ4c1B-7hKKpCJMte2Q?pwd=ctva

```bash
# Model URI
#Face
face:
  saveDetectedFace: true
  # 高精度大模型 retinaface detection model URI
  retinaface: /Users/calvin/ai_projects/face_search_bak/simple_face_search/face_search/models/retinaface.onnx
  # 速度快小模型，精度低于大模型 mobilenet face detection model URI
  mobilenet: /Users/calvin/ai_projects/face_search_bak/simple_face_search/face_search/models/mobilenet.onnx
  # face feature model URI
  feature: /Users/calvin/ai_projects/face_search_bak/simple_face_search/face_search/models/face_feature.onnx

model:
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
```


### 人脸大数据搜索介绍
- 人像高精度搜索：人脸特征提取(使用人脸特征模型提取128维特征)前先做 - 人脸检测，人脸关键点提取，人脸对齐

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/search_engine.png)


#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

#### 功能介绍
- 人像搜索：上传人像图片搜索
- 数据管理：提供图像压缩包(zip格式)上传，人像特征提取

### 1. 前端部署

#### 1.1 直接运行：
```bash
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境为例)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/face_search/dist/;
            index  index.html index.htm;
        }
     ......
     
# 重新加载配置：
sudo nginx -s reload 

# 部署应用后，重启：
cd /usr/local/Cellar/nginx/1.19.6/bin

# 快速停止
sudo nginx -s stop

# 启动
sudo nginx     
```

## 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+
- 需要安装redis
- 需要安装MySQL数据库

#### 2.2 运行程序：
```bash
# 运行程序

java -jar face-search-1.0.jar

```

### 3. 后端向量引擎部署

#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus向量引擎镜像（用于计算特征值向量相似度）
下载 milvus-standalone-docker-compose.yml 配置文件并保存为 docker-compose.yml     

[单机版安装文档](https://milvus.io/docs/install_standalone-docker.md)       
[引擎配置文档](https://milvus.io/docs/configure-docker.md)   
[milvus-sdk-java](https://github.com/milvus-io/milvus-sdk-java)  


```bash
# 例子：v2.2.11，请根据官方文档，选择合适的版本
wget https://github.com/milvus-io/milvus/releases/download/v2.2.11/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 启动 Docker 容器
```bash
sudo docker-compose up -d
```

#### 3.4 编辑向量引擎连接配置信息
- application.yml
- 根据需要编辑向量引擎连接ip地址127.0.0.1为容器所在的主机ip
```bash
##################### 向量引擎 ###############################
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 256
  nlist: 16384
  dimension: 128 #dimension of each vector
  collectionName: face_search #collection name

```

## 4. 打开浏览器
- 输入地址： http://localhost:8089

- 图片上传        
1). 点击上传按钮上传zip压缩包.  
2). 点击提取人脸特征按钮.  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/data.png)

- 人像搜索
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/search.png)


- 重置Milvus向量引擎(清空数据):  
```bash
top.aias.tools.MilvusInit.java
```



#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html
