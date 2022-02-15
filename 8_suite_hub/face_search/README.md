### 目录：
http://aias.top/


### 人脸大数据搜索介绍
- 人像高精度搜索：人脸特征提取(使用人脸特征模型提取512维特征)前先做 - 人脸检测，人脸关键点提取，人脸对齐

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/search_engine.png)


#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

####  功能
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

## 3. 后端向量引擎部署（Milvus 2.0）
#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus向量引擎镜像（用于计算特征值向量相似度）
下载 milvus-standalone-docker-compose.yml 配置文件并保存为 docker-compose.yml        
[单机版安装文档](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        

##### 最新版本请参考官网
- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
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
  dimension: 512 #dimension of each vector
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
me.calvin.modules.search.tools.MilvusInit.java
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   