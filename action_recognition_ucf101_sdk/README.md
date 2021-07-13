# 行为识别(支持ucf101数据集分类)SDK
识别图片中101种行为。

### 支持分类如下：
- ApplyEyeMakeup
- ApplyLipstick
- Archery
- BabyCrawling
- BalanceBeam
- BandMarching
- BaseballPitch
- Basketball
- BasketballDunk
- BenchPress
- Biking
- Billiards
- BlowDryHair
- BlowingCandles
- BodyWeightSquats
- Bowling
- ...

[点击下载](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/action_recognition_sdk/ucf101_classes.txt)

### SDK包含两个检测器：
-  Inceptionv3ActionRecognition
backbone: inceptionv3, dataset:ucf101
-  Vgg16ActionRecognition
backbone: vgg16, dataset:ucf101

## 运行例子 - Inceptionv3ActionRecognitionExample
- 测试图片
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/object_detection_sdk/SSDResnet50Detection.png)

运行成功后，命令行应该看到下面的信息:
```text
...
FrontCrawl : 0.999593198299408
[INFO ] - [
	class: "FrontCrawl", probability: 0.99959
	class: "BreastStroke", probability: 0.00039
	class: "Diving", probability: 0.00001
	class: "StillRings", probability: 2.8e-06
	class: "PoleVault", probability: 5.0e-07
]
```

### 帮助 
添加依赖库：lib/action-recognition-ucf101-lib-0.1.0.jar

## QQ群：
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/OCR_QQ.png)
