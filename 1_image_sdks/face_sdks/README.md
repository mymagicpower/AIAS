#### 人脸工具箱 SDK

### 官网：
[官网链接](http://www.aias.top/)

#### 人脸识别
广义的人脸识别实际包括构建人脸识别系统的一系列相关技术，包括人脸图像采集、人脸定位、人脸识别预处理、身份确认以及身份查找等；
而狭义的人脸识别特指通过人脸进行身份确认或者身份查找的技术或系统。
人脸识别是一项热门的计算机技术研究领域，它属于生物特征识别技术，是对生物体（一般特指人）本身的生物特征来区分生物体个体。
生物特征识别技术所研究的生物特征包括脸、指纹、手掌纹、虹膜、视网膜、声音（语音）、体形、个人习惯（例如敲击键盘的力度和频率、签字）等，
相应的识别技术就有人脸识别、指纹识别、掌纹识别、虹膜识别、视网膜识别、语音识别（用语音识别可以进行身份识别，也可以进行语音内容的识别，
只有前者属于生物特征识别技术）、体形识别、键盘敲击识别、签字识别等。

#### 行业现状
人脸识别技术目前已经广泛应用于包括人脸门禁系统、刷脸支付等各行各业。随着人脸识别技术的提升，应用越来越广泛。目前中国的人脸识
别技术已经在世界水平上处于领先地位，在安防行业，国内主流安防厂家也都推出了各自的人脸识别产品和解决方案，泛安防行业是人脸识别技术主要应用领域。

#### 技术发展趋势
目前人脸识别技术广泛采用的是基于神经网络的深度学习模型。利用深度学习提取出的人脸特征，相比于传统技术，能够提取更多的特征，
更能表达人脸之间的相关性，能够显著提高算法的精度。近些年大数据技术以及算力都得到了大幅提升，而深度学习非常依赖于大数据与算力，
这也是为什么这项技术在近几年取得突破的原因。更多更丰富的数据加入到训练模型中，意味着算法模型更加通用，更贴近现实世界。另一方面，算力的提升，
使得模型可以有更深的层级结构，同时深度学习的理论模型本身也在不断的完善中，模型本身的优化将会极大地提高人脸识别的技术水平。

#### 人脸识别关键技术
人脸识别涉及的关键技术包含：人脸检测，人脸关键点，人脸特征提取，人脸比对，人脸对齐。
![face_sdk](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sdk.png)

### 1. 人脸检测(含5个人脸关键点)SDK - face_detection_sdk
人脸检测(含5个人脸关键点)提供了两个模型的实现：
#### 小模型: 
模型推理例子代码: LightFaceDetectionExample.java 

#### 大模型: 
模型推理例子代码: RetinaFaceDetectionExample.java 

#### 运行人脸检测的例子：
1. 运行成功后，命令行应该看到下面的信息:
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
2. 输出图片效果如下：
![detected-faces](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/retinaface_detected.png)

### 2.  人脸对齐 SDK - face_alignment_sdk
#### 运行人脸检测的例子 FaceAlignExample.java
运行成功后，输出图片效果如下：
![face_align](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_align.png)

### 3.  人脸特征提取与比对SDK - face_feature_sdk
#### 3.1 人脸特征提取 - FeatureExtractionExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Face feature: [-0.04026184, -0.019486362, -0.09802659, 0.01700999, 0.037829027, ...]
```

##### 3.2 人脸特征比对 - FeatureComparisonExample
人脸识别完整的pipeline：人脸检测(含人脸关键点) --> 人脸对齐 --> 人脸特征提取 --> 人脸比对
- 首先检测人脸   
- 然后根据人脸关键点转正对齐   
- 提取特征比较相似度   
![face_feature](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_feature.png)

运行成功后，命令行应该看到下面的信息:  
比对使用的是欧式距离的计算方式。

```text
[INFO ] - face1 feature: [0.19923544, 0.2091935, -0.17899065, ..., 0.7100589, -0.27192503, 1.1901716]
[INFO ] - face2 feature: [0.1881579, -0.40177754, -0.19950306, ..., -0.71886086, 0.31257823, -0.009294844]
[INFO ] - kana1 - kana2 Similarity： 0.68710256
```
提取特征计算相似度。可以进一步对112 * 112 人脸图片按比例裁剪，去除冗余信息，比如头发等，以提高精度。
如果图片模糊，可以结合人脸超分辨模型使用。

### 4.  人脸分辨率提升SDK - face_sr_sdk
#### 4.1 单张人脸图片超分辨 - GFPExample
- 测试图片（左侧原图，右侧效果）
![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/single_face_sr.png)

#### 4.2 多人脸超分辨（自动检测人脸，然后对齐人脸后提升分辨率 - FaceSrExample
- 自动检测人脸及关键地，然后抠图，然后根据人脸关键点转正对齐
![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_det.png)

- 对所有转正对齐的人脸提升分辨率
![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sr.png)


### 5. 图片人脸修复 SDK - face_restoration_sdk
- 自动检测人脸及关键地，然后抠图，然后根据人脸关键点转正对齐。
- 对所有转正对齐的人脸提升分辨率。
- 使用分割模型提取人脸，逆向变换后贴回原图。

#### 运行例子 - FaceRestorationExample
- 测试图片
  ![face_sr](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_res.png)

