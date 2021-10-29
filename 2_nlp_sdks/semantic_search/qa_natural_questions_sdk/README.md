# 自然问题问答 SDK【英文】
模型基于Google的Natural Questions dataset（100k Google search查询数据，
及源自Wikipedia的相关passages）训练。
谷歌发布的大规模训练和评估开放领域问答系统的语料库Natural Questions(NQ),旨在推动人们开发出更有效,更强大的问答系统。
而在此前,一直没有大量公开的可用于训练和评估问答模型的自然生成问题(如人们寻求信息时提出的问题)及答案。
NQ是一个大规模训练和评估开放领域问题回答系统的语料库,它第一个复制了人们找到问题答案的端到端流程。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/qa_natural_questions.jpeg)


### SDK功能：
-  query / passage[title, text]向量提取
-  相似度计算

## 运行例子 - QANaturalQuestionsExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试语句：
[INFO ] - query: How many people live in London?
# passage 是<title，text>构成的一对数据。
[INFO ] - passage [title, text]: [London, London has 9,787,426 inhabitants at the 2011 census.]

# 向量维度：
[INFO ] - Vector dimension: 768

# 生成向量：
[INFO ] - query embeddings: [0.04629234, -0.33281654, ..., -0.22015738, -0.06693681]
[INFO ] - passage[title, text] embeddings: [-0.015913313, -0.10886402, ..., 0.48449898, -0.32266212]

#计算相似度：
[INFO ] - Similarity: 0.650292

```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)


### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   