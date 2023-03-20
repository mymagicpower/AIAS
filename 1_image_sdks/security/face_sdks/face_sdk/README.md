
### Download the model and place it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip

### Face Detection SDK
This article gives an example implementation of face detection.

#### Face Detection:
Model Inference Example: FaceDetectionExample.java

#### Running the Face Detection Example
1. After successful execution, the command line should display the following information:
```text
[INFO ] -  Face detection result image has been saved in: build/output/faces_detected.png
[INFO ] - [
	class: "Face", probability: 0.99996, bounds: [x=0.179, y=0.236, width=0.129, height=0.232]
	class: "Face", probability: 0.99989, bounds: [x=0.490, y=0.206, width=0.112, height=0.225]
	class: "Face", probability: 0.99981, bounds: [x=0.830, y=0.283, width=0.115, height=0.212]
]
```
2. The output image looks like this:
![detected-faces](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/faces_detected.png)



### Open Source Algorithms
#### 1. Open source algorithms used by SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
