### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1P3hfmGMHL56ENYLpGShYNg?pwd=wf6s

### 超分辨(4倍)SDK
提升图片4倍分辨率。


### 运行例子 - SuperResolutionExample
- 测试图片（左侧原图，右侧效果）
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/super_resolution_sdk/stitch0.png)

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Images generated: 1
```

### 开源算法
#### 1. sdk使用的开源算法
- [Real-ESRGAN](https://github.com/xinntao/Real-ESRGAN)

#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)


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