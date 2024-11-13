### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1jM5vy82-60V9-qmeyHrDZg?pwd=xqiu

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 文本 - 词法分析SDK [中文]
词法分析模型能整体性地完成中文分词、词性标注、专名识别任务。

词性标注：
- n 普通名词
- f 方位名词
- s 处所名词
- t 时间
- nr 人名
- ns 地名
- nt 机构名
- nw 作品名
- nz 其他专名
- v 普通动词
- vd 动副词
- vn 名动词
- a 形容词
- ad 副形词
- an 名形词
- d 副词
- m 数量词
- q 量词
- r 代词
- p 介词
- c 连词
- u 助词
- xc 其他虚词
- w 标点符号
- PER 人名
- LOC 地名
- ORG 机构名
- TIME 时间


### SDK算法：
模型网络结构：   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/lac_network.png)

- 论文：     
https://arxiv.org/abs/1807.01882

#### 运行例子 - LacExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input Sentence: 今天是个好日子
[INFO ] - Words : [今天, 是, 个, 好日子]
[INFO ] - Tags : [TIME, v, q, n]
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