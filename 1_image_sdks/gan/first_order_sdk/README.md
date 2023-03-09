### 官网：
[官网链接](https://www.aias.top/)

### 动作驱动 SDK
### 不得用于非法用途！
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


### 开源算法
#### 1. sdk使用的开源算法
- [first-order-model](https://github.com/AliaksandrSiarohin/first-order-model)
- [预训练模型](https://drive.google.com/open?id=1PyQJmkdCsAkOYwUyaj_l-l0as-iLDgeH)


#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)
- [参考项目](https://gitee.com/endlesshh/first-order-model-java)    
- [为了适配DJL修改了模型参数](https://gitee.com/endlesshh/fisrt-order-model)


### 其它帮助信息
https://aias.top/guides.html


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)  

### Reference：
https://gitee.com/endlesshh/first-order-model-java 


#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html