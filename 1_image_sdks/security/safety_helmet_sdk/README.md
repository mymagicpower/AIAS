### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1CV7mWcLOhcCJG4n1nf5apA?pwd=v182

### 安全帽检测SDK
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

#### 运行小模型例子 - SmallSafetyHelmetDetectExample
- 测试图片
![small](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_s.png)

#### 运行中模型例子 - MediumSafetyHelmetDetectExample
- 测试图片
![medium](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_m.png)

#### 运行大模型例子 - LargeSafetyHelmetDetectExample
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

### 开源算法
#### 1. sdk使用的开源算法
- [Safety-Helmet-Wearing-Dataset](https://github.com/njvisionpower/Safety-Helmet-Wearing-Dataset)

#### 2. 模型如何导出 ?
- 直接支持，无需特殊格式导出。

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