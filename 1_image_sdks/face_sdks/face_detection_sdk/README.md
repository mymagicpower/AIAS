### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/10lwRrO3puEMpnVHD9FYObg?pwd=c134

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

#### 人脸检测(含5个人脸关键点)SDK
sdk给出了人脸检测的 java 实现。
人脸检测算法是一种计算机视觉技术，它基于机器学习和人工智能技术，用于自动检测和定位图像或视频中的人脸。这种算法可以识别脸部特征和形状，并根据这些信息对人脸进行分类和跟踪。
在计算机视觉中，人脸检测是一个基础问题，它是许多高级应用程序的先决条件，例如人脸识别、表情识别、人脸跟踪和人脸合成。人脸检测算法通过分析图像、视频中的像素值，来确定图像中是否存在人脸，如果存在，就可以定位人脸的位置和大小。
目前，人脸检测算法的应用非常广泛，它被广泛应用于安全监控、人机交互、智能家居等领域。例如，许多智能手机、平板电脑和笔记本电脑都已经配备了人脸识别功能，这些设备使用人脸检测算法来确定用户是否有权限访问设备。
总之，人脸检测算法是一种非常重要的计算机视觉技术，它可以在许多不同的应用程序中发挥作用，使得我们的生活更加智能、便捷和安全。

#### 人脸识别关键技术
人脸识别涉及的关键技术包含：人脸检测，人脸关键点，人脸特征提取，人脸比对，人脸对齐。
![face_sdk](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sdk.png)

本文的例子给出了人脸检测(含5个人脸关键点)的参考实现。
#### 人脸检测(含5个人脸关键点)提供了两个模型的实现：
1. 小模型: 
模型推理例子代码: LightFaceDetectionExample.java 

2. 大模型: 
模型推理例子代码: RetinaFaceDetectionExample.java 


#### 运行人脸检测的例子
1. 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Face detection result image has been saved in: build/output/retinaface_detected.png
[INFO ] - [
	class: "Face", probability: 0.99993, bounds: [x=0.552, y=0.762, width=0.071, height=0.156]
	class: "Face", probability: 0.99992, bounds: [x=0.696, y=0.665, width=0.071, height=0.155]
	class: "Face", probability: 0.99976, bounds: [x=0.176, y=0.778, width=0.033, height=0.073]
	class: "Face", probability: 0.99961, bounds: [x=0.934, y=0.686, width=0.032, height=0.068]
	class: "Face", probability: 0.99949, bounds: [x=0.026, y=0.756, width=0.039, height=0.078]
]
```
2. 输出图片效果如下：
![detected-faces](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/retinaface_detected.png)

   
### 开源算法
#### 1. sdk使用的开源算法
- [RetinaFaceDetection - Pytorch_Retinaface](https://github.com/biubug6/Pytorch_Retinaface)
- [Face-Detector-1MB-with-landmark](https://github.com/biubug6/Face-Detector-1MB-with-landmark)



#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html
