# 词向量SDK【英文】
词向量/词嵌入（Word embedding）是自然语言处理（NLP）中语言模型与表征学习技术的统称。
概念上而言，它是指把一个维数为所有词的数量的高维空间嵌入到一个维数低得多的连续向量空间中，
每个单词或词组被映射为实数域上的向量。


- 词向量
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器


### SDK包含两个模型：
-  w2v_wiki_dim300 (WordEncoderExample1)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为352219，
   训练采用的语料是——Wikipedia_zh 中文维基百科。
  
-  w2v_weibo_dim300 (WordEncoderExample2)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为195204，
   训练采用的语料是——Weibo微博。

### SDK功能：
- 词向量提取
- 相似度计算:
-   余弦相似度
-   内积
 
## 运行例子 - WordEncoderExample1
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [0.365368, 0.506662, ..., -0.157893, 0.346256]
[INFO ] - 美国-特征值: [0.365368, 0.506662, ..., -0.157893, 0.346256]

[INFO ] - 余弦相似度: 0.41243544
[INFO ] - 内积: 11.631776
```
## 运行例子 - WordEncoderExample2
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [-0.186542, 0.153161, ..., -0.344588, 0.269266]
[INFO ] - 美国-特征值: [-0.186542, 0.153161, ..., -0.344588, 0.269266]

[INFO ] - 余弦相似度: 0.30708003
[INFO ] - 内积: 6.5972724
```

### 帮助 
-  添加依赖库：lib/aias-word-encoder-cn-lib-0.1.0.jar
-  下载wiki模型特征数据，添加到 src/test/resources/ 路径下：
[wiki](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/w2v_wiki_dim300.npy) 

-  下载weibo模型特征数据，添加到 src/test/resources/ 路径下：
[weibo](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/w2v_weibo_dim300.npy) 