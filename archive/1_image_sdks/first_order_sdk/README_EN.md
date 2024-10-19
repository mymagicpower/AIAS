
### Download the model and place it in the /models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/generator.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/kpdetector.zip
- 
### Action-Driven SDK
The SDK drives the movement of a single image using a video of a person performing a specific action. It can drive any type of motion, such as facial or body movement, animation, etc. It does not require any prior knowledge of the target, such as a skeleton. The algorithm can learn key points in the image through self-supervision. Furthermore, this method is derived from motion models and has good interpretability.

### Original image:
![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/beauty.jpg)

### Driving video: driver.mp4

### Generated video: Run FirstOrderExample
The generated mp4 is saved at
```
build/output/result.mp4
```

### Generated Gif: Run Mp4ToGif
The recorded gif is saved at
```
build/output/result.gif
```

![gif](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/result.gif)


### Open-source Algorithms
#### 1. Open-source algorithms used by the SDK
- [first-order-model](https://github.com/AliaksandrSiarohin/first-order-model)
- [Pre-trained model](https://drive.google.com/open?id=1PyQJmkdCsAkOYwUyaj_l-l0as-iLDgeH)


#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

