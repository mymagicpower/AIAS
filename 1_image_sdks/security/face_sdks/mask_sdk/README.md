
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip

### Mask Detection SDK
Mask detection helps fight against pneumonia, and artificial intelligence technology is being applied to epidemic prevention and control. Wearing a mask has become one of the most important measures to cut off transmission routes in epidemic prevention. However, in practical scenarios, there are still people who do not take it seriously, do not pay attention, and have a lucky mentality, especially in public places, which poses great risks to individuals and the public.
Based on artificial intelligence, mask detection function can perform real-time detection based on camera video stream.

#### SDK function
- Mask detection

#### Running example
1. After successful operation, the command line should see the following information:
```text
[INFO ] -  Face mask detection result image has been saved in: build/output/faces_detected.png
[INFO ] - [
	class: "MASK", probability: 0.99998, bounds: [x=0.608, y=0.603, width=0.148, height=0.265]
	class: "MASK", probability: 0.99998, bounds: [x=0.712, y=0.154, width=0.129, height=0.227]
	class: "NO MASK", probability: 0.99997, bounds: [x=0.092, y=0.123, width=0.066, height=0.120]
	class: "NO MASK", probability: 0.99986, bounds: [x=0.425, y=0.146, width=0.062, height=0.114]
	class: "MASK", probability: 0.99981, bounds: [x=0.251, y=0.671, width=0.088, height=0.193]
]
```
2. The output image effect is as follows:
![result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png)


### Open source algorithms
#### 1. Open source algorithms used by SDK
- [PaddleDetection](https://github.com/PaddlePaddle/PaddleDetection)
- [PaddleClas](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/README_ch.md)

#### 2. How to export the model?
- [export_model](https://github.com/PaddlePaddle/PaddleDetection/blob/release%2F2.4/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)

