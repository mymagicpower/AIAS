## DNA工具包
脱氧核糖核酸（英文DeoxyriboNucleic Acid，缩写为DNA）是生物细胞内含有的四种生物大分子之一核酸的一种。
DNA携带有合成RNA和蛋白质所必需的遗传信息，是生物体发育和正常运作必不可少的生物大分子。
DNA序列指使用一串字母（A、T、C、G）表示的真实的或者假设的携带基因信息的DNA分子的一级结构。
DNA序列测定方法有光学测序和芯片测序两种。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/dna.jpeg)

### SDK功能
- 特征提取
文本(DNA序列)特征提取：将文本数据转化成特征向量的过程，比较常用的文本特征表示法为词袋法。
词袋法：不考虑词语出现的顺序，每个出现过的词汇单独作为一列特征，这些不重复的特征词汇集合为词表。
- CountVectorizer是属于常见的特征数值计算类，是一个文本特征提取方法。对于每一个训练文本，它只考虑每种词汇在该训练文本中出现的频率。
- CountVectorizer会将文本中的词语转换为词频矩阵，它通过fit函数计算各个词语出现的次数。
- CountVectorizer旨在通过计数来将一个文档转换为向量。当不存在先验字典时，Countvectorizer作为Estimator提取词汇进行训练，
并生成一个CountVectorizerModel用于存储相应的词汇向量空间。该模型产生文档关于词语的稀疏表示。
在CountVectorizerModel的训练过程中，CountVectorizer将根据语料库中的词频排序从高到低进行选择，词汇表的最大含量由vocabsize超参数来指定，
超参数minDF则指定词汇表中的词语至少要在多少个不同文档中出现。

Todo list：
- 向量归一化
- 向量相似度计算
- ......

### 运行例子 - DNASequennceExample
运行成功后，命令行应该看到下面的信息:
```text
# 显示前5条数据
+-----+--------------------+
|label|            sequence|
+-----+--------------------+
|    4|[ATGC, TGCC, GCCC...|
|    4|[ATGA, TGAA, GAAC...|
|    3|[ATGT, TGTG, GTGT...|
|    3|[ATGT, TGTG, GTGT...|
|    3|[ATGC, TGCA, GCAA...|
+-----+--------------------+

# 特征向量

+-----+--------------------+--------------------+
|label|            sequence|            features|
+-----+--------------------+--------------------+
|    4|[ATGC, TGCC, GCCC...|(336,[0,8,14,17,1...|
|    4|[ATGA, TGAA, GAAC...|(336,[0,1,2,3,5,7...|
|    3|[ATGT, TGTG, GTGT...|(336,[0,1,2,3,4,5...|
|    3|[ATGT, TGTG, GTGT...|(336,[0,1,2,3,4,5...|
|    3|[ATGC, TGCA, GCAA...|(336,[0,1,2,3,4,5...|
+-----+--------------------+--------------------+

```

### 参考资料： 
http://spark.apache.org/docs/latest/ml-features.html#countvectorizer

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   