
### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1u6zGdB7GW83H9Kg64OB_Sw?pwd=c73c

### kafka-情感倾向分析【英文】SDK
情感倾向分析（Sentiment Classification）
针对带有主观描述的文本，可自动判断该文本的情感极性类别并给出相应的置信度，
能够帮助企业理解用户消费习惯、分析热点话题和危机舆情监控，为企业提供有利的决策支持。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)


### SDK算法：
-  情感倾向分为两类
-  Negative (消极)
-  Positive (积极)

#### 1. 启动 zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. 启动 kafka:

`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. 创建 topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic twitter-data`

#### 4. 下载并导入测试数据（src/test/resources目录下, twitter data.txt文件）
[点击下载测试数据](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/data.txt)

`kafka-console-producer --broker-list localhost:9092 --topic twitter-data < data.txt`

#### 5. 运行例子 - SentimentAnalysisExample
运行成功后，命令行应该看到下面的信息:
```bash
...
content: is alone downstairs...working 
prediction: [
	class: "Negative", probability: 0.98781
	class: "Positive", probability: 0.01218
]
content: I feel bad for doing it 
prediction: [
	class: "Negative", probability: 0.99725
	class: "Positive", probability: 0.00274
]
content: @RyanSeacrest is it just me, or she hates anoop. i mean seriously, she's kinda mean to him. 
prediction: [
	class: "Negative", probability: 0.99816
	class: "Positive", probability: 0.00183
]
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