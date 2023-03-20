
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/animals.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/mobilenet_animals.zip
- 
### Animal classification recognition SDK
Animal recognition SDK that supports the classification of 7978 types of animals.

### ### SDK features
- Supports the classification recognition of 7978 types of animals and provides confidence levels.
- Provides two available model examples:
  1). Example of the large model (resnet50): AnimalsClassificationExample
  2). Example of the small model (mobilenet_v2): LightAnimalsClassExample

[Animal classification](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/animals.txt)

### Running examples
- Test image
![tiger](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/tiger.jpeg)

After successful execution, the command line should display the following information:
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


### Open source algorithms
#### 1. Open source algorithm used in the SDK
- [PaddleClas](https://github.com/PaddlePaddle/PaddleClas)
#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/tools/export_model.py)    
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)   
