# 语义搜索SDK【英文】
语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本。
模型基于MS MARCO数据集训练，可以用于语义搜索，如：关键字 / 搜索短语 / 问题，模型可以找到跟查询query相关的passages。

MS MARCO是微软发布了的一套问答组成的数据集，人工智能领域的研究人员可用它来构建能够与真人相媲美的问答系统。
这套数据集全称：Microsoft MAchine Reading COmprehension，意为“微软机器阅读理解”。
MS MARCO是目前同类型中最有用的数据集，因为它建立在经过匿名处理的真实世界数据(Bing搜索引擎的搜索查询数据)基础之上。


- 语义搜索   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/semantic_search.jpeg)


### SDK功能：
-  短文本向量提取（平均60 words，max_seq_length 128）
-  相似度计算

## 运行例子 - QuestionAnswerRetrievalExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试QA语句：
[INFO ] - Query sentence: How big is London
[INFO ] - Passage sentence: London has 9,787,426 inhabitants at the 2011 census

# 向量维度：
[INFO ] - Vector dimension: 768

# 生成向量：
[INFO ] - Query sentence embeddings: [-0.07430013, -0.5672244, ..., -0.44672608, -0.029431352]
[INFO ] - Passage embeddings: [-0.097875535, -0.3074494, ..., 0.07692761, 0.17015335]

#计算相似度：
[INFO ] - Similarity: 0.5283209
```
### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)


### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   