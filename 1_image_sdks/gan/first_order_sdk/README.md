#动作驱动 SDK
#不得用于非法用途！
sdk以一段动作视频去驱动一张图片运动。可以驱动任意类型的运动，如：人脸、全身运动，动画运动的驱动。
不需要先验地知道目标的一些信息，比如骨架等。能够通过自监督的方式学习到图像中的关键点。
且该方法基于运动模型推导而出，在形式上也具有良好的可解释性。

### 原图：
![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/beauty.jpg)

### 驱动视频：
[driver](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/driver.mp4)

### 生成视频：运行 FirstOrderExample
生成mp4保存于
```
build/output/result.mp4
```

[mp4](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/result.mp4)

### 生成Gif：运行 Mp4ToGif
录制gif保存于
```
build/output/result.gif
```

![gif](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/result.gif)

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)  

Reference：
https://gitee.com/endlesshh/first-order-model-java 