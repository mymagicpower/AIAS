### 目录：
http://aias.top/

#### 模型下载：
- 链接:https://pan.baidu.com/s/16933J3dX16xnjbYaay-4og?pwd=cwxk

### 以图搜图【向量引擎精简版】
#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

#### 功能介绍
- 以图搜图：上传图片搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


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
            root   /Users/calvin/Documents/image_search/dist/;
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

### 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+

- application.yml   
1). 根据需要编辑图片上传根路径rootPath    
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
    path: C:\\
    rootPath: C:\\data_root\\
    ...
```
1). 更新模型路径   
```bash
# Model URI
model:
  # Embedding Model
  imageModel: /Users/calvin/products/4_apps/simple_image_search/image_search/models/CLIP-ViT-B-32-IMAGE.pt
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
```


2). 根据需要编辑图片baseurl 
```bash
image:
  #baseurl是图片的地址前缀
  baseurl: http://127.0.0.1:8089/files/
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar image-search-0.1.0.jar

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
# 例子：v2.2.8，请根据官方文档，选择合适的版本
wget https://github.com/milvus-io/milvus/releases/download/v2.2.8/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 启动 Docker 容器
```bash
sudo docker-compose up -d
```

#### 3.4 编辑向量引擎连接配置信息
- aiplatform-system\src\main\resources\application-dev.yml
- 根据需要编辑向量引擎连接ip地址127.0.0.1为容器所在的主机ip
```bash
##################### 向量引擎 ###############################
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 256
  nlist: 16384
  dimension: 512 #dimension of each vector
  collectionName: arts #collection name

```


### 4. 功能使用
打开浏览器
- 输入地址： http://localhost:8090

#### 4.1 图片上传
1). 点击上传按钮上传文件.  
[测试图片数据](https://pan.baidu.com/s/1QtF6syNUKS5qkf4OKAcuLA?pwd=wfd8)
2). 点击特征提取按钮. 
等待图片特征提取，特征存入json文件。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/mini_search_3.png)

#### 4.2 以图搜图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/mini_search_2.png)

### 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/swagger.png)


### 官网：
[官网链接](http://www.aias.top/)


#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html
