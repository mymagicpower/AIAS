# 文本 - 短文本相似度SDK [中文]
计算两个句子的cosin相似度:
可以根据用户输入的两个文本，计算出相似度得分。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

### SDK算法：
该模型基于SimNet，是一个计算句子相似度的模型。

## 运行例子 - SemanticExample
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
### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   