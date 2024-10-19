## 目录：
http://aias.top/


### 图像&文本的跨模态相似性比对检索【支持40种语言】
- 包含两个项目，满足不同场景的需要

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/arc.png)

### 1. 图像&文本的跨模态相似性比对检索【无向量引擎版】 - simple_image_text_search
#### 1.1 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作

#### 1.2 功能介绍
- 以图搜图：上传图片搜索
- 以文搜图：输入文本搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


### 2. 图像&文本的跨模态相似性比对检索【向量引擎版】 - image_text_search
#### 2.1 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

#### 2.2 功能介绍
- 以图搜图：上传图片搜索
- 以文搜图：输入文本搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


### 3. 打开浏览器
- 输入地址： http://localhost:8090

#### 3.1 图片上传
1). 点击上传按钮上传文件.  
[测试图片数据](https://pan.baidu.com/s/1QtF6syNUKS5qkf4OKAcuLA?pwd=wfd8)
2). 点击特征提取按钮. 
等待图片特征提取，特征存入向量引擎。通过console可以看到进度信息。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/storage.png)

#### 3.2 跨模态搜索 - 文本搜图
  输入文字描述，点击查询，可以看到返回的图片清单，根据相似度排序。

- 例子1，输入文本：车
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search1.png)

- 例子2，输入文本：雪地上两只狗
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search2.png)

#### 3.3 跨模态搜索 - 以图搜图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png)
