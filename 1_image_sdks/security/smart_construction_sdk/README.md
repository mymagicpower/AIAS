
### Download the model, place it in the models directory, and unzip
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/smart_construction_models.zip

### Smart Construction Detection SDK
Supported categories:

- person (human body)
- head (without safety helmet)
- helmet (with safety helmet)

### SDK Functionality
Construction safety detection, providing detection boxes and confidence levels.

- Provides three models:
- Small model (yolov5s 29.7M)
- Medium model (yolov5m 86.8M)
- Large model (yolov5l 190.8M)
- 

### Run Small Model Example- Yolov5sExample

- Test image effect (only display safety helmet detection, filter out other categories for display, see code for details)
![small](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/helmet_head_person_s.jpeg)

### Run Medium Model Example- Yolov5mExample

- Test image effect
![medium](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/helmet_head_person_m.jpeg)

### Run Large Model Example- Yolov5lExample

- Test image effect
![large](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/helmet_head_person_l.jpeg)


After a successful run, the command line should display the following information:
```text
[INFO ] - [
	class: "helmet", probability: 0.89502, bounds: [x=0.956, y=0.525, width=0.044, height=0.067]
	class: "helmet", probability: 0.85951, bounds: [x=0.237, y=0.439, width=0.036, height=0.046]
	class: "helmet", probability: 0.81705, bounds: [x=0.901, y=0.378, width=0.036, height=0.052]
	class: "helmet", probability: 0.80817, bounds: [x=0.250, y=0.399, width=0.029, height=0.040]
	class: "helmet", probability: 0.80528, bounds: [x=0.771, y=0.336, width=0.029, height=0.043]
]
```


### Open source algorithm
#### 1. Open source algorithm used by the SDK
- [Smart_Construction](https://github.com/PeterH0323/Smart_Construction)

#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- Export model (pytorch model is special, CPU&GPU models are not universal. So CPU and GPU need to be exported separately)
- torch.device('cpu')
- torch.device('gpu')
```text
"""Exports a YOLOv5 *.pt model to ONNX and TorchScript formats

Usage:
    $ export PYTHONPATH="$PWD" && python models/export.py --weights ./weights/yolov5s.pt --img 640 --batch 1
"""

import argparse

import torch

from utils.google_utils import attempt_download

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--weights', type=str, default='./helmet_head_person_s.pt', help='weights path')
    parser.add_argument('--img-size', nargs='+', type=int, default=[640, 640], help='image size')
    parser.add_argument('--batch-size', type=int, default=1, help='batch size')
    parser.add_argument('--augment', action='store_true', help='augmented inference')
    opt = parser.parse_args()
    opt.img_size *= 2 if len(opt.img_size) == 1 else 1  # expand
    print(opt)

    # Input
    img = torch.zeros((opt.batch_size, 3, *opt.img_size))  # image size(1,3,320,192) iDetection

    # Load PyTorch model
    attempt_download(opt.weights)
    model = torch.load(opt.weights, map_location=torch.device('cpu'))['model'].float()
    model.eval()
    model.model[-1].export = False  # set Detect() layer export=True
    if img.ndimension() == 3:
        img = img.unsqueeze(0)
    y = model(img)  # dry run

    # TorchScript export
    try:
        print('\nStarting TorchScript export with torch %s...' % torch.__version__)
        f = opt.weights.replace('.pt', '.torchscript.pt')  # filename
        ts = torch.jit.trace(model, img)
        ts.save(f)
        print('TorchScript export success, saved as %s' % f)
    except Exception as e:
        print('TorchScript export failure: %s' % e)

    # # ONNX export
    # try:
    #     import onnx
    #
    #     print('\nStarting ONNX export with onnx %s...' % onnx.__version__)
    #     f = opt.weights.replace('.pt', '.onnx')  # filename
    #     model.fuse()  # only for ONNX
    #     torch.onnx.export(model, img, f, verbose=False, opset_version=12, input_names=['images'],
    #                       output_names=['classes', 'boxes'] if y is None else ['output'])
    #
    #     # Checks
    #     onnx_model = onnx.load(f)  # load onnx model
    #     onnx.checker.check_model(onnx_model)  # check onnx model
    #     print(onnx.helper.printable_graph(onnx_model.graph))  # print a human readable model
    #     print('ONNX export success, saved as %s' % f)
    # except Exception as e:
    #     print('ONNX export failure: %s' % e)
    #
    # # CoreML export
    # try:
    #     import coremltools as ct
    #
    #     print('\nStarting CoreML export with coremltools %s...' % ct.__version__)
    #     # convert model from torchscript and apply pixel scaling as per detect.py
    #     model = ct.convert(ts, inputs=[ct.ImageType(name='images', shape=img.shape, scale=1 / 255.0, bias=[0, 0, 0])])
    #     f = opt.weights.replace('.pt', '.mlmodel')  # filename
    #     model.save(f)
    #     print('CoreML export success, saved as %s' % f)
    # except Exception as e:
    #     print('CoreML export failure: %s' % e)

    # Finish
    print('\nExport complete. Visualize with https://github.com/lutzroeder/netron.')

```
