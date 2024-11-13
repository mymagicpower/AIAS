### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1hW3HZ9iIuz20FXtrZBAoKg?pwd=96ij

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 句向量SDK【支持中文】
用CoSENT方法训练，基于nghuyong/ernie-3.0-base-zh用人工挑选后的中文STS数据集，加入了s2p(sentence to paraphrase)数据，
强化了其长文本的表征能力，并在中文各NLI测试集评估达到SOTA，中文s2p(句子vs段落)语义匹配任务推荐使用。
- 说明：
- s2p, 即 sentence to passage ，代表了异质文本之间的嵌入能力，适用任务：文本检索，GPT 记忆模块等

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
  


#### 运行例子 - Text2VecExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试语句：
[INFO ] - input Sentence1: 如何更换花呗绑定银行卡
[INFO ] - input Sentence2: 花呗更改绑定银行卡

# 向量维度：
[INFO ] - Vector dimensions: 768

# 中文 - 生成向量：
[INFO ] - Sentence1 embeddings: [0.7316301, 0.16452518, ..., -0.6056768, -0.22449276]
[INFO ] - Sentence2 embeddings: [0.58031255, 0.04431232,  ..., -0.40282968, -0.07049167]

#计算中文相似度：
[INFO ] - Chinese Similarity: 0.93226075

```

### 开源算法
#### 1. sdk使用的开源算法
- [text2vec-base-chinese-paraphrase](https://huggingface.co/shibing624/text2vec-base-chinese-paraphrase)



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

