# 文字识别（OCR）
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

## OCR识别SDK功能
当前版本只支持水平文字识别(图片需摆正)。

### 模型的实现：
1. 小模型: 
模型推理例子代码: [LightOcrRecognitionExample.java](https://github.com/mymagicpower/AIAS/blob/main/ocr_sdk/src/main/java/me/calvin/example/LightOcrRecognitionExample.java).  

## 运行OCR识别例子
1. 首先下载例子代码
```bash
git clone https://github.com/mymagicpower/AIAS.git
```

2. 导入examples项目到IDE中：
```
cd ocr_sdk
```

3. 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/ocr_result.png
[INFO ] - [
	class: "你", probability: -1.0e+00, bounds: [x=0.319, y=0.164, width=0.050, height=0.057]
	class: "永远都", probability: -1.0e+00, bounds: [x=0.329, y=0.349, width=0.206, height=0.044]
	class: "无法叫醒一个", probability: -1.0e+00, bounds: [x=0.328, y=0.526, width=0.461, height=0.044]
	class: "装睡的人", probability: -1.0e+00, bounds: [x=0.330, y=0.708, width=0.294, height=0.043]
]
```
输出图片效果如下：
![ocr_result](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_result.png)


### 添加依赖lib - aais-ocr-lib-0.1.0.jar：
![lib](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/lib.png)
