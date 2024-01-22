### 官网：
[官网链接](https://www.aias.top/)

## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:

#### 1. 图像预处理SDK
在OCR文字识别的时候，我们得到的图像一般情况下都不是正的，多少都会有一定的倾斜。 并且图片有可能是透视视角拍摄，需要重新矫正，尤其此时，将图片转正可以提高文字识别的精度。

##### 1.1. 方向检测与旋转 - ocr_direction_det_sdk
##### 方向检测 - DirectionDetExample
模型本身支持 0 度，和 180 度两种方向分类。
但是由于中文的书写习惯，根据宽高比可以判断文本的90度和270度两个方向。
- 0度
- 90度
- 180度
- 270度   
![OcrDirectionExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrDirectionExample.jpeg)

##### 方向旋转 - RotationExample
- 逆时针旋转
- 每次旋转90度的倍数
  ![RotationExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/RotationExample.jpeg)






#### 2. 文字识别SDK 
- ocr_v3_sdk ocr V3版本
- ocr_v4_sdk ocr V4版本

##### 2.1. V3 文本检测 
- 中文文本检测
- 英文文本检测
- 多语言文本检测
![OcrV3DetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3DetExample.jpeg)

##### 2.2. V3 文本识别
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


##### 2.3. 多线程文本识别 - OcrV3MultiThreadRecExample
CPU：2.3 GHz 四核 Intel Core i5
同样图片单线程运行时间：1172 ms
多线程运行时间：707 ms
图片检测框较多时，多线程可以显著提升识别速度。


#### 3. 版面分析 - ocr_layout_sdk
可以用于配合文字识别，表格识别的流水线处理使用。
##### 3.1. 中文版面分析 - LayoutCNDetExample
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

##### 3.2. 英文版面分析 - LayoutENDetExample
英文版面分析模型，可以划分文字、标题、表格、图片以及列表5类区域：
- text
- title
- list
- table
- figure
- 运行成功后，命令行应该看到下面的信息:

![LayoutENDetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/LayoutENDetExample.jpeg)


##### 3.3. 中英文文档 - 表格区域检测 - TableDetExample
表格数据集训练的版面分析模型，支持中英文文档表格区域的检测。
![TableDetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TableDetExample.jpeg)


#### 4. 表格识别 - ocr_table_sdk
##### 4.1. 英文表格识别 - TableENRecExample
![TableENRecExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TableENRecExample.jpeg)


##### 4.2. 中文表格识别 - TableCNRecExample
![TableCNRecExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TableCNRecExample.jpeg)

##### 4.3. 多表格自动检测识别 - MultiTableRecExample
![MultiTableRecExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/MultiTableRecExample.jpeg)


#### 5. led文字识别 - ocr_led_sdk
##### 5.1. 文本检测 - OcrV3DetExample

![OcrV3DetExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/led_det_result.png)

##### 5.2. 文本识别 - OcrV3RecExample


![OcrV3RecExample1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/led_rec_result.png)



### 开源算法
#### 1. PaddleOCR开源大礼包
- 链接：https://pan.baidu.com/s/1MiWrtgnR3Te1BOpAb3maqg?pwd=djbk 


#### 2. sdk使用的开源算法
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)
- https://github.com/PaddlePaddle/PaddleOCR/blob/release%2F2.6/applications/%E6%B6%B2%E6%99%B6%E5%B1%8F%E8%AF%BB%E6%95%B0%E8%AF%86%E5%88%AB.md

#### 3. 模型如何导出 ?
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

