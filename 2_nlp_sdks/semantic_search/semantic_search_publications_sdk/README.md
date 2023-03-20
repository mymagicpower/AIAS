### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://github.com/mymagicpower/AIAS/releases/download/apps/allenai-specter.zip

### 学术论文语义搜索 SDK【英文】
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

#### 运行例子 - SemanticSearchPublicationsExample
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

model = SentenceTransformer('allenai-specter', device='cpu')
model.eval()
batch_size=1
max_seq_length=256
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_features = {'input_ids': input_ids, 'token_type_ids': input_type_ids, 'attention_mask': input_mask}

traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("models/allenai-specter/allenai-specter.pt")
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