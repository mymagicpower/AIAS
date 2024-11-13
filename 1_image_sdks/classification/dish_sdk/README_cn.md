### 官网：
[官网链接](https://www.aias.top/)

### 下载模型
- 链接: https://pan.baidu.com/s/1No67j8xhXlDfx676P14yQw?pwd=gtdq

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

### 菜品分类识别SDK
菜品识别sdk，支持8416种菜品的分类识别。

### SDK功能
- 支持8416种菜品的分类识别，并给出置信度。
- 提供两个可用模型例子
1). 大模型(resnet50)例子：DishesClassificationExample
2). 小模型(mobilenet_v2)例子：LightDishesClassExample

[菜品分类](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dishes.txt)

### 运行例子
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

### 开源算法
#### 1. sdk使用的开源算法
[PaddleClas](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/README_ch.md)
#### 2. 模型如何导出 ?
- [export_model](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/tools/export_model.py)
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