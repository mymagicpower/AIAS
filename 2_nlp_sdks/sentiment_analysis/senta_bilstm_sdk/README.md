### 官网：
[官网链接](http://www.aias.top/)



### 文本 - 情感分析SDK [中文]
情感倾向分析（Sentiment Classification，简称Senta）针对带有主观描述的中文文本，
可自动判断该文本的情感极性类别并给出相应的置信度，能够帮助企业理解用户消费习惯、
分析热点话题和危机舆情监控，为企业提供有利的决策支持。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK算法：
SDK包含两个模型：
- SentaLstm - 该模型（约650M）基于一个LSTM结构，情感类型分为：
- 消极（negative）
- 积极（positive）

- SentaBilstm - 该模型（约650M）基于一个双向LSTM结构，情感类型分为：
- 消极（negative）
- 积极（positive）

#### 运行例子 - SentaLstmExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 输入句子: 这家餐厅很好吃
#中文分词
[INFO ] - Words : [这家, 餐厅, 很好吃]

#词性标注
[INFO ] - Tags : [r, n, a]

#情感分析概率
[INFO ] - [0.07149485, 0.9285052]

[INFO ] - negative : 0.07149485
[INFO ] - positive : 0.9285052

```

#### 运行例子 - SentaBilstmExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 输入句子: 这家餐厅很好吃
#中文分词
[INFO ] - Words : [这家, 餐厅, 很好吃]

#词性标注
[INFO ] - Tags : [r, n, a]

#情感分析概率
[INFO ] - [0.059312407, 0.9406876]

[INFO ] - negative : 0.059312407
[INFO ] - positive : 0.9406876

```

### 开源算法
#### 1. sdk使用的开源算法
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
#### 2. 模型如何导出 ?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)


### 其它帮助信息
http://aias.top/guides.html


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   