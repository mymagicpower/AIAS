## 目录：
http://aias.top/

## 音频搜索 - 声纹搜索

### 声纹识别
所谓声纹(Voiceprint)，是用电声学仪器显示的携带言语信息的声波频谱。人类语言的产生是人体语言中枢与发音器官之间一个复杂的生理物理过程，
人在讲话时使用的发声器官--舌、牙齿、喉头、肺、鼻腔在尺寸和形态方面每个人的差异很大，所以任何两个人的声纹图谱都有差异。
声纹识别(Voiceprint Recognition, VPR)，也称为说话人识别(Speaker Recognition)，有两类，
即说话人辨认(Speaker Identification)和说话人确认(Speaker Verification)。前者用以判断某段语音是若干人中的哪一个所说的，是“多选一”问题；而后者用以确认某段语音是否是指定的某个人所说的，是“一对一判别”问题。不同的任务和应用会使用不同的声纹识别技术，如缩小刑侦范围时可能需要辨认技术，而银行交易时则需要确认技术。不管是辨认还是确认，都需要先对说话人的声纹进行建模，这就是所谓的“训练”或“学习”过程。

本例子提供了声纹搜索，使用了短时傅里叶变换算法，声纹特征提取算法。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/arc.png)


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
            root   /Users/calvin/Documents/voiceprint_search/dist/;
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
1). 根据需要编辑图片上传根路径audio_root   
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
    rootPath: ~/file/audio_root/ #压缩包解压缩文件夹
  linux:
    path: /home/aias/file/
    rootPath: /home/aias/file/audio_root/ #压缩包解压缩文件夹
  windows:
    path: file:/D:/aias/file/
    rootPath: file:/D:/aias/file/audio_root/ #压缩包解压缩文件夹
    ...
```

2). 根据需要编辑图片baseUrl 
```bash
image:
  #baseurl是图片的地址前缀
  baseUrl: http://127.0.0.1:8089/files/
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar voiceprint-search-0.1.0.jar

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
[测试声纹音频](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/a_1.wav)
2). 点击特征提取按钮. 
等待音频文件解析，声音采样，短时傅里叶变换提取声音频谱，特征提取，特征存入向量引擎。通过console可以看到进度信息。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/storage.png)

- 音频搜索
  上传wav格式音频文件，点击查询，可以看到返回的清单，根据相似度排序。     
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/search.png)

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html     
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/audio_search/swagger.png)

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