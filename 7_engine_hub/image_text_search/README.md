### 目录：
http://aias.top/


### 图像&文本的跨模态相似性比对检索【支持40种语言】
本例子提供了通过文本搜图片的能力展示（模型本身当然也支持图片搜文字，或者混合搜索）。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/arc.png)


#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

#### 背景介绍
OpenAI 发布了两个新的神经网络：CLIP 和 DALL·E。它们将 NLP（自然语言识别）与 图像识别结合在一起，对日常生活中的图像和语言有了更好的理解。
之前都是用文字搜文字，图片搜图片，现在通过CLIP这个模型，可是实现文字搜图片，图片搜文字。其实现思路就是将图片跟文本映射到同一个向量空间。如此，就可以实现图片跟文本的跨模态相似性比对检索。      
- 特征向量空间（由图片 & 文本组成）    
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

#### CLIP - “另类”的图像识别
目前，大多数模型学习从标注好的数据集的带标签的示例中识别图像，而 CLIP 则是学习从互联网获取的图像及其描述, 即通过一段描述而不是“猫”、“狗”这样的单词标签来认识图像。
为了做到这一点，CLIP 学习将大量的对象与它们的名字和描述联系起来，并由此可以识别训练集以外的对象。
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip.png)
如上图所示，CLIP网络工作流程： 预训练图编码器和文本编码器，以预测数据集中哪些图像与哪些文本配对。
然后，将CLIP转换为zero-shot分类器。此外，将数据集的所有分类转换为诸如“一只狗的照片”之类的标签，并预测最佳配对的图像。

CLIP模型地址：
https://github.com/openai/CLIP/blob/main/README.md

#### 支持的语言列表：
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/languages.png)

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
            root   /Users/calvin/Documents/image_text_search/dist/;
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
    path: file:/D:/aias/file/
    rootPath: file:/D:/aias/file/data_root/
    ...
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

java -jar image-text-search-0.1.0.jar

```

### 3. 后端向量引擎部署（Milvus 2.0）
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

### 4. 打开浏览器
- 输入地址： http://localhost:8090

#### 4.1 图片上传
1). 点击上传按钮上传文件.  
[测试图片数据](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/images.zip)
2). 点击特征提取按钮. 
等待图片特征提取，特征存入向量引擎。通过console可以看到进度信息。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/storage.png)

#### 4.2 跨模态搜索 - 文本搜图
  输入文字描述，点击查询，可以看到返回的图片清单，根据相似度排序。

- 例子1，输入文本：车
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search1.png)

- 例子2，输入文本：雪地上两只狗
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search2.png)

#### 4.3 跨模态搜索 - 以图搜图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png)

### 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/swagger.png)

- 初始化向量引擎(清空数据): 

```bash
me.aias.tools.MilvusInit.java 
       ...
```

- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   