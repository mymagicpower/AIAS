### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1kPDducw29Ln_Hxv17ks86A?pwd=gq36

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 行人检测SDK
行人检测是利用计算机视觉技术判断图像中是否存在行人并给予精确定位，一般用矩形框表示。
行人检测技术有很强的使用价值，它可以与行人跟踪，行人重识别等技术结合，应用于汽车无人驾驶系统，
智能视频监控，人体行为分析，客流统计系统，智能交通等领域。


### SDK功能
- 行人检测，给出检测框和置信度

#### 运行例子 - PedestrianDetectExample
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

### 开源算法
#### 1. sdk使用的开源算法
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)

#### 2. 模型如何导出 ?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)


### 其它帮助信息
https://aias.top/guides.html

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   


#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html