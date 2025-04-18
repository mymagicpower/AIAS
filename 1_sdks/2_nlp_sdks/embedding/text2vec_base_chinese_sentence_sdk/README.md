### 官网：
[官网链接](http://www.aias.top/)

### 下载模型
- 查看最新下载链接请查看 1_sdks/README.md

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 句向量SDK【支持中文】
用CoSENT方法训练，基于nghuyong/ernie-3.0-base-zh，用人工挑选后的中文STS数据集：
shibing624/nli-zh-all/text2vec-base-chinese-sentence-dataset训练得到。
并在中文各NLI测试集评估达到较好效果，中文s2s(句子vs句子)语义匹配任务推荐使用.
- s2s, 即 sentence to sentence ，代表了同质文本之间的嵌入能力，适用任务：文本相似度，重复问题检测，文本分类等

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
[INFO ] - Sentence1 embeddings: [0.36803457, -0.32426378, ..., -0.89762807, -0.37553307]
[INFO ] - Sentence2 embeddings: [0.33208293, -0.38463414,  ..., -0.7069321, -0.09533284]

#计算中文相似度：
[INFO ] - Chinese Similarity: 0.9704481

```

### 开源算法
#### 1. sdk使用的开源算法
- [text2vec-base-chinese-sentence](https://huggingface.co/shibing624/text2vec-base-chinese-sentence)



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

