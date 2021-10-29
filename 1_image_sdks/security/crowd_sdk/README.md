# 人群密度检测 SDK
CrowdNet模型是2016年提出的人流密度估计模型，论文为《CrowdNet: A Deep Convolutional Network for DenseCrowd Counting》，
CrowdNet模型主要有深层卷积神经网络和浅层卷积神经组成，通过输入原始图像和高斯滤波器得到的密度图进行训练，最终得到的模型估计图像中的行人的数量。
当然这不仅仅可以用于人流密度估计，理论上其他的动物等等的密度估计应该也可以。

以下是CrowdNet模型的结构图，从结构图中可以看出，CrowdNet模型是深层卷积网络（Deep Network）和浅层卷积网络（Shallow Network）组成，
两组网络通过拼接成一个网络，接着输入到一个卷积核数量和大小都是1的卷积层，最后通过插值方式得到一个密度图数据，通过统计这个密度就可以得到估计人数。
![model](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/network.png)

#### sdk功能：
- 计算人数
- 计算密度图

## 运行例子 - CrowdDetectExample
- 测试图片
![crowd](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/crowd1.jpg)

例子代码：
```text
    Path imageFile = Paths.get("src/test/resources/crowd1.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, NDList> criteria = new CrowdDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, NDList> predictor = model.newPredictor()) {
      NDList list = predictor.predict(image);

      //quantity为人数
      float q = list.get(1).toFloatArray()[0];
      int quantity = (int)(Math.abs(q) + 0.5);
      logger.info("人数 quantity: {}", quantity);
      
      // density为密度图
      NDArray densityArray = list.get(0);
      logger.info("密度图 density: {}", densityArray.toDebugString(1000000000, 1000, 1000, 1000));
```


运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - 人数 quantity: 11

[INFO ] - 密度图 density: ND: (1, 1, 80, 60) cpu() float32
[  
   [ 4.56512964e-04,  2.19504116e-04,  3.44428350e-04,  ..., -1.44560239e-04,  1.58709008e-04],
   [ 9.59073077e-05,  2.53924576e-04,  2.51444580e-04,  ..., -1.64886122e-04,  1.14555296e-04],
   [ 6.42040512e-04,  5.44962648e-04,  4.95903892e-04,  ..., -1.15299714e-04,  3.01052118e-04],
   [ 1.58930803e-03,  1.43694575e-03,  7.95312808e-04,  ...,  1.44582940e-04,  4.20258410e-04],
    ....
   [ 2.21548311e-04,  2.92199198e-04,  3.05847381e-04,  ...,  6.77200791e-04,  2.88001203e-04],
   [ 5.04880096e-04,  2.36357562e-04,  1.90203893e-04,  ...,  8.42695648e-04,  2.92608514e-04],
   [ 1.45231024e-04,  1.56763941e-04,  2.12623156e-04,  ...,  4.69507067e-04,  1.36347953e-04],
   [ 5.02332812e-04,  2.98928004e-04,  3.34762561e-04,  ...,  4.80025599e-04,  2.72601028e-04],
]

```
#### 密度图
![density](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/density.png)


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   