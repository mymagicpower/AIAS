# 行人检测SDK
行人检测是利用计算机视觉技术判断图像中是否存在行人并给予精确定位，一般用矩形框表示。
行人检测技术有很强的使用价值，它可以与行人跟踪，行人重识别等技术结合，应用于汽车无人驾驶系统，
智能视频监控，人体行为分析，客流统计系统，智能交通等领域。


### SDK功能
- 行人检测，给出检测框和置信度

## 运行例子 - PedestrianDetectExample
- 测试图片
![pedestrian](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/ped_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "pedestrian", probability: 0.97251, bounds: [x=0.284, y=0.451, width=0.101, height=0.394]
	class: "pedestrian", probability: 0.97015, bounds: [x=0.418, y=0.448, width=0.082, height=0.377]
	class: "pedestrian", probability: 0.96476, bounds: [x=0.568, y=0.423, width=0.105, height=0.411]
	class: "pedestrian", probability: 0.95523, bounds: [x=0.811, y=0.401, width=0.104, height=0.436]
	class: "pedestrian", probability: 0.93908, bounds: [x=0.680, y=0.433, width=0.074, height=0.352]
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