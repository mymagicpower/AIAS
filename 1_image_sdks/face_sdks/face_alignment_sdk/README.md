### 官网：
[官网链接](https://www.aias.top/)

### 下载模型
- 链接: https://pan.baidu.com/s/10lwRrO3puEMpnVHD9FYObg?pwd=c134

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

### 人脸对齐 SDK
sdk给出了人脸对齐的 java 实现。
人脸对齐算法是一种用于对齐人脸图像中的人脸的技术。其目的是将人脸图像中的人脸调整到一个标准的位置和大小，以便于后续的人脸识别、表情识别、面部分析等任务的进行。
这种技术主要通过对人脸进行旋转、缩放和平移等调整来实现。在对齐过程中，算法会根据人脸的特征点（如眼睛、鼻子、嘴巴等）进行定位和标记，然后根据这些特征点的位置和关系进行调整，使得人脸的位置和大小达到一定的标准。
人脸对齐技术对于提高人脸识别的准确性和效率非常重要。因为在人脸识别过程中，不同的人脸可能存在着不同的姿态、表情和光照等情况，而人脸对齐技术可以将这些差异减少，从而更好地进行识别和分析。
总的来说，人脸对齐技术是计算机视觉领域中的一项重要技术，在人脸识别、表情识别、面部分析等应用中有着广泛的应用前景。

#### 人脸识别关键技术
人脸识别涉及的关键技术包含：人脸检测，人脸关键点，人脸特征提取，人脸比对，人脸对齐。
![face_sdk](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sdk.png)

#### 人脸对齐的实现：
#### 运行人脸检测的例子 FaceAlignExample.java
运行成功后，输出图片效果如下：
![face_align](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_align.png)


### 开源算法
#### 1. sdk使用的开源算法
- [RetinaFaceDetection - Pytorch_Retinaface](https://github.com/biubug6/Pytorch_Retinaface)
- [LightFaceDetection - Ultra-Light-Fast-Generic-Face-Detector-1MB](https://github.com/Linzaer/Ultra-Light-Fast-Generic-Face-Detector-1MB)



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
