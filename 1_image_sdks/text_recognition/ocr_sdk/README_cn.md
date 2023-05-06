### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1AGKdyvVeRONOhAHu-Ot7RA?pwd=3m2f

## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:

#### 1. 方向检测
- OcrDirectionExample
- 0度
- 90度
- 180度
- 270度   
  ![detect_direction](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_direction.png)

#### 2. 图片旋转
- RotationExample

#### 3. 文字识别 (原生支持倾斜文本, 1 & 2 需要时可以作为辅助)
- OcrV3RecognitionExample

#### 4. 图片旋转


### 运行OCR识别例子
#### 1.1 文字识别：
- 例子代码: OcrV3RecognitionExample.java    
- 运行成功后，命令行应该看到下面的信息:
```text
time: 766
time: 2221
烦恼！
无数个
吃饱了就有
烦恼
没有吃饱只有一个
```

- 输出图片效果如下：
![text_with_angle](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/text_with_angle.png)


#### 2. 图片旋转：
每调用一次rotateImg方法，会使图片逆时针旋转90度。
- 例子代码: RotationExample.java 
- 旋转前图片:
![ticket_0](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_0.png)
- 旋转后图片效果如下：
![rotate_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/rotate_result.png)

#### 3 多线程文字识别：
- 例子代码: OcrV3MultiThreadRecExample.java


### 更新说明
1. 表格识别暂时归档至项目 AIAS_Archive  - image_sdks/ocr_sdk
2. 引擎切换为onnx提升性能

### 开源算法
#### sdk使用的开源算法
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)
- [PaddleOCR转ONNX](https://github.com/PaddlePaddle/Paddle2ONNX)

```text
Paddle模型转onnx:
https://github.com/PaddlePaddle/Paddle2ONNX

转换例子：
1. OCRv3 文字检测
paddle2onnx --model_dir /Users/calvin/Downloads/paddle_ocr/ch_PP-OCRv3_det_infer \
            --model_filename inference.pdmodel \
            --params_filename inference.pdiparams \
            --save_file inference.onnx \
            --enable_dev_version True \
            --enable_onnx_checker True

2. OCRv3 文字识别
paddle2onnx --model_dir /Users/calvin/Downloads/paddle_ocr/ch_PP-OCRv3_rec_infer \
            --model_filename inference.pdmodel \
            --params_filename inference.pdiparams \
            --save_file inference.onnx \
            --enable_dev_version True \
            --enable_onnx_checker True

3. OCRv2 文字检测
paddle2onnx --model_dir /Users/calvin/Downloads/paddle_ocr/ch_PP-OCRv2_det_infer \
            --model_filename inference.pdmodel \
            --params_filename inference.pdiparams \
            --save_file inference.onnx \
            --enable_dev_version True \
            --enable_onnx_checker True

4. 方向检测
方向检测这个模型有点问题，需要先修改 Paddle 模型输入 Shape：
https://github.com/PaddlePaddle/Paddle2ONNX/blob/develop/tools/paddle/README.md

1). 修改 Paddle 模型输入 Shape
python paddle_infer_shape.py --model_dir /Users/calvin/Downloads/paddle_ocr/ch_ppocr_mobile_v2.0_cls_infer --model_filename inference.pdmodel --params_filename inference.pdiparams --save_dir /Users/calvin/Downloads/paddle_ocr/ch_ppocr_mobile_v2.0_cls_infer/new_model --input_shape_dict="{'x':[-1,3,-1,-1]}"

2). 然后再转onnx模型（这样可以解锁输入数据的大小限制）：
paddle2onnx --model_dir /Users/calvin/Downloads/paddle_ocr/ch_ppocr_mobile_v2.0_cls_infer/new_model \
            --model_filename inference.pdmodel \
            --params_filename inference.pdiparams \
            --save_file inference.onnx \
            --enable_dev_version True \
            --enable_onnx_checker True
```


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   


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