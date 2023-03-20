### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接1: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-IMAGE.zip
- 链接2: https://github.com/mymagicpower/AIAS/releases/download/apps/M-BERT-Base-ViT-B.zip

### 图像&文本的跨模态相似性比对检索 SDK【支持40种语言】

#### 背景介绍
OpenAI 发布了两个新的神经网络：CLIP 和 DALL·E。它们将 NLP（自然语言识别）与 图像识别结合在一起，      
对日常生活中的图像和语言有了更好的理解。      
之前都是用文字搜文字，图片搜图片，现在通过CLIP这个模型，可是实现文字搜图片，图片搜文字。      
其实现思路就是将图片跟文本映射到同一个向量空间。如此，就可以实现图片跟文本的跨模态相似性比对检索。      
- 特征向量空间（由图片 & 文本组成）  
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

#### CLIP，“另类”的图像识别
目前，大多数模型学习从标注好的数据集的带标签的示例中识别图像，而 CLIP 则是学习从互联网获取的图像及其描述, 
即通过一段描述而不是“猫”、“狗”这样的单词标签来认识图像。
为了做到这一点，CLIP 学习将大量的对象与它们的名字和描述联系起来，并由此可以识别训练集以外的对象。
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip.png)
如上图所示，CLIP网络工作流程： 预训练图编码器和文本编码器，以预测数据集中哪些图像与哪些文本配对。
然后，将CLIP转换为zero-shot分类器。此外，将数据集的所有分类转换为诸如“一只狗的照片”之类的标签，并预测最佳配对的图像。

CLIP模型地址：
https://github.com/openai/CLIP/blob/main/README.md

### SDK功能：
-  图像&文本特征向量提取
-  相似度计算
-  softmax计算置信度

#### 支持的语言列表：
* Albanian
* Amharic
* Arabic
* Azerbaijani
* Bengali
* Bulgarian
* Catalan
* Chinese (Simplified)
* Chinese (Traditional)
* Dutch
* English
* Estonian
* Farsi
* French
* Georgian
* German
* Greek
* Hindi
* Hungarian
* Icelandic
* Indonesian
* Italian
* Japanese
* Kazakh
* Korean
* Latvian
* Macedonian
* Malay
* Pashto
* Polish
* Romanian
* Russian
* Slovenian
* Spanish
* Swedish
* Tagalog
* Thai
* Turkish
* Urdu
* Vietnamese

#### 运行例子 - ImageTextSearchExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试文本：
[INFO ] - texts: [在雪地里有两条狗, 一只猫在桌子上, 夜晚的伦敦]


# 测试图片：
[INFO ] - image: src/test/resources/two_dogs_in_snow.jpg
```
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/two_dogs_in_snow.jpeg)

```text
# 向量维度：
[INFO ] - Vector dimension: 512

# 生成图片向量：
[INFO ] - image embeddings: [0.22221693, 0.16178696, ..., -0.06122274, 0.13340257]

# 生成文本向量 & 计算相似度：
[INFO ] - text [在雪地里有两条狗] embeddings: [0.07365318, -0.011488605, ..., -0.10090914, -0.5918399]
[INFO ] - Similarity: 30.857948%

[INFO ] - text [一只猫在桌子上] embeddings: [0.01640176, 0.02016575, ..., -0.22862512, -0.091851026]
[INFO ] - Similarity: 10.379046%

[INFO ] - text [夜晚的伦敦] embeddings: [-0.19309878, -0.008406041, ..., -0.1816148, 0.12109539]
[INFO ] - Similarity: 14.382527%


#softmax 置信度计算：
[INFO ] - texts: [在雪地里有两条狗, 一只猫在桌子上, 夜晚的伦敦]
[INFO ] - Label probs: [0.9999999, 1.2768101E-9, 6.995442E-8]

# "在雪地里有两条狗" 与图片相似的置信度为：0.9999999 
```

### 开源算法
#### 1. sdk使用的开源算法
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [预训练模型](https://www.sbert.net/docs/pretrained_models.html#image-text-models)
- [安装](https://www.sbert.net/docs/installation.html)
- [说明](https://www.sbert.net/examples/applications/image-search/README.html)
  

#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- 导出CPU模型（pytorch 模型特殊，CPU&GPU模型不通用。所以CPU，GPU需要分别导出）
- device='cpu'
- device='gpu'
- export_image_search.py
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