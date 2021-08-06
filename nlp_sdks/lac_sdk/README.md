# 文本 - 词法分析SDK [中文]
词法分析模型能整体性地完成中文分词、词性标注、专名识别任务。

### SDK算法：
模型网络结构：
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/nlp_sdks/lac_network.png)
论文：
https://arxiv.org/abs/1807.01882

## 运行例子 - LacExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input Sentence: 今天是个好日子
[INFO ] - Words : [今天, 是, 个, 好日子]
[INFO ] - Tags : [TIME, v, q, n]
```

### 帮助 
添加依赖库：lib/aias-lac-lib-0.1.0.jar