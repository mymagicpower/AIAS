### 官网：
[官网链接](https://www.aias.top/)

#### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1kNlsZBvfZtX-VsdXLFI_8A?pwd=w0fo

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
#### led 文字识别SDK 
##### 1. 文本检测 - OcrV3DetExample

![OcrV3DetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/led_det_result.png)

##### 2. 文本识别 - OcrV3RecExample


![OcrV3RecExample1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/led_rec_result.png)





### 开源算法
#### 1. sdk使用的开源算法
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)
- https://github.com/PaddlePaddle/PaddleOCR/blob/release%2F2.6/applications/%E6%B6%B2%E6%99%B6%E5%B1%8F%E8%AF%BB%E6%95%B0%E8%AF%86%E5%88%AB.md

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

