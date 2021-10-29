# 声纹识别
所谓声纹(Voiceprint)，是用电声学仪器显示的携带言语信息的声波频谱。人类语言的产生是人体语言中枢与发音器官之间一个复杂的生理物理过程，
人在讲话时使用的发声器官--舌、牙齿、喉头、肺、鼻腔在尺寸和形态方面每个人的差异很大，所以任何两个人的声纹图谱都有差异。
声纹识别(Voiceprint Recognition, VPR)，也称为说话人识别(Speaker Recognition)，有两类，
即说话人辨认(Speaker Identification)和说话人确认(Speaker Verification)。前者用以判断某段语音是若干人中的哪一个所说的，
是“多选一”问题；而后者用以确认某段语音是否是指定的某个人所说的，是“一对一判别”问题。不同的任务和应用会使用不同的声纹识别技术，
如缩小刑侦范围时可能需要辨认技术，而银行交易时则需要确认技术。不管是辨认还是确认，都需要先对说话人的声纹进行建模，这就是所谓的“训练”或“学习”过程。

sdk基于PaddlePaddle实现声纹识别模型。使用的是中文语音语料数据集，这个数据集一共有3242个人的语音数据，有1130000+条语音数据。

#### SDK包含的功能
- 声纹特征向量提取
- 声纹相似度计算

#### 运行例子 - VoiceprintExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 音频文件 a_1.wav, a_2.wav是同一个人
[INFO ] - input audio: src/test/resources/a_1.wav
[INFO ] - input audio: src/test/resources/a_2.wav
[INFO ] - input audio: src/test/resources/b_1.wav

# 声纹 512维特征向量
[INFO ] - a_1.wav feature: [-0.24602059, 0.20456463, -0.306607, ..., 0.016211584, 0.108457334]
[INFO ] - a_2.wav feature: [-0.115257666, 0.18287876, -0.45560476, ..., 0.15607461, 0.12677354]
[INFO ] - b_1.wav feature: [-0.009925389, -0.02331138, 0.18817122, ..., 0.058160514, -0.041663148]

# 相似度计算
[INFO ] - a_1.wav,a_2.wav 相似度： 0.9165065
[INFO ] - a_1.wav,b_1.wav 相似度： 0.024052326
```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   