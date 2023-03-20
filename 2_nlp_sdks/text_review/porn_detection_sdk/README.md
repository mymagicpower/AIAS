### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1xwIg2np6_N_dt_Lxj5eAVQ?pwd=8juy

### 文本 - 文本审核SDK [中文]
色情检测模型可自动判别文本是否涉黄并给出相应的置信度，对文本中的色情描述、低俗交友、污秽文爱进行识别。

应用场景举例：
适用于视频直播弹幕、社区论坛留言等，对用户评论信息进行检测，一旦发现用户提交违规内容，进行自动审核与实时过滤，保证产品良好用户体验。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/text_review.jpeg)


### SDK算法：
采用LSTM网络结构并按字粒度进行切词，具有较高的分类精度。该模型最大句子长度为256字。

#### 运行例子 - ReviewExample
运行成功后，命令行应该看到下面的信息:
```text
...

[INFO ] - 测试1: 黄色内容
[INFO ] - [0.03643743, 0.96356255]
[INFO ] - not_porn : 0.03643743
[INFO ] - porn : 0.96356255

[INFO ] - 测试2: 黄牛党
[INFO ] - [0.9998598, 1.4011623E-4]
[INFO ] - not_porn : 0.9998598
[INFO ] - porn : 1.4011623E-4
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