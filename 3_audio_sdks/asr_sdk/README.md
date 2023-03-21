
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/deep_speech.zip

### Speech Recognition (ASR) [Short Speech]
Speech recognition (Automatic Speech Recognition) is an advanced technology that focuses on processing and identifying human speech through speech signal processing and pattern recognition, which automatically recognizes and understands human oral language and converts it into corresponding texts or commands. Speech recognition is a cross-disciplinary field that has close relationships with acoustics, phonetics, linguistics, information theory, pattern recognition theory, and neuroscience.

The SDK is based on DeepSpeech2 model for Chinese speech recognition, and the recognition effect is good. DeepSpeech2 is an end-to-end automatic speech recognition (ASR) engine based on PaddlePaddle.

Audio segmentation is added on the basis of short speech recognition. It uses voice activity detection (VAD) to detect silence.

- Deep Speech 2 Paper
  [Deep Speech 2 : End-to-End Speech Recognition in English and Mandarin](http://proceedings.mlr.press/v48/amodei16.pdf)


#### Run example  - SpeechRecognitionExample
After successful operation, the command line should see the following information:
```text
...
[INFO ] - input audio: src/test/resources/test.wav
[INFO ] - Score : 91.685394
[INFO ] - Words : 近几年不但我用书给女儿压岁也劝说亲朋友不要给女儿压岁钱而改送压岁书
```

####This example VAD only supports Mac & Linux, VAD Windows environment support, please refer to:
https://gitee.com/endlesshh/ttskit-java


### Open Source Algorithm
#### 1. The open source algorithm used by the SDK
- [PaddlePaddle-DeepSpeech](https://github.com/yeyupiaoling/PaddlePaddle-DeepSpeech)
#### 2. How to export the model?
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
