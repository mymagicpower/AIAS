### 官网：
[官网链接](https://www.aias.top/)

#### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1aUOhMUQpvNb5VvcnIhLlVQ?pwd=2hqi

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
#### 版面分析 - ocr_layout_sdk
可以用于配合文字识别，表格识别的流水线处理使用。
##### 1. 中文版面分析 - LayoutCNDetExample
中文版面分析模型，可以划分为表格、图片、图片标题、表格、表格标题、页眉、脚本、引用、公式10类区域：
- text
- title
- figure
- figure_caption
- table
- table_caption
- header
- footer
- reference
- equation
![LayoutCNDetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/LayoutCNDetExample.jpeg)

##### 2. 英文版面分析 - LayoutENDetExample
英文版面分析模型，可以划分文字、标题、表格、图片以及列表5类区域：
- text
- title
- list
- table
- figure
- 运行成功后，命令行应该看到下面的信息:

![LayoutENDetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/LayoutENDetExample.jpeg)


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