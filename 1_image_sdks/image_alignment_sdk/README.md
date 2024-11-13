### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1LrBs49Y32vU5Z71_E-qwIA?pwd=cfsc

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 图像矫正
在OCR文字识别的时候，我们得到的图像一般情况下都不是正的，多少都会有一定的倾斜。 并且图片有可能是透视视角拍摄，需要重新矫正，尤其此时，将图片转正可以提高文字识别的精度。
智能图片裁剪框架。自动识别边框，手动调节选区，使用透视变换裁剪并矫正选区；适用于身份证，名片，文档等照片的裁剪。
- 直线检测在好多实现应用中能用到到，比如文档扫描，辅助驾驶中的车道线检测，传统的算法用的最多应该属于霍夫曼直线检测，但传统算法都有一个痛苦的调参过程和只能对优化过的使用场景有较好的结果，换个场景可能就要重新调参。
- MLSD是一种面向实时和轻量级的线段检测深度学习算法，论文地址：https://arxiv.org/abs/210600186，
相对于传统算法，MLSD只是在训练模型的层面上会比较麻烦，在实现检测并不用去关心各种参数就能达到很好的效果。


#### 图像矫正java实现（自动检测边缘，透视变换转正）
图像转正提供了两个模型，根据需要使用。
![MlsdSquareExample](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/MlsdSquareExample.jpeg)



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