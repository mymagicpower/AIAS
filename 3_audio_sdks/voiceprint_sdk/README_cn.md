### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1DTdghpqs4xK1IZRdEXTuiA?pwd=3q7m

### 声纹识别
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


### 开源算法
#### 1. sdk使用的开源算法
- [VoiceprintRecognition-PaddlePaddle](https://github.com/yeyupiaoling/VoiceprintRecognition-PaddlePaddle)
#### 2. 模型如何导出 ?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
- 导出模型
- export_model.py
```text
import argparse
import functools
import os
import shutil
import time
from datetime import datetime, timedelta

import paddle
import paddle.distributed as dist
from paddle.io import DataLoader
from paddle.metric import accuracy
from paddle.static import InputSpec
from visualdl import LogWriter
from utils.resnet import resnet34
from utils.metrics import ArcNet
from utils.reader import CustomDataset
from utils.utility import add_arguments, print_arguments

parser = argparse.ArgumentParser(description=__doc__)
add_arg = functools.partial(add_arguments, argparser=parser)
add_arg('gpus',             str,    '0',                      '训练使用的GPU序号，使用英文逗号,隔开，如：0,1')
add_arg('batch_size',       int,    32,                       '训练的批量大小')
add_arg('num_workers',      int,    4,                        '读取数据的线程数量')
add_arg('num_epoch',        int,    50,                       '训练的轮数')
add_arg('num_classes',      int,    3242,                     '分类的类别数量')
add_arg('learning_rate',    float,  1e-3,                     '初始学习率的大小')
add_arg('input_shape',      str,    '(None, 1, 257, 257)',    '数据输入的形状')
add_arg('train_list_path',  str,    'dataset/train_list.txt', '训练数据的数据列表路径')
add_arg('test_list_path',   str,    'dataset/test_list.txt',  '测试数据的数据列表路径')
add_arg('save_model',       str,    'models/',                '模型保存的路径')
add_arg('resume',           str,    None,                     '恢复训练，当为None则不使用恢复模型')
add_arg('pretrained_model', str,    None,                     '预训练模型的路径，当为None则不使用预训练模型')
args = parser.parse_args()


# 评估模型
@paddle.no_grad()
def test(model, metric_fc, test_loader):
    model.eval()
    accuracies = []
    for batch_id, (spec_mag, label) in enumerate(test_loader()):
        feature = model(spec_mag)
        output = metric_fc(feature, label)
        label = paddle.reshape(label, shape=(-1, 1))
        acc = accuracy(input=output, label=label)
        accuracies.append(acc.numpy()[0])
    model.train()
    return float(sum(accuracies) / len(accuracies))


# 保存模型
def save_model(args,model):
    input_shape = eval(args.input_shape)
    # 保存预测模型
    if not os.path.exists(os.path.join(args.save_model, 'infer')):
        os.makedirs(os.path.join(args.save_model, 'infer'))
    paddle.jit.save(layer=model,
                    path=os.path.join(args.save_model, 'infer/model'),
                    input_spec=[InputSpec(shape=[input_shape[0], input_shape[1], input_shape[2], input_shape[3]], dtype='float32')])


if __name__ == '__main__':
    save_model(args)
```



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