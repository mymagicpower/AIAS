
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/retinaface.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/ultranet.zip
- 
### Face Alignment SDK
This article provides a reference implementation for face alignment.
       
#### Face Recognition
Broadly speaking, face recognition includes a series of related technologies for building face recognition systems, including face image acquisition, face location, face recognition preprocessing, identity confirmation, and identity search. Face recognition is a popular research field in computer technology. It belongs to biometric recognition technology and distinguishes biological individuals based on the biological characteristics of the organism itself (generally referring to humans). The biological characteristics studied by biometric recognition technology include face, fingerprint, palm print, iris, retina, sound (voice), body shape, personal habits (such as the strength and frequency of keyboard typing, signature), etc. The corresponding recognition technologies include face recognition, fingerprint recognition, palm print recognition, iris recognition, retina recognition, speech recognition (speech recognition can be used for identity recognition or speech content recognition, only the former belongs to biometric recognition technology), body shape recognition, keyboard typing recognition, signature recognition, etc.

#### Industry Status
Face recognition technology has been widely used in various industries, including face access control systems, face payment, etc. With the improvement of face recognition technology, its applications are becoming more and more extensive. Currently, China's face recognition technology is at the leading level in the world. In the security industry, domestic mainstream security manufacturers have also launched their own face recognition products and solutions, and the security industry is the main application area of face recognition technology.

#### Technical Development Trends
Currently, the face recognition technology widely used is based on a neural network deep learning model. The use of face features extracted by deep learning can extract more features and better express the correlation between faces than traditional technology, significantly improving the accuracy of the algorithm. In recent years, big data technology and computing power have been greatly improved, and deep learning is very dependent on big data and computing power, which is why this technology has made breakthroughs in recent years. More and richer data added to the training model means that the algorithm model is more universal and closer to the real world. On the other hand, the improvement of computing power enables the model to have deeper hierarchical structures, and the theoretical model of deep learning itself is also constantly improving, which will greatly improve the level of face recognition technology.

#### Key Technologies for Face Recognition
The key technologies involved in face recognition include: face detection, face key points, face feature extraction, face comparison, and face alignment.
![face_sdk](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sdk.png)

#### Implementation of Face Alignment:
Model Inference Example Code: FaceAlignExample.java

#### Run the Face Detection Example
1. After successful operation, the output image effect is as follows:
![face_align](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_align.png)


### Open Source Algorithms
#### 1. Open-source algorithms used in SDK
- [RetinaFaceDetection - Pytorch_Retinaface](https://github.com/biubug6/Pytorch_Retinaface)
- [LightFaceDetection - Ultra-Light-Fast-Generic-Face-Detector-1MB](https://github.com/Linzaer/Ultra-Light-Fast-Generic-Face-Detector-1MB)


#### 2. How to Export Models?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

