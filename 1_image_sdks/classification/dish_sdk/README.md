## 目录：
http://aias.top/


# 菜品分类识别SDK
菜品识别sdk，支持8416种菜品的分类识别。

### SDK功能
- 支持8416种菜品的分类识别，并给出置信度。
- 提供两个可用模型例子
1). 大模型(resnet50)例子：DishesClassificationExample
2). 小模型(mobilenet_v2)例子：LightDishesClassExample

[菜品分类](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/dish_sdk/dishes.txt)

## 运行例子
- 测试图片
![dish](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/dish_sdk/dish.jpeg)

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
添加依赖库：lib/aias-dish-lib-0.1.0.jar

#### QQ群：111257454

