### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1ky9ljTdGKOYgRPhkRl9kMQ?pwd=yzgx

### 中文翻译为英文SDK
机器翻译，又称为自动翻译，是利用计算机将一种自然语言(源语言)转换为另一种自然语言(目标语言)的过程。
它是计算语言学的一个分支，是人工智能的终极目标之一，具有重要的科学研究价值。
同时，机器翻译又具有重要的实用价值。随着经济全球化及互联网的飞速发展，机器翻译技术在促进政治、经济、文化交流等方面起到越来越重要的作用。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg)

### SDK算法：
2017 年，Google机器翻译团队在其发表的论文Attention Is All You Need中，
提出了用于完成机器翻译（Machine Translation）等序列到序列（Seq2Seq）学习任务的一种全新网络结构——Transformer。
Tranformer网络完全使用注意力（Attention）机制来实现序列到序列的建模，并且取得了很好的效果。
本SDK模型包含6层的transformer结构，头数为8，隐藏层参数为512，参数量为64M。
提供了中文翻译为英文的能力。


#### 运行例子 - TranslationExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 输入句子: 今天天气怎么样？
#中文分词
[INFO ] - Words : [今天, 天气, 怎么样, ？]

#词性标注
[INFO ] - Tags : [TIME, n, r, w]

#翻译结果，按置信度排序：
[INFO ] - T0:  What's the weather like today?
[INFO ] - T1:  How is the weather today?
[INFO ] - T2:  How's the weather today?
[INFO ] - T3:  How was the weather today?
[INFO ] - T4:  What is the weather like today?

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