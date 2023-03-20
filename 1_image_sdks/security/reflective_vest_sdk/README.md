### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1GdQ_4Xuh0hSP8I09pxdIeQ?pwd=2u13

### 反光衣检测SDK
实现施工区域或者危险区域人员穿戴检测.


### SDK功能
- 反光衣检测，给出检测框和置信度

#### 运行例子 - ReflectiveVestDetectExample
- 测试图片
![pedestrian](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/reflective_detect_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "safe 0.936024010181427", probability: 0.93602, bounds: [x=0.316, y=0.628, width=0.259, height=0.370]
	class: "safe 0.9202641248703003", probability: 0.92026, bounds: [x=0.000, y=0.106, width=0.176, height=0.341]
	class: "safe 0.9085375070571899", probability: 0.90853, bounds: [x=0.578, y=0.501, width=0.221, height=0.485]
	class: "safe 0.8891122937202454", probability: 0.88911, bounds: [x=0.802, y=0.465, width=0.197, height=0.532]
	class: "unsafe 0.781899094581604", probability: 0.78189, bounds: [x=0.177, y=0.432, width=0.190, height=0.416]
]
```

### 开源算法
#### 1. sdk使用的开源算法
- [yolov5-reflective-clothes-detect-python](https://github.com/RichardoMrMu/yolov5-reflective-clothes-detect-python)

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