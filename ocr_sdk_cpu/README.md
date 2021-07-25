## 目录：
http://aias.top/

# 文字识别（OCR）
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

## OCR识别SDK功能
1. 方向检测   
- 0度   
- 90度   
- 180度   
- 270度   
![detect_direction](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/detect_direction.png)
2. 图片旋转
3. 文字识别

## 运行OCR识别例子
### 1. 文字方向检测：
- 例子代码: OcrDetectionExample.java    
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/detect_result.png
[INFO ] - [
	class: "0", probability: 1.00000, bounds: [x=0.073, y=0.069, width=0.275, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.652, y=0.158, width=0.222, height=0.040]
	class: "0", probability: 1.00000, bounds: [x=0.143, y=0.252, width=0.144, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.628, y=0.328, width=0.168, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.064, y=0.330, width=0.450, height=0.023]
]
```
- 输出图片效果如下：
![detect_result](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/detect_result.png)

### 2. 文字方向检测帮助类（增加置信度信息显示，便于调试）：
- 例子代码: OcrDetectionHelperExample.java 
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/detect_result_helper.png
[INFO ] - [
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.073, y=0.069, width=0.275, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.652, y=0.158, width=0.222, height=0.040]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.143, y=0.252, width=0.144, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.628, y=0.328, width=0.168, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.064, y=0.330, width=0.450, height=0.023]
]
```
- 输出图片效果如下：
![detect_result_helper](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/detect_result_helper.png)

### 3. 图片旋转：
每调用一次rotateImg方法，会使图片逆时针旋转90度。
- 例子代码: RotationExample.java 

旋转前图片                    |  旋转后图片                     
:-------------------------:|:-------------------------:
![](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ticket_0.png)        |  ![](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/rotate_result.png)         

### 2. 文字识别：
再使用本方法前，请调用上述方法使图片文字呈水平0度方向。  
- 例子代码: LightOcrRecognitionExample.java  
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/ocr_result.png
[INFO ] - [
	class: "你", probability: -1.0e+00, bounds: [x=0.319, y=0.164, width=0.050, height=0.057]
	class: "永远都", probability: -1.0e+00, bounds: [x=0.329, y=0.349, width=0.206, height=0.044]
	class: "无法叫醒一个", probability: -1.0e+00, bounds: [x=0.328, y=0.526, width=0.461, height=0.044]
	class: "装睡的人", probability: -1.0e+00, bounds: [x=0.330, y=0.708, width=0.294, height=0.043]
]
```
- 输出图片效果如下：
![ocr_result](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_result.png)

### 帮助 
[点击下载SDK](https://djl-model.oss-cn-hongkong.aliyuncs.com/jars/aais-ocr-lib-0.1.0.jar). 

添加依赖lib - aais-ocr-lib-0.1.0.jar：
![lib](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/lib.png)

## QQ群：
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/OCR_QQ.png)
