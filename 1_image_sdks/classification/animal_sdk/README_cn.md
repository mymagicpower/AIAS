### 官网：
[官网链接](https://www.aias.top/)

### 下载模型
- 链接: https://pan.baidu.com/s/1LLwbo3Wvu96c1lID4drgoQ?pwd=41mj

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

### 动物分类识别SDK
动物识别sdk，支持7978种动物的分类识别。

### SDK功能
- 支持7978种动物的分类识别，并给出置信度。
- 提供两个可用模型例子
1). 大模型(resnet50)例子：AnimalsClassificationExample
2). 小模型(mobilenet_v2)例子：LightAnimalsClassExample

[动物分类](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/animals.txt)

### 运行例子
- 测试图片
![tiger](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/tiger.jpeg)

运行成功后，命令行应该看到下面的信息:
```text
西伯利亚虎 : 1.0
[INFO ] - [
	class: "西伯利亚虎", probability: 1.00000
	class: "孟加拉虎", probability: 0.02022
	class: "华南虎", probability: 0.00948
	class: "苏门答腊虎", probability: 0.00397
	class: "印度支那虎", probability: 0.00279
]
```


### 开源算法
#### 1. sdk使用的开源算法
- [PaddleClas](https://github.com/PaddlePaddle/PaddleClas)
#### 2. 模型如何导出 ?
- [export_model](https://github.com/PaddlePaddle/PaddleClas/blob/release%2F2.2/tools/export_model.py)    
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)   



### 其它帮助信息 
https://aias.top/guides.html

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   



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