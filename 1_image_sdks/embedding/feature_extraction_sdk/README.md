### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1EjrCjBOxyZc2aAptOgi8BQ?pwd=6aaw

### 特征提取(512维)SDK
提取图片512维特征值，并支持图片1:1特征比对，给出置信度。

### SDK功能：
#### 1. 特征提取
使用imagenet预训练模型resnet50，提取图片512维特征。

- 运行例子 - FeatureExtractionExample
测试图片
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/feature_extraction_sdk/car1.png)

- 运行成功后，命令行应该看到下面的信息:
```text
...
512
[INFO ] - [..., 0.18182503, 0.13296463, 0.22447465, 0.07165501..., 0.16957843]

```

#### 2. 图片1:1比对
计算图片相似度。

- 运行例子 - FeatureComparisonExample
测试图片: 左右特征对比
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/feature_extraction_sdk/comparision.png)

- 运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 0.77396494

```


### 开源算法
#### 1. sdk使用的开源算法
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [预训练模型](https://www.sbert.net/docs/pretrained_models.html#image-text-models)
- [安装](https://www.sbert.net/docs/installation.html)


#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- 导出CPU模型（pytorch 模型特殊，CPU&GPU模型不通用。所以CPU，GPU需要分别导出）
```text
from sentence_transformers import SentenceTransformer, util
from PIL import Image
import torch

#Load CLIP model
model = SentenceTransformer('clip-ViT-B-32', device='cpu')

#Encode an image:
# img_emb = model.encode(Image.open('two_dogs_in_snow.jpg'))

#Encode text descriptions
# text_emb = model.encode(['Two dogs in the snow', 'A cat on a table', 'A picture of London at night'])
text_emb = model.encode(['Two dogs in the snow'])
sm = torch.jit.script(model)
sm.save("models/clip-ViT-B-32/clip-ViT-B-32.pt")

#Compute cosine similarities
# cos_scores = util.cos_sim(img_emb, text_emb)
# print(cos_scores)
```

- 导出GPU模型
```text
  from sentence_transformers import SentenceTransformer, util
  from PIL import Image
  import torch

#Load CLIP model
model = SentenceTransformer('clip-ViT-B-32', device='gpu')

#Encode an image:
# img_emb = model.encode(Image.open('two_dogs_in_snow.jpg'))

#Encode text descriptions
# text_emb = model.encode(['Two dogs in the snow', 'A cat on a table', 'A picture of London at night'])
text_emb = model.encode(['Two dogs in the snow'])
sm = torch.jit.script(model)
sm.save("models/clip-ViT-B-32/clip-ViT-B-32.pt")

#Compute cosine similarities
# cos_scores = util.cos_sim(img_emb, text_emb)
# print(cos_scores)
```

### 其它帮助信息
https://aias.top/guides.html


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   



#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html