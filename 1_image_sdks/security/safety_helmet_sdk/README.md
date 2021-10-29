# 安全帽检测SDK
安全帽检测。
- 支持类别：
- safe
- unsafe

### SDK功能
- 安全帽检测，给出检测框和置信度
- 三个模型：
- 小模型（mobilenet0.25）
- 中模型（mobilenet1.0）
- 大模型（darknet53）

## 运行小模型例子 - SmallSafetyHelmetDetectExample
- 测试图片
![small](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_s.png)

## 运行中模型例子 - MediumSafetyHelmetDetectExample
- 测试图片
![medium](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_m.png)

## 运行大模型例子 - LargeSafetyHelmetDetectExample
- 测试图片
![large](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_l.png)


运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "safe 0.9983590245246887", probability: 0.99835, bounds: [x=0.244, y=0.000, width=0.086, height=0.150]
	class: "unsafe 0.998088538646698", probability: 0.99808, bounds: [x=0.226, y=0.204, width=0.115, height=0.263]
	class: "safe 0.997364342212677", probability: 0.99736, bounds: [x=0.584, y=0.247, width=0.162, height=0.302]
	class: "safe 0.9963852167129517", probability: 0.99638, bounds: [x=0.319, y=0.000, width=0.076, height=0.133]
	class: "safe 0.9952006936073303", probability: 0.99520, bounds: [x=0.757, y=0.262, width=0.111, height=0.264]
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