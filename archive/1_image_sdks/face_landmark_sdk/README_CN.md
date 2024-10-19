### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1yCzwcWED9n74kw2cDy5hSg?pwd=pfjx

### 人脸关键点SDK
识别输入图片中的所有人脸关键点，每张人脸检测出68个关键点（人脸轮廓17个点，左右眉毛各5个点，左右眼睛各6个点，鼻子9个点，嘴巴20个点）。

- 关键点定义
![landmark](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_landmark_sdk/face_landmark.jpeg)

### SDK功能
- 人脸关键点检测（每张人脸检测出68个关键点）

### 运行例子 - FaceLandmarkExample
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

### 开源算法
#### 1. sdk使用的开源算法
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
#### 2. 模型如何导出 ?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   



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