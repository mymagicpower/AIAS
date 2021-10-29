# 文本 - 对话情绪识别SDK [中文]
对话情绪识别（Emotion Detection）专注于识别智能对话场景中用户的情绪，
针对智能对话场景中的用户文本，自动判断该文本的情绪类别并给出相应的置信度。


![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg)

### SDK算法：
该模型基于TextCNN（多卷积核CNN模型），能够更好地捕捉句子局部相关性。
模型大小：约120M
情感类型分为：
- 消极（negative）
- 中性（neutral）
- 积极（positive）

## 运行例子 - SentaBilstmExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input Sentence: 今天天气真好
#中文分词
[INFO ] - Words : [今天, 天气, 真好]

#词性标注
[INFO ] - Tags : [TIME, n, a]

#情感分析概率
[INFO ] - [0.0018637113, 0.07143357, 0.9267028]

[INFO ] - negative : 0.0018637113
[INFO ] - neutral : 0.07143357
[INFO ] - positive : 0.9267028

```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   