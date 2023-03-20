### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://github.com/mymagicpower/AIAS/releases/download/apps/msmarco-distilbert-base-v4.zip

### 语义搜索SDK【英文】
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

#### 运行例子 - QuestionAnswerRetrievalExample
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

### 开源算法
#### 1. sdk使用的开源算法
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [预训练模型](https://www.sbert.net/docs/pretrained_models.html)
- [安装](https://www.sbert.net/docs/installation.html)


#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- 导出CPU模型（pytorch 模型特殊，CPU&GPU模型不通用。所以CPU，GPU需要分别导出）
- device='cpu'
- device='gpu'
- export_model.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('msmarco-distilbert-base-v4', device='cpu')
model.eval()
batch_size=1
max_seq_length=128
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
# input_features = (input_ids, input_type_ids, input_mask)
input_features = {'input_ids': input_ids,'attention_mask': input_mask}

# traced_model = torch.jit.trace(model, example_inputs=input_features)
traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("models/msmarco-distilbert-base-v4.pt")
```

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