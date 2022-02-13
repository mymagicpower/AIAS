## 目录：
http://aias.top/


## 视频搜索
本例子提供了人像搜索，使用了人脸检测算法，人脸特征提取算法。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/arc.png)


#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

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
            root   /Users/calvin/Documents/video_search/dist/;
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

- application.yml   
1). 根据需要编辑图片上传根路径imageRootPath    
```bash
# 文件存储路径
file:
  mac:
    ...
    imageRootPath: ~/file/image_root/ #图片文件根目录
  linux:
    ....
    imageRootPath: /home/aias/file/image_root/ #图片文件根目录
  windows:
    ...
    imageRootPath: file:/D:/aias/file/image_root/ ##图片文件根目录
    ...
```

2). 根据需要编辑图片baseurl 
```bash
image:
  #baseurl是图片的地址前缀
  baseurl: http://127.0.0.1:8089/images/
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar video-search-0.1.0.jar

```

## 3. 后端向量引擎部署（Milvus 2.0）
#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus向量引擎镜像（用于计算特征值向量相似度）
下载 milvus-standalone-docker-compose.yml 配置文件并保存为 docker-compose.yml        
[单机版安装文档](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        
```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 启动 Docker 容器
```bash
sudo docker-compose up -d
```

#### 3.5 编辑向量引擎连接配置信息
- application.yml
- 根据需要编辑向量引擎连接ip地址127.0.0.1为容器所在的主机ip
```bash
################## 向量引擎 ################
search:
  host: 127.0.0.1
  port: 19530
```

## 4. 打开浏览器
- 输入地址： http://localhost:8090

- 视频上传
1). 点击上传按钮上传视频文件.  
[测试视频](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/test.mp4)
2). 点击提取人脸特征提取按钮. 
等待图片帧解析，人脸检测，特征提取，特征存入向量引擎。通过console可以看到进度信息。
imageRootPath目录下，可以看到提取出的图片帧及检测目标图片。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/storage.png)

- 视频搜索
  上传图片，点击查询，可以看到返回的图片清单，根据相似度排序。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/search.png)

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/swagger.png)

- 初始化向量引擎(清空数据): 
me.aias.tools.MilvusInit.java 

- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   