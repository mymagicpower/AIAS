## 目录：
http://aias.top/


# 实例分割(支持80分类)SDK
支持对图片中80个分类的目标进行实例分割。

### 支持coco数据集分类如下：
- person
- bicycle
- car
- motorcycle
- airplane
- bus
- train
- truck
- boat
- traffic light
- fire hydrant
- stop sign
- parking meter
- bench
- bird
- ...

[点击下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/Instance_segmentation_sdk/coco_classes.txt)

### SDK算法：
-  InstanceSegmentation
mask_rcnn, backbone: resnet18，dataset: coco

## 运行例子 - InstanceSegmentationExample
- 测试图片
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/Instance_segmentation_sdk/result.jpeg)

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - [
	class: "dog", probability: 0.99422, bounds: [x=0.024, y=0.344, width=0.225, height=0.542]
	class: "person", probability: 0.97572, bounds: [x=0.263, y=0.139, width=0.209, height=0.474]
	class: "person", probability: 0.97018, bounds: [x=0.236, y=0.148, width=0.547, height=0.685]
	class: "cat", probability: 0.88282, bounds: [x=0.276, y=0.551, width=0.249, height=0.194]
	class: "chair", probability: 0.55487, bounds: [x=0.716, y=0.249, width=0.187, height=0.276]
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