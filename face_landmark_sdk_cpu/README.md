## 目录：
http://aias.top/


# 人脸关键点SDK
识别输入图片中的所有人脸关键点，每张人脸检测出68个关键点（人脸轮廓17个点，左右眉毛各5个点，左右眼睛各6个点，鼻子9个点，嘴巴20个点）。
- 关键点定义
![landmark](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/face_landmark_sdk/face_landmark.jpeg)

### SDK功能
- 人脸检测
- 人脸关键点检测（每张人脸检测出68个关键点）

## 运行例子 - FaceLandmarkExample
- 测试图片
![tiger](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/face_landmark_sdk/face-landmarks.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [0.026217185, 0.24056429, -0.0071445643,...,...., 0.5127686, 0.85029036]
[INFO ] - [
	class: "Face", probability: 0.99995, bounds: [x=0.179, y=0.236, width=0.130, height=0.232]
	class: "Face", probability: 0.99989, bounds: [x=0.490, y=0.207, width=0.112, height=0.225]
	class: "Face", probability: 0.99984, bounds: [x=0.831, y=0.283, width=0.115, height=0.212]
]
```

### 帮助 
添加依赖库：lib/aias-face-landmark-lib-0.1.0.jar

## QQ群：
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/OCR_QQ.png)
