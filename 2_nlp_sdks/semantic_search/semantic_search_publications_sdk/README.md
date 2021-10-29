# 学术论文语义搜索 SDK【英文】
学术论文搜索模型提供了学术论文的特征提取与相似性比对能力。
传入的参数为文章的[标题,摘要]([title, abstract])组成。
subword级切词，最大长度 max_sequence_length: 256（按经验上限平均130个单词左右）。

使用的模型：
https://github.com/allenai/specter/blob/master/README.md
该模型是symmetric search，向量空间由title & abstract组成。

- 特征向量提取  
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

- 特征向量空间（由title & abstract组成）   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/semantic_search.jpeg)


### SDK功能：
-  paper [title, abstract]特征向量提取
-  相似度计算

## 运行例子 - SemanticSearchPublicationsExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试语句：
[INFO ] - paper1 [title, abstract]: [BERT, We introduce a new language representation model called BERT]
[INFO ] - paper2 [title, abstract]: [Attention is all you need, The dominant sequence transduction models are based on complex recurrent or convolutional neural networks]

# 向量维度：
[INFO ] - Vector dimension: 768

# 生成向量：
[INFO ] - paper1[title, text] embeddings: [-0.83961445, 1.1465806, ..., 0.5574437, 0.4750324]
[INFO ] - paper2[title, text] embeddings: [-0.23870255, 1.2555068, ..., 0.052179076, 0.47623542]

#计算相似度：
[INFO ] - Similarity: 0.82421297

```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   