# 轻量句向量SDK【英文】
句向量是指将语句映射至固定维度的实数向量。
将不定长的句子用定长的向量表示，为NLP下游任务提供服务。

- 句向量
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器

### SDK功能：
-  句向量提取
-  相似度计算

## 运行例子 - SentenceEncoderExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试语句：
[INFO ] - input Sentence1: This model generates embeddings for input sentence
[INFO ] - input Sentence2: This model generates embeddings

# 向量维度：
[INFO ] - Vector dimensions: 384

# 生成向量：
[INFO ] - Sentence1 embeddings: [-0.14147712, -0.025930656, -0.18829542,..., -0.11860573, -0.13064586]
[INFO ] - Sentence2 embeddings: [-0.43392915, -0.23374224, -0.12924, ..., 0.0916177, 0.080070406]

#计算相似度：
[INFO ] - Similarity: 0.7306041

```


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
