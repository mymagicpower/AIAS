### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1NqyGxUA8-cDOTk8aCmcsuQ?pwd=qsvn

### 自然问题问答 SDK【英文】
模型基于Google的Natural Questions dataset（100k Google search查询数据，
及源自Wikipedia的相关passages）训练。
谷歌发布的大规模训练和评估开放领域问答系统的语料库Natural Questions(NQ),旨在推动人们开发出更有效,更强大的问答系统。
而在此前,一直没有大量公开的可用于训练和评估问答模型的自然生成问题(如人们寻求信息时提出的问题)及答案。
NQ是一个大规模训练和评估开放领域问题回答系统的语料库,它第一个复制了人们找到问题答案的端到端流程。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/qa_natural_questions.jpeg)


### SDK功能：
-  query / passage[title, text]向量提取
-  相似度计算

#### 运行例子 - QANaturalQuestionsExample
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
- export_model_natural_questions.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('nq-distilbert-base-v1', device='cpu')
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
traced_model.save("models/nq-distilbert-base-v1/nq-distilbert-base-v1.pt")
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