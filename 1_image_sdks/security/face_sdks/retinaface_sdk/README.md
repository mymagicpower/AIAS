
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/retinaface.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/ultranet.zip

### Face detection (with 5 face landmarks) SDK
This example provides a reference implementation of face detection (with 5 face landmarks).

### Face detection (with 5 face landmarks) provides implementations of two models:

1. Small model:
   Example code for model inference: LightFaceDetectionExample.java
2. Large model:
   Example code for model inference: RetinaFaceDetectionExample.java


### Running the face detection example

1. After successful execution, the command line should display the following information:

```text
[INFO ] - Face detection result image has been saved in: build/output/retinaface_detected.png
[INFO ] - [
	class: "Face", probability: 0.99993, bounds: [x=0.552, y=0.762, width=0.071, height=0.156]
	class: "Face", probability: 0.99992, bounds: [x=0.696, y=0.665, width=0.071, height=0.155]
	class: "Face", probability: 0.99976, bounds: [x=0.176, y=0.778, width=0.033, height=0.073]
	class: "Face", probability: 0.99961, bounds: [x=0.934, y=0.686, width=0.032, height=0.068]
	class: "Face", probability: 0.99949, bounds: [x=0.026, y=0.756, width=0.039, height=0.078]
]
```
2. The output image is as follows:
![detected-faces](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/retinaface_detected.png)

   
### Open source algorithms
#### 1. Open source algorithms used by the SDK
- [RetinaFaceDetection - Pytorch_Retinaface](https://github.com/biubug6/Pytorch_Retinaface)
- [LightFaceDetection - Ultra-Light-Fast-Generic-Face-Detector-1MB](https://github.com/Linzaer/Ultra-Light-Fast-Generic-Face-Detector-1MB)


#### 2. How to export models?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

