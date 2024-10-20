### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1Hg4eF1IntXlkOVCy1SEsyg?pwd=3rs3

### 句向量SDK【支持中文】
是用CoSENT方法训练，基于sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2
用人工挑选后的多语言STS数据集训练得到， 并在中英文测试集评估相对于原模型效果有提升。
- 说明：
- s2s, 即 sentence to sentence ，代表了同质文本之间的嵌入能力，适用任务：文本相似度，重复问题检测，文本分类等
- s2p, 即 sentence to passage ，代表了异质文本之间的嵌入能力，适用任务：文本检索，GPT 记忆模块等


### 支持的语言 50+ languages：
: ar, bg, ca, cs, da, de, el, en, es, et, fa, fi, fr, fr-ca, gl, gu, he, hi, 
hr, hu, hy, id, it, ja, ka, ko, ku, lt, lv, mk, mn, mr, ms, my, nb, nl, pl, pt, pt-br, 
ro, ru, sk, sl, sq, sr, sv, th, tr, uk, ur, vi, zh-cn, zh-tw.

- 句向量    
  ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


### SDK功能：
- 句向量提取
- 相似度（余弦）计算
- 
### 句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器
  


#### 运行例子 - SentenceEncoderExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试语句：
[INFO ] - input Sentence1: 如何更换花呗绑定银行卡
[INFO ] - input Sentence2: 花呗更改绑定银行卡

# 向量维度：
[INFO ] - Vector dimensions: 384

# 中文 - 生成向量：
[INFO ] - Sentence1 embeddings: [0.07825765, 0.39537004, 0.0050692777, ..., 0.08910287, 0.1219089]
[INFO ] - Sentence2 embeddings: [0.07381156, 0.37328288, 0.026979268,  ..., 0.040807277, 0.10056336]

#计算中文相似度：
[INFO ] - Chinese Similarity: 0.9376931

```

### 开源算法
#### 1. sdk使用的开源算法
- [text2vec-base-multilingual](https://huggingface.co/shibing624/text2vec-base-multilingual)




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

