
### Download the model and place it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/pedestrian.zip

### Pedestrian Detection SDK
Pedestrian detection is the use of computer vision technology to determine whether there are pedestrians in the image and to provide accurate positioning, generally represented by a rectangular box. Pedestrian detection technology has strong practical value. It can be combined with pedestrian tracking, pedestrian re-identification, and other technologies, and applied to autonomous driving systems for cars, intelligent video surveillance, human behavior analysis, passenger flow statistics system, intelligent transportation and other fields.

### SDK Functionality
- Pedestrian detection, providing detection boxes and confidence

#### Running Example- PedestrianDetectExample
- Test image
![pedestrian](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/ped_result.png)

After successful operation, the command line should see the following information:
```text
[INFO ] - [
	class: "pedestrian", probability: 0.97251, bounds: [x=0.284, y=0.451, width=0.101, height=0.394]
	class: "pedestrian", probability: 0.97015, bounds: [x=0.418, y=0.448, width=0.082, height=0.377]
	class: "pedestrian", probability: 0.96476, bounds: [x=0.568, y=0.423, width=0.105, height=0.411]
	class: "pedestrian", probability: 0.95523, bounds: [x=0.811, y=0.401, width=0.104, height=0.436]
	class: "pedestrian", probability: 0.93908, bounds: [x=0.680, y=0.433, width=0.074, height=0.352]
]
```

### Open Source Algorithms
#### 1. Open source algorithms used by SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)

#### 2. How is the model exported?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
