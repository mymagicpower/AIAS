### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1LnKnwM2TcEi8SpP-cHbA9g?pwd=yu4g

### 烟火检测SDK
支持烟雾-火灾2类检测.


### SDK功能
- 烟火检测，给出检测框和置信度
- 支持类别：
- fire
- smoke

#### 运行例子 - FireSmokeDetectExample
- 测试图片
![fire_detect](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/fire_detect_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "fire 0.847178041934967", probability: 0.84717, bounds: [x=0.522, y=0.516, width=0.083, height=0.173]
	class: "smoke 0.4434642493724823", probability: 0.44346, bounds: [x=0.492, y=0.000, width=0.295, height=0.116]
	class: "smoke 0.36228814721107483", probability: 0.36228, bounds: [x=0.576, y=0.110, width=0.113, height=0.121]
]
```

### 开源算法
#### 1. sdk使用的开源算法
- [fire-smoke-detect-yolov4](https://github.com/gengyanlei/fire-smoke-detect-yolov4)

#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- 导出模型（pytorch 模型特殊，CPU&GPU模型不通用。所以CPU，GPU需要分别导出）
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
    parser.add_argument('--weights', type=str, default='./best.pt', help='weights path')
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

### 其它帮助信息
http://aias.top/guides.html


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