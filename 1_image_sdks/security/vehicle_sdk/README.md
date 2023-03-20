
### Download the model and place it in the models folder
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/vehicle.zip

### Vehicle Detection SDK
Vehicle detection is a very important and challenging task in urban traffic monitoring, and its difficulty lies in accurately locating and classifying relatively small vehicles in complex scenes.
Recognize vehicle types such as car, truck, bus, motorbike, and tricycle.

### SDK functions

- Vehicle detection, giving detection boxes and confidence
- Supported categories:
  -car
  -truck
  -bus
  -motorbike
  -tricycle
  -carplate


#### Example-VehicleDetectExample
- Test image
![vehicle](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/vehicle_result.png)

After running successfully, the command line should see the following information:
```text
[INFO ] - [
	class: "car", probability: 0.98734, bounds: [x=0.210, y=0.420, width=0.225, height=0.218]
	class: "car", probability: 0.93550, bounds: [x=0.377, y=0.432, width=0.150, height=0.120]
	class: "car", probability: 0.88870, bounds: [x=0.167, y=0.411, width=0.127, height=0.178]
	class: "car", probability: 0.85094, bounds: [x=0.479, y=0.351, width=0.504, height=0.476]
	class: "carplate", probability: 0.83096, bounds: [x=0.321, y=0.502, width=0.046, height=0.019]
]
```

### Open source algorithm
#### 1. Open source algorithms used by the SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)

#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
