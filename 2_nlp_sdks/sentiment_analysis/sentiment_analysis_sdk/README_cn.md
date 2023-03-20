### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1KCS7dgQWXxWB2EWFZNV4LA?pwd=pkmv

### 情感倾向分析SDK
情感倾向分析（Sentiment Classification）
针对带有主观描述的中文文本，可自动判断该文本的情感极性类别并给出相应的置信度，
能够帮助企业理解用户消费习惯、分析热点话题和危机舆情监控，为企业提供有利的决策支持。


![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK算法：
-  情感倾向分为两类
-  Negative (消极)
-  Positive (积极)

#### 运行例子 - SentimentAnalysisExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Number of inter-op threads is 4
[INFO ] - Number of intra-op threads is 4
[INFO ] - input Sentence: I like AIAS. AIAS is the best DL application suite!
[INFO ] - [
	class: "Positive", probability: 0.99927
	class: "Negative", probability: 0.00072
]
```

### 开源算法
#### 无

### 其它帮助信息
http://aias.top/guides.html


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