# AIAS & DJL Android 例子
在这个例子里，你可以学习到如何实现pytorch模型的推理，用于检测图片中的人脸。
优点：
- 使用的DJL引擎可以直接将服务器端的模型部署在android环境,不需要转换模型，适配算子等。
- 支持主流框架：mxnet,pytorch,tensorflow,tflite。
- 适合用于非实时的场景（比如：每隔10分钟检测一下车辆违停等），部署成本小。

缺点：
- 无法用于实时场景

例子引用了一个轻量的模型：
[Ultra-Light-Fast-Generic-Face-Detector-1MB](https://github.com/Linzaer/Ultra-Light-Fast-Generic-Face-Detector-1MB).

## 人脸检测

Sample 1                     |  Sample 2                     
:-------------------------:|:-------------------------:
![face1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/edge_computing/face_1.jpeg)        |  ![face2](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/edge_computing/face_2.jpeg)         


The minimum API level: 26.

### 构建项目

使用android studio，或者使用下面的命令安装app：

```
cd face_detection

# for Linux/macOS
./gradlew iD

# for Windows
..\gradlew iD
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
