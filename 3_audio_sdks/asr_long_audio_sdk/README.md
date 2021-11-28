# 语音识别（ASR）【长语音】
语音识别（Automatic Speech Recognition）是以语音为研究对象，通过语音信号处理和模式识别让机器自动识别和理解人类口述的语。
语音识别技术就是让机器通过识别和理解过程把语音信号转变为相应的文本或命令的高技术。
语音识别是一门涉及面很广的交叉学科，它与声学、语音学、语言学、信息理论、模式识别理论以及神经生物学等学科都有非常密切的关系。

sdk基于DeepSpeech2模型实现中文语音识别，识别效果不错。
DeepSpeech2是基于PaddlePaddle实现的端到端自动语音识别（ASR）引擎。

在短语音识别的基础上增加了音频分割。使用了语音活动检测(VAD)检测静音。

- Deep Speech 2 论文 
[Deep Speech 2 : End-to-End Speech Recognition in English and Mandarin](http://proceedings.mlr.press/v48/amodei16.pdf)


### 运行例子 - SpeechRecognitionExampleL
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 第1个分割音频, 得分: 99.28923, 识别结果: 近几年不但我用输给女儿压岁
音频均方根能量: -30.505535
[INFO ] - 第2个分割音频, 得分: 88.94682, 识别结果: 劝说清朋不要给女儿压岁钱玩改送压岁书
[INFO ] - 最终识别结果:,近几年不但我用输给女儿压岁,劝说清朋不要给女儿压岁钱玩改送压岁书
```

### 参数设置
音频分割参数的设置，会影响检测结果的精度。所以请合理设置参数。
padding_duration_ms：300 
frame_duration_ms：30

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   

### jlibrosa 地址：
https://github.com/Subtitle-Synchronizer/jlibrosa
https://github.com/Subtitle-Synchronizer/jlibrosa/blob/master/binaries/jlibrosa-1.1.8-SNAPSHOT-jar-with-dependencies.jar