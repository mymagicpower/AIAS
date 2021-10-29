## 目录：
http://aias.top/

# 动物分类识别SDK
动物识别sdk，支持7978种动物的分类识别。

### SDK功能
- 支持7978种动物的分类识别，并给出置信度。
- 提供两个可用模型例子
1). 大模型(resnet50)例子：AnimalsClassificationExample
2). 小模型(mobilenet_v2)例子：LightAnimalsClassExample

[动物分类](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/animals.txt)

## 运行例子
- 测试图片
![tiger](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/tiger.jpeg)

运行成功后，命令行应该看到下面的信息:
```text
西伯利亚虎 : 1.0
[INFO ] - [
	class: "西伯利亚虎", probability: 1.00000
	class: "孟加拉虎", probability: 0.02022
	class: "华南虎", probability: 0.00948
	class: "苏门答腊虎", probability: 0.00397
	class: "印度支那虎", probability: 0.00279
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