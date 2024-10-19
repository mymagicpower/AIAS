
### Download the model and put it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/traffic.zip


### Object Detection (Supports COCO Dataset Classification) SDK
Detects 80 classes of objects in images.

### Supports classification of the following:
- person
- bicycle
- car
- motorcycle
- airplane
- bus
- train
- truck
- boat
- traffic light
- fire hydrant
- stop sign
  ...

[Click to download](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/object_detection_sdk/coco_classes.txt)

### Running the example - CocoDetectionExample
- Test image
![tiger](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/traffic_sdk/result.png)

After running successfully, the command line should display the following information:
```text
[INFO ] - [
	class: "person", probability: 0.99875, bounds: [x=0.262, y=0.236, width=0.205, height=0.499]
	class: "person", probability: 0.99427, bounds: [x=0.518, y=0.218, width=0.116, height=0.551]
	class: "person", probability: 0.96588, bounds: [x=0.431, y=0.245, width=0.102, height=0.475]
	class: "bicycle", probability: 0.95741, bounds: [x=0.704, y=0.402, width=0.149, height=0.340]
	class: "person", probability: 0.93060, bounds: [x=0.775, y=0.287, width=0.102, height=0.455]
]
```


### Open source algorithm
#### 1. Open source algorithm used by SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
