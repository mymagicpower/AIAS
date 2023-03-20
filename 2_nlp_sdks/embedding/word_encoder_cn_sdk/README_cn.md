### 官网：
[官网链接](http://www.aias.top/)


### 下载相应的模型特征数据
- 链接: https://pan.baidu.com/s/1DOzp8AJ7Rjpf8C8i6alEaA?pwd=y6z3
  添加到 src/test/resources/ 路径下。

### 词向量SDK【中文】
词向量/词嵌入（Word embedding）是自然语言处理（NLP）中语言模型与表征学习技术的统称。
概念上而言，它是指把一个维数为所有词的数量的高维空间嵌入到一个维数低得多的连续向量空间中，
每个单词或词组被映射为实数域上的向量。


- 词向量
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/word_vector.jpeg)

### SDK功能：
- 词向量提取
- 相似度计算:
-   余弦相似度
-   内积

### SDK包含9个模型数据：
#### WordEncoderExample1 （w2v_wiki_dim300 403M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为352219，
   训练采用的语料是——Wikipedia_zh 中文维基百科。

-  运行例子 - WordEncoderExample1
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [0.365368, 0.506662, ..., -0.157893, 0.346256]
[INFO ] - 美国-特征值: [0.365368, 0.506662, ..., -0.157893, 0.346256]

[INFO ] - 余弦相似度: 0.41243544
[INFO ] - 内积: 11.631776
 ```
  
#### WordEncoderExample2 (w2v_weibo_dim300 - 大小约 223M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为195204，
   训练采用的语料是——Weibo微博。
   
-  运行例子 - WordEncoderExample2
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [-0.186542, 0.153161, ..., -0.344588, 0.269266]
[INFO ] - 美国-特征值: [-0.186542, 0.153161, ..., -0.344588, 0.269266]

[INFO ] - 余弦相似度: 0.30708003
[INFO ] - 内积: 6.5972724
```

#### WordEncoderExample3 （w2v_financial_dim300 - 大小约 535M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为467324，
   训练采用的语料是——Financial News 金融新闻。
   
-  运行例子 - WordEncoderExample3
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 投资-特征值: [-0.146902, 0.203111, ..., -0.371138, 0.073174]
[INFO ] - 投机-特征值: [-0.146902, 0.203111, ..., -0.371138, 0.073174]
[INFO ] - 余弦相似度: 0.26770666
[INFO ] - 内积: 5.2186356
```
  
#### WordEncoderExample4 (w2v_sikuquanshu_dim300 - 大小约 22M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为19529，
   训练采用的语料是——Complete Library in Four Sections 四库全书。
   
-  运行例子 - WordEncoderExample4
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 曰-特征值: [0.063528, 0.068379, ..., -0.022315, -0.103614]
[INFO ] - 云-特征值: [0.063528, 0.068379, ..., -0.022315, -0.103614]
[INFO ] - 余弦相似度: 0.3282848
[INFO ] - 内积: 1.2609351
```
   
#### WordEncoderExample5 （w2v_literature_dim300 - 大小约 215M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为187962，
   训练采用的语料是——Literature 文学作品。

-  运行例子 - WordEncoderExample5
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 玄幻-特征值: [-1.036695, -0.648525, ..., -0.323885, 0.069166]
[INFO ] - 科幻-特征值: [-1.036695, -0.648525, ..., -0.323885, 0.069166]
[INFO ] - 余弦相似度: 0.50576097
[INFO ] - 内积: 26.441778
```
  
#### WordEncoderExample6 (w2v_people_daily_dim300 - 大小约 407M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为356055，
   训练采用的语料是——People's Daily News 人民日报。

-  运行例子 - WordEncoderExample6
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 发展-特征值: [0.618088, -0.389146, ..., 0.040372, 0.327205]
[INFO ] - 提升-特征值: [0.618088, -0.389146, ..., 0.040372, 0.327205]
[INFO ] - 余弦相似度: 0.36809018
[INFO ] - 内积: 21.746298
```
   
#### WordEncoderExample7 （w2v_sogou_dim300 - 大小约 418M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为365112，
   训练采用的语料是——Sogou News 搜狗新闻。

-  运行例子 - WordEncoderExample7
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [-0.358933, 0.34152, ..., 0.367553, 0.098403]
[INFO ] - 美国-特征值: [-0.358933, 0.34152, ..., 0.367553, 0.098403]
[INFO ] - 余弦相似度: 0.47674376
[INFO ] - 内积: 29.667158
```
  
#### WordEncoderExample8 (w2v_baidu_encyclopedia_dim300 - 大小约 728M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为635976，
   训练采用的语料是——Baidu Encyclopedia 百度百科。

-  运行例子 - WordEncoderExample8
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [0.46702, -0.137223, ..., -0.059722, -0.271998]
[INFO ] - 美国-特征值: [0.46702, -0.137223, ..., -0.059722, -0.271998]
[INFO ] - 余弦相似度: 0.51087683
[INFO ] - 内积: 24.71891
```
   
#### WordEncoderExample9 （w2v_zhihu_dim300 - 大小约 297M)
   基于W2V训练得到的中文Embedding模型，词向量的纬度为300，词表大小为259871，
   训练采用的语料是——Zhihu_QA 知乎问答。
  
-  运行例子 - WordEncoderExample9
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 中国-特征值: [-0.050675, 0.389359, ..., -0.203935, -0.371196]
[INFO ] - 美国-特征值: [-0.050675, 0.389359, ..., -0.203935, -0.371196]
[INFO ] - 余弦相似度: 0.5643151
[INFO ] - 内积: 9.433272
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