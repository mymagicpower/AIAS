## 目录：
http://aias.top/


# BIGGAN 图像自动生成SDK
能够自动生成1000种类别（支持imagenet数据集分类）的图片。

### 支持分类如下：
-  tench, Tinca tinca
-  goldfish, Carassius auratus
-  great white shark, white shark, man-eater, man-eating shark, Carcharodon carcharias
-  tiger shark, Galeocerdo cuvieri
-  hammerhead, hammerhead shark
-  electric ray, crampfish, numbfish, torpedo
-  stingray
-  cock
-  hen
-  ostrich, Struthio camelus
-  brambling, Fringilla montifringilla
-  goldfinch, Carduelis carduelis
-  house finch, linnet, Carpodacus mexicanus
-  junco, snowbird
-  indigo bunting, indigo finch, indigo bird, Passerina cyanea
-  robin, American robin, Turdus migratorius
- ...

[点击下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/classification_imagenet_sdk/synset.txt)

### SDK包含两个分类器：
size 支持 128, 256, 512三种图片尺寸
如：size = 512;
imageClass 支持imagenet类别0~999
如：imageClass = 156;

## 运行例子 - BigGAN
- 测试图片类别11，图片尺寸：512X512
![img1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image11.png)

- 测试图片类别156，图片尺寸：512X512
![img2](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image156.png)

- 测试图片类别821，图片尺寸：512X512
![img3](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image821.png)

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Number of inter-op threads is 4
[INFO ] - Number of intra-op threads is 4
[INFO ] - Generated image has been saved in: build/output/
```
Pytorch模型代码：
https://github.com/mymagicpower/BigGAN-Generator-Pretrained-Pytorch

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   