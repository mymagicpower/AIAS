### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1QlEPthADjv73NRSGtQviKw?pwd=zpbm

### 人体关键点SDK
检测图片中所有的行人，并识别每个人的肢体关键点。

### SDK功能
- 行人检测
- 肢体关键点检测

### SDK包含两个检测器：
-  PoseResnet18Estimation
simple pose, backbone: resnet18, dataset:imagenet
-  PoseResnet50Estimation
simple pose, backbone: resnet50, dataset:imagenet

### 运行例子 - PoseResnet18EstimationExample
- 测试图片
![pose-estimation](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/pose_estimation_sdk/pose-estimation.png)

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 
[	Joint [x=0.563, y=0.141], confidence: 0.9104,
	Joint [x=0.646, y=0.109], confidence: 0.8986,
	Joint [x=0.521, y=0.109], confidence: 0.9084,
	Joint [x=0.792, y=0.125], confidence: 0.8644,
	Joint [x=0.479, y=0.109], confidence: 0.8084,
	Joint [x=0.875, y=0.359], confidence: 0.5848,
	Joint [x=0.396, y=0.234], confidence: 0.5806,
	Joint [x=0.896, y=0.609], confidence: 0.4071,
	Joint [x=0.021, y=0.266], confidence: 0.7737,
	Joint [x=0.979, y=0.781], confidence: 0.4289,
	Joint [x=0.313, y=0.094], confidence: 0.7359,
	Joint [x=0.708, y=0.750], confidence: 0.4298,
	Joint [x=0.375, y=0.703], confidence: 0.4759
]
[INFO ] - [
	class: "person", probability: 0.98573, bounds: [x=0.695, y=0.063, width=0.304, height=0.926]
	class: "person", probability: 0.90103, bounds: [x=0.000, y=0.021, width=0.310, height=0.930]
	class: "person", probability: 0.73586, bounds: [x=0.317, y=0.004, width=0.380, height=0.991]
]
```

### 开源算法
#### 找不到了，后面会有更好的替换

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