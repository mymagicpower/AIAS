## kafka-句向量提取【支持15种语言】SDK
句向量是指将语句映射至固定维度的实数向量。
将不定长的句子用定长的向量表示，为NLP下游任务提供服务。
支持 15 种语言： 
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.
 
- 句向量    
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器

### SDK功能：
-  句向量提取
-  读取kafka topic

#### 1. 启动 zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. 启动 kafka:

`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. 创建 topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic sentence-data`

#### 4. 查看创建的topic

`kafka-topics --list --zookeeper localhost:2181`

#### 5. 输入测试数据
```bash
kafka-console-producer --broker-list localhost:9092 --topic sentence-data
> 这家餐厅很好吃
```

#### 6. 运行例子 - SentenceEncoderExample
运行成功后，命令行应该看到下面的信息:
```bash
...
content: How many people live in Berlin?
Vector dimensions: 512
Sentence1 embeddings: [-0.025924467, -0.0054853377, ..., -0.02703922, -0.024842339]

content: 这家餐厅很好吃
Vector dimensions: 512
Sentence1 embeddings: [-0.0035688172, -0.017706484, ..., 0.0061081746, -0.023076165]
...
```

#### Mac环境安装kafka 
```bash
brew install kafka
```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   