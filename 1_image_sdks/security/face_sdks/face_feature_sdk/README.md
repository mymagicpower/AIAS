
### Download the model and put it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/MobileFace.zip

### Face feature extraction and comparison SDK
This example provides a reference implementation for face feature extraction and comparison.
For a complete example, please refer to 8_suite_hub/face_search (PyTorch algorithm implementation,
please refer to the existing implementation and convert it to Paddle algorithm as needed).
The complete pipeline for face recognition: face detection (including facial landmarks) --> face alignment -->
face feature extraction --> face comparison

#### Face feature extraction: (omitting previous steps: face detection (including facial landmarks) --> face alignment)
Model inference example: FeatureExtractionExample

#### Face feature comparison:
Face comparison example: FeatureComparisonExample


#### Run the Face Feature Extraction Example - FeatureExtractionExample
After successful execution, the command line should display the following information:
```text
[INFO ] - Face feature: [-0.04026184, -0.019486362, -0.09802659, 0.01700999, 0.037829027, ...]
```

#### Run the Face Feature Comparison Example - FeatureComparisonExample
 `src/test/resources/kana1.jpg`  
![kana1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/kana1.jpg)     
 `src/test/resources/kana2.jpg`  
![kana2](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/kana2.jpg)  

After successful execution, the command line should display the following information:
The comparison is based on the calculation of Euclidean distance.

```text
[INFO ] - face1 feature: [-0.040261842, -0.019486364, ..., 0.031147916, -0.032064643]
[INFO ] - face2 feature: [-0.049654193, -0.04029847, ..., 0.04562381, -0.044428844]
[INFO ] - 相似度： 0.9022608
```

### Open source algorithms
#### 1. Open source algorithms used by the SDK
- [insightface](https://github.com/deepinsight/insightface)

#### Pre-trained models used by the project:
- [model_zoo](https://github.com/deepinsight/insightface/tree/master/model_zoo)
- [iresnet50](https://paddle-model-ecology.bj.bcebos.com/model/insight-face/arcface_iresnet50_v1.0_infer.tar)


#### 2. How to export the model?
- [how_to_export_paddle_model](https://github.com/deepinsight/insightface/blob/master/recognition/arcface_paddle/tools/export.py)
