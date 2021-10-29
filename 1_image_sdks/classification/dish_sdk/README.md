## 目录：
http://aias.top/


# 菜品分类识别SDK
菜品识别sdk，支持8416种菜品的分类识别。

### SDK功能
- 支持8416种菜品的分类识别，并给出置信度。
- 提供两个可用模型例子
1). 大模型(resnet50)例子：DishesClassificationExample
2). 小模型(mobilenet_v2)例子：LightDishesClassExample

[菜品分类](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dishes.txt)

## 运行例子
- 测试图片
![dish](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dish.jpeg)

运行成功后，命令行应该看到下面的信息:
```text
清炒虾仁 : 1.0
[INFO ] - [
	class: "清炒虾仁", probability: 1.00000
	class: "豆炒虾仁", probability: 0.84546
	class: "西芹果仁炒虾", probability: 0.34755
	class: "瓜炒虾仁", probability: 0.21187
	class: "炒芦笋", probability: 0.19085
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