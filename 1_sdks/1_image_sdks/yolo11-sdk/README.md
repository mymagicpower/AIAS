### 官网：
[官网链接](http://www.aias.top/)

### 下载模型
- 查看最新下载链接请查看 1_sdks/README.md
- 默认模型放置路径：yolo11-onnx\src\main\resources\

### YOLO11 模型SDK

#### 背景介绍
YOLO 是广受好评的YOLO （You Only Look Once）系列的最新进展，用于实时对象检测等。YOLO 支持各种视觉人工智能任务，
如检测、姿态估计、跟踪和分类。其先进的架构确保了卓越的速度和准确性，使其适用于各种应用。


### SDK功能：
-  目标检测
- ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/two_dogs_in_snow.jpeg)
-  人体关键点
- ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/two_dogs_in_snow.jpeg)
-  图像分类



### 开源算法
#### 1. sdk使用的开源算法
- https://github.com/ultralytics/ultralytics
- https://docs.ultralytics.com/zh

#### 2. 模型如何导出 ?
```text
from ultralytics import YOLO

# Load a model
# yolo11x-pose.pt
# yolo11n.pt
model = YOLO("yolo11x-cls.pt")

# Perform object detection on an image
results = model("D:\\ai_projects\\products\\python\\yolo\\img.png")
results[0].show()

# Export the model to ONNX format
path = model.export(format="onnx")  # return path to exported model
```
