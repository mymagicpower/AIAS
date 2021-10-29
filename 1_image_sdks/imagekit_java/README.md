## 目录：
http://aias.top/

# 图像预处理SDK
在OCR文字识别的时候，我们得到的图像一般情况下都不是正的，多少都会有一定的倾斜。
所以需要将图片转正。并且图片有可能是透视视角拍摄，需要重新矫正。

### SDK功能
-图像转正
-图像二值化，灰度化，去燥等经典算法。

### 完善中的功能：
-完善透视矫正
-完善文字方向检测算法，判断转正后的图片角度，以便进一步旋转图片使得文字水平。

## 运行例子
运行成功后，命令行应该看到下面的信息:
```text
319.0 , 865.0
319.0 , 113.0
785.0 , 113.0
785.0 , 865.0
startLeft = 319
startUp = 113
width = 467
height = 753
```
输出图片效果如下：
![ocr_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_sdk/images/rotation.png)


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   