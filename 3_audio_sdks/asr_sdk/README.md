# 语音识别（ASR）【短语音】
语音识别（Automatic Speech Recognition）是以语音为研究对象，通过语音信号处理和模式识别让机器自动识别和理解人类口述的语。
语音识别技术就是让机器通过识别和理解过程把语音信号转变为相应的文本或命令的高技术。
语音识别是一门涉及面很广的交叉学科，它与声学、语音学、语言学、信息理论、模式识别理论以及神经生物学等学科都有非常密切的关系。

sdk基于DeepSpeech2模型实现中文语音识别，识别效果不错。
DeepSpeech2是基于PaddlePaddle实现的端到端自动语音识别（ASR）引擎。

- Deep Speech 2 论文 
[Deep Speech 2 : End-to-End Speech Recognition in English and Mandarin](http://proceedings.mlr.press/v48/amodei16.pdf)


## 运行例子 - SpeechRecognitionExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input audio: src/test/resources/test.wav
[INFO ] - Score : 91.685394
[INFO ] - Words : 近几年不但我用书给女儿压岁也劝说亲朋友不要给女儿压岁钱而改送压岁书
```


### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   