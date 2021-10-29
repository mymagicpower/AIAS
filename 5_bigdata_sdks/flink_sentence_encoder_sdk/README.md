## flink-句向量提取【支持15种语言】SDK
句向量是指将语句映射至固定维度的实数向量。
将不定长的句子用定长的向量表示，为NLP下游任务提供服务。
支持 15 种语言： 
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.
 
- 句向量    
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器

### SDK功能：
-  flink句向量提取

### 环境准备
flink连接服务器端口，并从端口读取数据。我们使用最轻量的netcat来测试。
NC（netcat）被称为网络工具中的瑞士军刀，体积小巧，但功能强大。
#### 1. Linux/Mac
```
nc -l 9000
```
#### 2. Windows
```
nc -l -p 9000
```

### 运行例子 - SentenceEncoderExample
#### 在nc命令行输入语句
```bash
...
CalvindeMacBook-Pro:~ calvin$ nc -l 9000
How many people live in Berlin?
这家餐厅很好吃
```
#### 在IDE命令行可以看到对应的语句特征向量
```bash
[-0.025924467, -0.0054853377, 0.035019025, ..., -0.02703922, -0.024842339]
[-0.0035688172, -0.017706484, 0.050606336, ..., 0.0061081746, -0.023076165]
```

#### Mac环境安装netcat 
```bash
brew install nc
```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   