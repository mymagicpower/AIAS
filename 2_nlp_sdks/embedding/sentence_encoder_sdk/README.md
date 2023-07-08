# 句向量SDK【英文】
句向量是指将语句映射至固定维度的实数向量。
将不定长的句子用定长的向量表示，为NLP下游任务提供服务。

- 句向量
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器


### SDK包含两个模型：（模型较大，首次运行耐心等待下载）
-  SentenceEncoder 500M
-  SentenceEncoderLarge5 1G

### SDK功能：
-  句向量提取
-  相似度计算

## 运行例子 - SentenceEncoderExample
- 测试语句：
- I am a sentence for which I would like to get its embedding
- I am a sentence
- I am a sentence for which I would like to get ...

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - length: 512

[INFO ] - [0.050808605, -0.016524296, 0.015737822, -0.042864114, ..., 0.017881196]
[INFO ] - [0.018138515, -0.037706323, 0.04290087, -0.019213252, ..., 0.058189757]
[INFO ] - [0.04447042, -0.07614114, 0.0073701036, -0.045335855, ..., 0.054146692]

[INFO ] - 0.80258906
[INFO ] - 0.90100515
```


### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
