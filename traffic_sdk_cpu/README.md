# 人车非识别SDK
人车非（人、机动车，非机动车）识别sdk，支持6种目标检测识别。

### SDK功能
支持的6种目标检测识别如下：    
- 1-person 行人 
- 2-bicycle 自行车 
- 3-car 小汽车 
- 4-motorcycle 摩托车 
- 5-bus 公共汽车
- 6-truck 货车

## 运行例子 - TrafficDetectionExample
- 测试图片
![tiger](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/traffic_sdk/result.png)

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

### 帮助 
添加依赖库：lib/aias-traffic-lib-0.1.0.jar
