
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/voiceprint.zip

### Voiceprint Recognition

The so-called voiceprint is the sound wave spectrum that carries speech information displayed by the electroacoustic instrument. The generation of human language is a complex physiological and physical process between the language center and the pronunciation organ of the human body. The size and shape of the pronunciation organs such as the tongue, teeth, larynx, lungs, and nasal cavity used by people when speaking vary greatly, so the voiceprint spectra of any two people are different. Voiceprint recognition (Voiceprint Recognition, VPR), also known as speaker recognition, has two types: speaker recognition (Speaker Identification) and speaker verification (Speaker Verification). The former is used to determine which of several people a certain speech segment is spoken by, which is a "multiple-choice" problem; while the latter is used to confirm whether a certain speech segment is spoken by a specified person, which is a "one-to-one discrimination" problem. Different tasks and applications will use different voiceprint recognition technologies. For example, identification technology may be needed when narrowing the scope of criminal investigation, while confirmation technology is required for bank transactions. Whether it is identification or confirmation, the speaker's voiceprint needs to be modeled first, which is the so-called "training" or "learning" process.

The SDK implements the voiceprint recognition model based on PaddlePaddle. The Chinese speech corpus dataset is used, which has voice data from 3242 people and over 1,130,000 speech data.

### SDK contains functions

- Voiceprint feature vector extraction
  -Voiceprint similarity calculation

### Running Example - VoiceprintExample

After running successfully, the command line should see the following information:
```text
...
# Audio files a_1.wav and a_2.wav are from the same person
[INFO ] - input audio: src/test/resources/a_1.wav
[INFO ] - input audio: src/test/resources/a_2.wav
[INFO ] - input audio: src/test/resources/b_1.wav

# Voiceprint 512-dimensional feature vector
[INFO ] - a_1.wav feature: [-0.24602059, 0.20456463, -0.306607, ..., 0.016211584, 0.108457334]
[INFO ] - a_2.wav feature: [-0.115257666, 0.18287876, -0.45560476, ..., 0.15607461, 0.12677354]
[INFO ] - b_1.wav feature: [-0.009925389, -0.02331138, 0.18817122, ..., 0.058160514, -0.041663148]

# Similarity calculation
[INFO ] - a_1.wav, a_2.wav similarity: 0.9165065
[INFO ] - a_1.wav, b_1.wav similarity: 0.024052326

```


### Open source algorithm

### 1. Open source algorithm used by the SDK
- [VoiceprintRecognition-PaddlePaddle](https://github.com/yeyupiaoling/VoiceprintRecognition-PaddlePaddle)
#### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
- Export model
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
add_arg('gpus',             str,    '0',                      'GPU number used for training, separated by English commas, such as: 0,1')
add_arg('batch_size',       int,    32,                       'Batch size for training')
add_arg('num_workers',      int,    4,                        'Number of threads for reading data')
add_arg('num_epoch',        int,    50,                       'Number of training rounds')
add_arg('num_classes',      int,    3242,                     'Number of classification categories')
add_arg('learning_rate',    float,  1e-3,                     'Size of the initial learning rate')
add_arg('input_shape',      str,    '(None, 1, 257, 257)',    'Data input shape')
add_arg('train_list_path',  str,    'dataset/train_list.txt', 'Data list path for training data')
add_arg('test_list_path',   str,    'dataset/test_list.txt',  'Data list path for test data')
add_arg('save_model',       str,    'models/',                'Path to save the model')
add_arg('resume',           str,    None,                     'Resume training, if None, no restored model is used')
add_arg('pretrained_model', str,    None,                     'Path to the pre-trained model, if None, no pre-trained model is used')
args = parser.parse_args()

# Evaluate the model
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

# Save the model
def save_model(args,model):
    input_shape = eval(args.input_shape)
    # Save the prediction model
    if not os.path.exists(os.path.join(args.save_model, 'infer')):
        os.makedirs(os.path.join(args.save_model, 'infer'))
    paddle.jit.save(layer=model,
                    path=os.path.join(args.save_model, 'infer/model'),
                    input_spec=[InputSpec(shape=[input_shape[0], input_shape[1], input_shape[2], input_shape[3]], dtype='float32')])

if __name__ == '__main__':
    save_model(args)

```

