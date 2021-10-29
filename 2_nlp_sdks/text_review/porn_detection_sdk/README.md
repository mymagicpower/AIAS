# 文本 - 文本审核SDK [中文]
色情检测模型可自动判别文本是否涉黄并给出相应的置信度，对文本中的色情描述、低俗交友、污秽文爱进行识别。

应用场景举例：
适用于视频直播弹幕、社区论坛留言等，对用户评论信息进行检测，一旦发现用户提交违规内容，进行自动审核与实时过滤，保证产品良好用户体验。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/text_review.jpeg)


### SDK算法：
采用LSTM网络结构并按字粒度进行切词，具有较高的分类精度。该模型最大句子长度为256字。

## 运行例子 - ReviewExample
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

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   