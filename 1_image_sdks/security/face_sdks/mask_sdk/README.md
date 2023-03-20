### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1hM3HFi3kviMshOGKKlVCLg?pwd=p6t4

### 口罩检测SDK
口罩检测助力抗击肺炎，人工智能技术正被应用到疫情防控中来。
抗疫切断传播途径中，佩戴口罩已经几乎成为了最重要的举措之一。但是在实际场景中，仍然有不重视、不注意、侥幸心理的人员不戴口罩，尤其在公众场合，给个人和公众造成极大的风险隐患。
而基于人工智能的口罩检测功能可以基于摄像头视频流进行实时检测。

#### SDK功能
- 口罩检测

#### 运行例子
1. 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] -  Face mask detection result image has been saved in: build/output/faces_detected.png
[INFO ] - [
	class: "MASK", probability: 0.99998, bounds: [x=0.608, y=0.603, width=0.148, height=0.265]
	class: "MASK", probability: 0.99998, bounds: [x=0.712, y=0.154, width=0.129, height=0.227]
	class: "NO MASK", probability: 0.99997, bounds: [x=0.092, y=0.123, width=0.066, height=0.120]
	class: "NO MASK", probability: 0.99986, bounds: [x=0.425, y=0.146, width=0.062, height=0.114]
	class: "MASK", probability: 0.99981, bounds: [x=0.251, y=0.671, width=0.088, height=0.193]
]
```
2. 输出图片效果如下：
![result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png)


### 开源算法
#### 1. sdk使用的开源算法
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
- [PaddleClas](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/README_ch.md)

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
