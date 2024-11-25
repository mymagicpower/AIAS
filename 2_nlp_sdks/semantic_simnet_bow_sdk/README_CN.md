### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1isT3QSEZv2ltEOJax-rBWA?pwd=9eyh

### 文本 - 短文本相似度SDK [中文]
计算两个句子的cosin相似度:
可以根据用户输入的两个文本，计算出相似度得分。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

### SDK算法：
该模型基于SimNet，是一个计算句子相似度的模型。

#### 运行例子 - SemanticExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 句子 1: 这个棋局太难了
#中文分词
[INFO ] - Words : [这个, 棋局, 太难, 了]
#词性标注
[INFO ] - Tags : [r, n, a, xc]

[INFO ] - 句子 2: 这个棋局不简单
#中文分词
[INFO ] - Words : [这个, 棋局, 不, 简单]
#词性标注
[INFO ] - Tags : [r, n, d, a]

[INFO ] - 句子 3: 这个棋局很有意思
#中文分词
[INFO ] - Words : [这个, 棋局, 很, 有意思]
#词性标注
[INFO ] - Tags : [r, n, d, a]

#计算短文本相似度
[INFO ] - 句子 1: 这个棋局太难了
[INFO ] - 句子 2: 这个棋局不简单
[INFO ] - 相似度 : 0.8542996

[INFO ] - 句子 1: 这个棋局太难了
[INFO ] - 句子 3: 这个棋局很有意思
[INFO ] - 相似度 : 0.8260221

```

### 开源算法
#### 1. sdk使用的开源算法
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
#### 2. 模型如何导出 ?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)


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