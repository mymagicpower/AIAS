
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/face_landmark.zip

### Face Key Point SDK
Identify all face key points in the input image, with 68 key points detected for each face (17 points for the face contour, 5 points for each eyebrow, 6 points for each eye, 9 points for the nose, and 20 points for the mouth).

- Key point definition
![landmark](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_landmark_sdk/face_landmark.jpeg)

### SDK functions
- Face key point detection (68 key points detected for each face)

### Run Example - FaceLandmarkExample
- Test image
![landmarks](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_landmark_sdk/face-landmarks.png)

After successful execution, the command line should display the following information:
```text
[INFO ] - [-0.0155213205, 0.4801023, 0.0031973184,...,....,0.51944584, 0.7756358]
[INFO ] - [0.091941595, 0.3264855, 0.052257717,...,...., 0.23191518, 0.81276375]
[INFO ] - [0.026217185, 0.24056429, -0.0071445643,...,...., 0.5127686, 0.85029036]

[INFO ] -  Face landmarks detection result image has been saved in: build/output/face_landmarks.png

[INFO ] - [
	class: "Face", probability: 0.99995, bounds: [x=0.179, y=0.236, width=0.130, height=0.232]
	class: "Face", probability: 0.99989, bounds: [x=0.490, y=0.207, width=0.112, height=0.225]
	class: "Face", probability: 0.99984, bounds: [x=0.831, y=0.283, width=0.115, height=0.212]
]
```

### Open Source Algorithms
#### 1. Open source algorithms used by the SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
