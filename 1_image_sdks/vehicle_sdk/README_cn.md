### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/144DQ7G-cCXOSox-I7WMp8g?pwd=sgve

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 车辆检测SDK
车辆检测是城市交通监控中非常重要并且具有挑战性的任务，该任务的难度在于对复杂场景中相对较小的车辆进行精准地定位和分类。
支持car (汽车)，truck (卡车)，bus (公交车)，motorbike (摩托车)，tricycle (三轮车)等车型的识别。

### SDK功能
- 车辆检测，给出检测框和置信度
- 支持的类别：
- car
- truck
- bus
- motorbike
- tricycle
- carplate


#### 运行例子 - VehicleDetectExample
- 测试图片
![vehicle](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/vehicle_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "car", probability: 0.98734, bounds: [x=0.210, y=0.420, width=0.225, height=0.218]
	class: "car", probability: 0.93550, bounds: [x=0.377, y=0.432, width=0.150, height=0.120]
	class: "car", probability: 0.88870, bounds: [x=0.167, y=0.411, width=0.127, height=0.178]
	class: "car", probability: 0.85094, bounds: [x=0.479, y=0.351, width=0.504, height=0.476]
	class: "carplate", probability: 0.83096, bounds: [x=0.321, y=0.502, width=0.046, height=0.019]
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