### 官网：
[官网链接](https://www.aias.top/)

#### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1GSdLe3jxPzzn-5GzUfze9A?pwd=2g6f


### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:
#### 文字识别SDK (原生支持旋转倾斜文本, 如果需要，图像预处理SDK可以作为辅助)
##### 1. 文本检测 - OcrV3DetExample
- 中文文本检测
- 英文文本检测
- 多语言文本检测
![OcrV3DetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3DetExample.jpeg)

##### 2. 文本识别 - OcrV3RecExample
支持的语言模型：
- 中文简体
- 中文繁体
- 英文
- 韩语
- 日语
- 阿拉伯
- 梵文
- 泰米尔语
- 泰卢固语
- 卡纳达文
- 斯拉夫

![OcrV3RecExample1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3RecExample1.jpeg)

![OcrV3RecExample2](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3RecExample2.jpeg)


##### 3. 多线程文本识别 - OcrV3MultiThreadRecExample
CPU：2.3 GHz 四核 Intel Core i5
同样图片单线程运行时间：1172 ms
多线程运行时间：707 ms
图片检测框较多时，多线程可以显著提升识别速度。


### 开源算法
#### 1. sdk使用的开源算法
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)

#### 2. 模型如何导出 ?
(readme.md 里提供了推理模型的下载链接)
- [export_model](https://github.com/PaddlePaddle/PaddleOCR/blob/release%2F2.5/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)



#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html

