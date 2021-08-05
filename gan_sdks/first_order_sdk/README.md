#动作驱动 SDK
sdk以一段动作视频去驱动一张图片运动。可以驱动任意类型的运动，如：人脸、全身运动，动画运动的驱动。
不需要先验地知道目标的一些信息，比如骨架等。能够通过自监督的方式学习到图像中的关键点。
且该方法基于运动模型推导而出，在形式上也具有良好的可解释性。

### 原图：
![image](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/gan_sdks/beauty.jpg)

### 驱动视频：
[driver](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/gan_sdks/driver.mp4)

### 生成视频：运行 FirstOrderExample
生成mp4保存于
```
build/output/result.mp4
```

[mp4](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/gan_sdks/result.mp4)

### 生成Gif：运行 Mp4ToGif
录制gif保存于
```
build/output/result.gif
```

![gif](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/gan_sdks/result.gif)

帮助
添加依赖库：lib/aias-first-order-lib-0.1.0.jar