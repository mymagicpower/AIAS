## 目录：
http://aias.top/


### 文本向量搜索
本例子提供了文本搜索，支持上传csv文件，使用句向量模型提取特征，用于后续检索。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/arc.png)

#### 文本句向量搜索应用：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器
- RAG 用于大模型搜索增强生成

### 1. 文本向量搜索【无向量引擎版】 - simple_text_search
#### 1.1 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作


### 2. 文本向量搜索【向量引擎版】 - text_search
#### 2.1 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

## 3. 打开浏览器
- 输入地址： http://localhost:8090

- 上传CSV数据文件
1). 点击上传按钮上传CSV文件.  
[测试数据](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/example.csv)
2). 点击特征提取按钮. 
等待CSV文件解析，特征提取，特征存入向量引擎。通过console可以看到进度信息。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/storage.png)

- 文本搜索
  输入文字，点击查询，可以看到返回的清单，根据相似度排序。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png)

## 4. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/swagger.png)
