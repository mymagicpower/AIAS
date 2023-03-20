### 目录：
http://aias.top/

### DNA序列搜索
本例子提供了DNA序列搜索，支持上传文件文件，使用spark mlib计算模型提取特征，并基于milvus向量引擎进行后续检索。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/arc.png"  width = "600"/>
</div> 

#### 引擎特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

#### DNA背景介绍
脱氧核糖核酸（英文DeoxyriboNucleic Acid，缩写为DNA）是生物细胞内含有的四种生物大分子之一核酸的一种。
DNA携带有合成RNA和蛋白质所必需的遗传信息，是生物体发育和正常运作必不可少的生物大分子。
DNA序列指使用一串字母（A、T、C、G）表示的真实的或者假设的携带基因信息的DNA分子的一级结构。
DNA序列测定方法有光学测序和芯片测序两种。

#### 特征提取
文本(DNA序列)特征提取：将文本数据转化成特征向量的过程，比较常用的文本特征表示法为词袋法。
词袋法：不考虑词语出现的顺序，每个出现过的词汇单独作为一列特征，这些不重复的特征词汇集合为词表。
- CountVectorizer是属于常见的特征数值计算类，是一个文本特征提取方法。对于每一个训练文本，它只考虑每种词汇在该训练文本中出现的频率。
- CountVectorizer会将文本中的词语转换为词频矩阵，它通过fit函数计算各个词语出现的次数。
- CountVectorizer旨在通过计数来将一个文档转换为向量。当不存在先验字典时，Countvectorizer作为Estimator提取词汇进行训练，
并生成一个CountVectorizerModel用于存储相应的词汇向量空间。该模型产生文档关于词语的稀疏表示。
在CountVectorizerModel的训练过程中，CountVectorizer将根据语料库中的词频排序从高到低进行选择，词汇表的最大含量由vocabsize超参数来指定，
超参数minDF则指定词汇表中的词语至少要在多少个不同文档中出现。
模型训练，推理使用了spark mlib:
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/spark_mlib.png"  width = "400"/>
</div> 

- [算法详细介绍](http://spark.apache.org/docs/latest/ml-features.html#countvectorizer)


#### 向量引擎索引策略
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/milvus.png"  width = "600"/>
</div> 


#### 1. 前端部署

#### 1.1 安装运行：
```bash
# 安装依赖包
npm install
# 运行
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
            root   /Users/calvin/Documents/dna_sequence_search/dist/;
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

#### 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+ (建议1.8, 11)

- application.yml       
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
  linux:
    path: /home/aias/file/
  windows:
    path: file:/D:/aias/file/
  # 文件大小 /M
  maxSize: 3000
    ...
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar dna-sequence-search-0.1.0.jar

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

#### 4. 打开浏览器
- 输入地址： http://localhost:8090

- 上传数据文件
1). 点击上传按钮上传文件.  
[测试数据](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/human_data.txt)
2). 点击特征提取按钮. 
等待文件解析，模型训练，特征提取，特征存入向量引擎。通过console可以看到进度信息。
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/storage.png"  width = "600"/>
</div> 

- DNA序列搜索
输入文字，点击查询，可以看到返回的清单，根据相似度排序。
```text
ATGCCCCAACTAAATACTACCGTATGGCCCACCATAATTACCCCCATACTCCTTACACTATTCCTCATCACCCAACTAAAAATATTAAACACAAACTACCACCTACCTCCCTCACCAAAGCCCATAAAAATAAAAAATTATAACAAACCCTGAGAACCAAAATGAACGAAAATCTGTTCGCTTCATTCATTGCCCCCACAATCCTAG
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/search.png"  width = "600"/>
</div> 

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/dna_sequence_search/swagger.png"  width = "600"/>
</div> 

- 初始化向量引擎(清空数据): 

```bash
me.aias.tools.MilvusInit.java 
```

- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   