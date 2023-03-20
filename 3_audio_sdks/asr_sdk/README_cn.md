### 官网：
[官网链接](http://www.aias.top/)


### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1HFpOECcBYbrQ1A9k_mqszQ?pwd=59e5

### 语音识别（ASR）【短语音】
语音识别（Automatic Speech Recognition）是以语音为研究对象，通过语音信号处理和模式识别让机器自动识别和理解人类口述的语。
语音识别技术就是让机器通过识别和理解过程把语音信号转变为相应的文本或命令的高技术。
语音识别是一门涉及面很广的交叉学科，它与声学、语音学、语言学、信息理论、模式识别理论以及神经生物学等学科都有非常密切的关系。

sdk基于DeepSpeech2模型实现中文语音识别，识别效果不错。
DeepSpeech2是基于PaddlePaddle实现的端到端自动语音识别（ASR）引擎。

- Deep Speech 2 论文 
[Deep Speech 2 : End-to-End Speech Recognition in English and Mandarin](http://proceedings.mlr.press/v48/amodei16.pdf)


#### 运行例子 - SpeechRecognitionExample
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - input audio: src/test/resources/test.wav
[INFO ] - Score : 91.685394
[INFO ] - Words : 近几年不但我用书给女儿压岁也劝说亲朋友不要给女儿压岁钱而改送压岁书
```

### 本例子VAD只支持Mac & Linux, VAD windows环境支持，请参考：
https://gitee.com/endlesshh/ttskit-java


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

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   


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