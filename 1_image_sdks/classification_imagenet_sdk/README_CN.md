## 目录：
http://aias.top/


### 图片分类(支持imagenet数据集分类)SDK
识别图片1000种分类。

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/16EQetyebbh5JfuGLMVNcCg?pwd=h854

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
-  Darknet53Classification   
backbone: darknet53, dataset:imagenet
-  MobilenetClassification   
backbone: Mobilenetv3_small, dataset:imagenet

## 运行例子 - Darknet53ClassificationExample
- 测试图片
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/classification_imagenet_sdk/cup.jpeg)

运行成功后，命令行应该看到下面的信息:
```text
...
n03063599 coffee mug : 0.39411163330078125
[INFO ] - [
	class: "n03063599 coffee mug", probability: 0.39411
	class: "n07930864 cup", probability: 0.21575
	class: "n07920052 espresso", probability: 0.03084
	class: "n07932039 eggnog", probability: 0.02415
	class: "n04579145 whiskey jug", probability: 0.01683
]
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   