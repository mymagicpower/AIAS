## flink-情感倾向分析【英文】SDK
情感倾向分析（Sentiment Classification）
针对带有主观描述的文本，可自动判断该文本的情感极性类别并给出相应的置信度，
能够帮助企业理解用户消费习惯、分析热点话题和危机舆情监控，为企业提供有利的决策支持。
 
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)


### SDK算法：
-  情感倾向分为两类
-  Negative (消极)
-  Positive (积极)

### 环境准备
flink连接服务器端口，并从端口读取数据。我们使用最轻量的netcat来测试。
NC（netcat）被称为网络工具中的瑞士军刀，体积小巧，但功能强大。
#### 1. Linux/Mac
```
nc -l 9000
```
#### 2. Windows
```
nc -l -p 9000
```

### 运行例子 - SentenceEncoderExample
[点击下载测试数据](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/bigdata_sdks/data.txt)
#### 在nc命令行输入语句
```bash
...
CalvindeMacBook-Pro:~ calvin$ nc -l 9000
is alone downstairs...working
I feel bad for doing it
@RyanSeacrest is it just me, or she hates anoop. i mean seriously, she's kinda mean to him.
```
#### 在IDE命令行可以看到对应的语句情感分类结果
```bash
[
	class: "Negative", probability: 0.98781
	class: "Positive", probability: 0.01218
]
[
	class: "Negative", probability: 0.99725
	class: "Positive", probability: 0.00274
]
[
	class: "Negative", probability: 0.99816
	class: "Positive", probability: 0.00183
]
```

#### 帮助 
添加依赖库：lib/aias-sentiment-analysis-lib-0.1.0.jar

#### Mac环境安装netcat 
```bash
brew install nc
```