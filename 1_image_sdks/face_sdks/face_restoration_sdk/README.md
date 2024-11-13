### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/10lwRrO3puEMpnVHD9FYObg?pwd=c134

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

#### 图片人脸修复 SDK
人工智能图片人脸修复是一种应用计算机视觉技术和深度学习算法进行图像修复的方法。这种技术可以自动识别图像中的人脸，并进行修复和还原，从而使图像更加完整、清晰和自然。它可以处理各种类型的图像，包括黑白照片、彩色照片和视频帧等。相较于传统的图像修复方法，人工智能图片人脸修复更加高效和准确。它可以快速地修复照片中的缺陷，例如面部皮肤瑕疵、眼睛或嘴巴的闭合问题等，使其看起来更加美观自然。这种技术在图像处理、医学影像、电影制作等领域都有着广泛的应用前景，并且随着人工智能技术的不断发展，其应用领域也会越来越广泛。

#### 图片人脸修复java实现 - FaceRestorationExample
- 自动检测人脸及关键地，然后抠图，然后根据人脸关键点转正对齐
  ![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_det.png)

- 对所有转正对齐的人脸提升分辨率
  ![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sr.png)

- 使用分割模型提取人脸，逆向变换后贴回原图
  ![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_res.png)

  
### 开源算法
#### 1. sdk使用的开源算法
- [GFPGAN](https://github.com/TencentARC/GFPGAN)

  
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