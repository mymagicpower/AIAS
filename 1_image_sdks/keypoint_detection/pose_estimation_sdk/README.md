
### Download the model, place it in the models directory, and unzip
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/pose_estimation_models.zip

### Human Key Point SDK
Detects all pedestrians in the image and recognizes the key points of each person's limbs.

### SDK features

- Pedestrian detection
- Limb key point detection

### The SDK contains two detectors:
-  PoseResnet18Estimation
simple pose, backbone: resnet18, dataset:imagenet
-  PoseResnet50Estimation
simple pose, backbone: resnet50, dataset:imagenet

### Running Example - PoseResnet18EstimationExample
- Test picture
![pose-estimation](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/pose_estimation_sdk/pose-estimation.png)

After successful operation, the command line should see the following information:
```text
...
[INFO ] - 
[	Joint [x=0.563, y=0.141], confidence: 0.9104,
	Joint [x=0.646, y=0.109], confidence: 0.8986,
	Joint [x=0.521, y=0.109], confidence: 0.9084,
	Joint [x=0.792, y=0.125], confidence: 0.8644,
	Joint [x=0.479, y=0.109], confidence: 0.8084,
	Joint [x=0.875, y=0.359], confidence: 0.5848,
	Joint [x=0.396, y=0.234], confidence: 0.5806,
	Joint [x=0.896, y=0.609], confidence: 0.4071,
	Joint [x=0.021, y=0.266], confidence: 0.7737,
	Joint [x=0.979, y=0.781], confidence: 0.4289,
	Joint [x=0.313, y=0.094], confidence: 0.7359,
	Joint [x=0.708, y=0.750], confidence: 0.4298,
	Joint [x=0.375, y=0.703], confidence: 0.4759
]
[INFO ] - [
	class: "person", probability: 0.98573, bounds: [x=0.695, y=0.063, width=0.304, height=0.926]
	class: "person", probability: 0.90103, bounds: [x=0.000, y=0.021, width=0.310, height=0.930]
	class: "person", probability: 0.73586, bounds: [x=0.317, y=0.004, width=0.380, height=0.991]
]
```
