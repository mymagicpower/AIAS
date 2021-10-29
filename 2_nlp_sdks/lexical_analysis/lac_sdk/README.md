# 文本 - 词法分析SDK [中文]
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

## 运行例子 - LacExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input Sentence: 今天是个好日子
[INFO ] - Words : [今天, 是, 个, 好日子]
[INFO ] - Tags : [TIME, v, q, n]
```


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   