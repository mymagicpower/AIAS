### 官网：
[官网链接](http://www.aias.top/)


#### 语音活动检测(Voice Activity Detection,VAD)
语音活动检测(Voice Activity Detection,VAD)又称语音端点检测,语音边界检测。目的是从声音信号流里识别和消除长时间的静音期， 静音抑制可以节省宝贵的带宽资源，可以有利于减少用户感觉到的端到端的时延。

#### 关键特性
- 快速
  单个CPU线程可以在不到1ms的时间内处理一个音频块（30+ ms）[参见](https://github.com/snakers4/silero-vad/wiki/Performance-Metrics#silero-vad-performance-metrics)。使用批处理或GPU也可以显著提高性能。在某些条件下，ONNX甚至可以运行得更快4-5倍。

- 轻量级
  JIT模型的大小约为1MB。

- 通用
  Silero VAD在包括100多种语言的大型语料库上进行训练，并且在具有各种背景噪声和质量水平的不同领域的音频上表现良好。

- 灵活的采样率
  Silero VAD支持8000 Hz和16000 Hz的采样率。


<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/silero.png"  width = "600"/>
</div> 


### sdk使用的开源项目
https://github.com/snakers4/silero-vad