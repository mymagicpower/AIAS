
###Download models and place them in the /models directory

- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/dishes.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/mobilenet_dishes.zip

### Dish Classification Recognition SDK
The dish recognition SDK supports the classification recognition of 8416 kinds of dishes.

### SDK Functions
-Supports the classification recognition of 8416 kinds of dishes and provides confidence levels.
-Provides two available model examples
1. Example of the large model (resnet50): DishesClassificationExample
2. Example of the small model (mobilenet_v2): LightDishesClassExample

[Dish Classification](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dishes.txt)

### Running Examples
-Test image
![dish](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dish.jpeg)

After successful execution, the command line should display the following information:
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

### Open source algorithms
#### 1. Open source algorithms used in the SDK
[PaddleClas](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/README_ch.md)
#### 2. How to export models?
- [export_model](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)

