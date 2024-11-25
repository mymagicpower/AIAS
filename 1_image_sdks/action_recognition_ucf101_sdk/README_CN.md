## 目录：
http://aias.top/

### 行为识别(支持ucf101数据集分类)SDK
识别图片中101种行为。

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1qwwCTKMRHRkt1bG4lpE7bg?pwd=fkpk
- 
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

[点击下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/action_recognition_sdk/ucf101_classes.txt)

### SDK包含两个检测器：
-  Inceptionv3ActionRecognition
backbone: inceptionv3, dataset:ucf101
-  Vgg16ActionRecognition
backbone: vgg16, dataset:ucf101

## 运行例子 - Inceptionv3ActionRecognitionExample
- 测试图片
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/object_detection_sdk/SSDResnet50Detection.png)

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

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
