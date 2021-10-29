# 车辆检测SDK
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


## 运行例子 - VehicleDetectExample
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

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   