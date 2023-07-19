### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1pGpTprOOukl7Kf0QdNma0w?pwd=syp9

### 语音识别（ASR）【长语音】
语音识别（Automatic Speech Recognition）是以语音为研究对象，通过语音信号处理和模式识别让机器自动识别和理解人类口述的语。
语音识别技术就是让机器通过识别和理解过程把语音信号转变为相应的文本或命令的高技术。
语音识别是一门涉及面很广的交叉学科，它与声学、语音学、语言学、信息理论、模式识别理论以及神经生物学等学科都有非常密切的关系。

sdk基于DeepSpeech2模型实现中文语音识别，识别效果不错。
DeepSpeech2是基于PaddlePaddle实现的端到端自动语音识别（ASR）引擎。

在短语音识别的基础上增加了音频分割。使用了语音活动检测(VAD)检测静音。

- Deep Speech 2 论文 
[Deep Speech 2 : End-to-End Speech Recognition in English and Mandarin](http://proceedings.mlr.press/v48/amodei16.pdf)


#### 运行例子 - SpeechRecognitionExampleL
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 第1个分割音频, 得分: 99.28923, 识别结果: 近几年不但我用输给女儿压岁
音频均方根能量: -30.505535
[INFO ] - 第2个分割音频, 得分: 88.94682, 识别结果: 劝说清朋不要给女儿压岁钱玩改送压岁书
[INFO ] - 最终识别结果:,近几年不但我用输给女儿压岁,劝说清朋不要给女儿压岁钱玩改送压岁书
```

#### 1. 采样率
采样率(samplerate)为 16000 Hz，表示每秒 16000 个采样点
##### MP3
mp3 每帧均为1152个字节(一个字节对应一个采样点)， 则：
每帧播放时长 = 1152 * 1000 / sample_rate
例如：sample_rate = 44100HZ 时，
计算出的时长为： 1152 * 1000 / 44100 = 26.122ms，
这就是经常听到的mp3每帧播放时间固定为26ms的由来。

#### 2. 量化精度（位宽）
量化精度，又叫量化深度，上图中，每一个红色的采样点，都需要用一个数值来表示大小，这个数值的数据类型大小可以是：4bit、8bit、16bit、32bit等等，位数越多，表示得就越精细，声音质量自然就越好，当然，数据量也会成倍增大。
常见的位宽是：8bit 或者 16bit

#### 3. 声道数（channels）
由于音频的采集和播放是可以叠加的，因此，可以同时从多个音频源采集声音，并分别输出到不同的扬声器，故声道数一般表示声音录制时的音源数量或回放时相应的扬声器数量。
单声道（Mono）和双声道（Stereo）比较常见，顾名思义，前者的声道数为1，后者为2

#### 4. 音频帧（frame）
是用于测量显示帧数的度量。所谓的测量单位为每秒显示帧数(Frames per Second，简称：FPS）或“赫兹”（Hz）。
音频跟视频很不一样，视频每一帧就是一张图像，而从上面的正玄波可以看出，音频数据是流式的，本身没有明确的一帧帧的概念，在实际的应用中，为了音频算法处理/传输的方便，一般约定俗成取2.5ms~60ms为单位的数据量为一帧音频。
这个时间被称之为“采样时间”，其长度没有特别的标准，它是根据具体应用的需求来决定的，我们可以计算一下一帧音频帧的大小：
假设某通道的音频信号是采样率为8kHz，位宽为16bit，20ms一帧，双通道，则一帧音频数据的大小为：
int size = 8000 x 16bit x 0.02s x 2 = 5120 bit = 640 byte

#### 5. 常见的音频编码方式有哪些？
模拟的音频信号转换为数字信号需要经过采样和量化，量化的过程被称之为编码，根据不同的量化策略，产生了许多不同的编码方式，常见的编码方式有：PCM 和 ADPCM，这些数据代表着无损的原始数字音频信号，添加一些文件头信息，就可以存储为WAV文件了，它是一种由微软和IBM联合开发的用于音频数字存储的标准，可以很容易地被解析和播放。

#### 6. 常见的音频压缩格式有哪些？
首先简单介绍一下音频数据压缩的最基本的原理：因为有冗余信息，所以可以压缩。
（1） 频谱掩蔽效应： 人耳所能察觉的声音信号的频率范围为20Hz～20KHz，在这个频率范围以外的音频信号属于冗余信号。
（2） 时域掩蔽效应： 当强音信号和弱音信号同时出现时，弱信号会听不到，因此，弱音信号也属于冗余信号。
下面简单列出常见的音频压缩格式：
MP3，AAC，OGG，WMA，Opus，FLAC，APE，m4a，AMR，等等。

#### 参数设置
音频分割参数的设置，会影响检测结果的精度。所以请合理设置参数。
padding_duration_ms：300 
frame_duration_ms：30


#### 帮助
##### 共享库文件:
- linux: vad4j_sdk/lib/linux
- -libfvad.so
- -libwebrtcvadwrapper.so
- windows: vad4j_sdk/lib/windows
- -libfvad.dll
- -libwebrtcvadwrapper.dll

##### linux/mac 设置环境变量
- 共享库文件需添加到 java.library.path
  LD_LIBRARY_PATH to /path/to/shared/libraries:$LD_LIBRARY_PATH.

##### windows 设置环境变量
- 共享库文件需添加到 PATH

##### 音频数据
输入的数据需是16-bit PCM audio数据，详细信息请参考下面的链接：
https://github.com/jitsi/jitsi-webrtc-vad-wrapper/blob/master/readme.md


### 开源算法
#### 1. sdk使用的开源算法
- [PaddlePaddle-DeepSpeech](https://github.com/yeyupiaoling/PaddlePaddle-DeepSpeech)
#### 2. 模型如何导出 ?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
- export_model.py
```text
import argparse
import functools
import paddle
from model_utils.model import DeepSpeech2Model
from utils.utility import add_arguments

parser = argparse.ArgumentParser(description=__doc__)
add_arg = functools.partial(add_arguments, argparser=parser)
add_arg('num_conv_layers',  int,    2,      "卷积层数量")
add_arg('num_rnn_layers',   int,    3,      "循环神经网络的数量")
add_arg('rnn_layer_size',   int,    1024,   "循环神经网络的大小")
add_arg('use_gpu',          bool,   False,   "是否使用GPU加载模型")
add_arg('vocab_path',       str,    './dataset/zh_vocab.txt',     "数据集的词汇表文件路径")
add_arg('resume_model',     str,    './models/param/50.pdparams', "恢复模型文件路径")
add_arg('save_model_path',  str,    './models/infer/',            "保存导出的预测模型文件夹路径")
args = parser.parse_args()

# 是否使用GPU
place = paddle.CUDAPlace(0) if args.use_gpu else paddle.CPUPlace()

with open(args.vocab_path, 'r', encoding='utf-8') as f:
    vocab_size = len(f.readlines())

# 获取DeepSpeech2模型，并设置为预测
ds2_model = DeepSpeech2Model(vocab_size=vocab_size,
                             num_conv_layers=args.num_conv_layers,
                             num_rnn_layers=args.num_rnn_layers,
                             rnn_layer_size=args.rnn_layer_size,
                             resume_model=args.resume_model,
                             place=place)

ds2_model.export_model(model_path=args.save_model_path)
print('成功导出模型，模型保存在：%s' % args.save_model_path)

```
- export_model_1300.py
```text
import argparse
import functools
import paddle
from model_utils.model import DeepSpeech2Model
from utils.utility import add_arguments

parser = argparse.ArgumentParser(description=__doc__)
add_arg = functools.partial(add_arguments, argparser=parser)
add_arg('num_conv_layers',  int,    2,      "卷积层数量")
add_arg('num_rnn_layers',   int,    3,      "循环神经网络的数量")
add_arg('rnn_layer_size',   int,    1024,   "循环神经网络的大小")
add_arg('use_gpu',          bool,   False,   "是否使用GPU加载模型")
add_arg('vocab_path',       str,    './models/DeepSpeech-1300/dataset/zh_vocab.txt',     "数据集的词汇表文件路径")
add_arg('resume_model',     str,    './models/DeepSpeech-1300/models/step_final/params.pdparams', "恢复模型文件路径")
add_arg('save_model_path',  str,    './models/infer-1300/',            "保存导出的预测模型文件夹路径")
args = parser.parse_args()

# 是否使用GPU
place = paddle.CUDAPlace(0) if args.use_gpu else paddle.CPUPlace()

with open(args.vocab_path, 'r', encoding='utf-8') as f:
    vocab_size = len(f.readlines())

# 获取DeepSpeech2模型，并设置为预测
ds2_model = DeepSpeech2Model(vocab_size=vocab_size,
                             num_conv_layers=args.num_conv_layers,
                             num_rnn_layers=args.num_rnn_layers,
                             rnn_layer_size=args.rnn_layer_size,
                             resume_model=args.resume_model,
                             place=place)

ds2_model.export_model(model_path=args.save_model_path)
print('成功导出模型，模型保存在：%s' % args.save_model_path)


```

### 其它帮助信息
http://aias.top/guides.html



#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html


### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   

### jlibrosa 地址：
https://github.com/Subtitle-Synchronizer/jlibrosa
https://github.com/Subtitle-Synchronizer/jlibrosa/blob/master/binaries/jlibrosa-1.1.8-SNAPSHOT-jar-with-dependencies.jar