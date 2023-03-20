### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1HYTfWazlk8W9pQDGVWAYbA?pwd=8mwt


### 目标检测(支持coco数据集分类)SDK
检测图片中80个分类的目标。

### 支持分类如下：
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
  ...

[点击下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/object_detection_sdk/coco_classes.txt)

### 运行例子 - CocoDetectionExample
- 测试图片
![tiger](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/traffic_sdk/result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "person", probability: 0.99875, bounds: [x=0.262, y=0.236, width=0.205, height=0.499]
	class: "person", probability: 0.99427, bounds: [x=0.518, y=0.218, width=0.116, height=0.551]
	class: "person", probability: 0.96588, bounds: [x=0.431, y=0.245, width=0.102, height=0.475]
	class: "bicycle", probability: 0.95741, bounds: [x=0.704, y=0.402, width=0.149, height=0.340]
	class: "person", probability: 0.93060, bounds: [x=0.775, y=0.287, width=0.102, height=0.455]
]
```


### 开源算法
#### 1. sdk使用的开源算法
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
#### 2. 模型如何导出 ?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)



### 其它帮助信息
http://aias.top/guides.html

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