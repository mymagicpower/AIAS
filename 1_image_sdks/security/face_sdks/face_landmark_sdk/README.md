## 目录：
http://aias.top/

## 人脸关键点SDK
识别输入图片中的所有人脸关键点，每张人脸检测出68个关键点（人脸轮廓17个点，左右眉毛各5个点，左右眼睛各6个点，鼻子9个点，嘴巴20个点）。

- 关键点定义
![landmark](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_landmark_sdk/face_landmark.jpeg)

### SDK功能
- 人脸关键点检测（每张人脸检测出68个关键点）

## 运行例子 - FaceLandmarkExample
- 测试图片
![landmarks](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_landmark_sdk/face-landmarks.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [-0.0155213205, 0.4801023, 0.0031973184,...,....,0.51944584, 0.7756358]
[INFO ] - [0.091941595, 0.3264855, 0.052257717,...,...., 0.23191518, 0.81276375]
[INFO ] - [0.026217185, 0.24056429, -0.0071445643,...,...., 0.5127686, 0.85029036]

[INFO ] -  Face landmarks detection result image has been saved in: build/output/face_landmarks.png

[INFO ] - [
	class: "Face", probability: 0.99995, bounds: [x=0.179, y=0.236, width=0.130, height=0.232]
	class: "Face", probability: 0.99989, bounds: [x=0.490, y=0.207, width=0.112, height=0.225]
	class: "Face", probability: 0.99984, bounds: [x=0.831, y=0.283, width=0.115, height=0.212]
]
```


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   

