### 官网：
[官网链接](https://www.aias.top/)


### BIGGAN 图像自动生成SDK
能够自动生成1000种类别（支持imagenet数据集分类）的图片。

### 支持分类如下：
-  tench, Tinca tinca
-  goldfish, Carassius auratus
-  great white shark, white shark, man-eater, man-eating shark, Carcharodon carcharias
-  tiger shark, Galeocerdo cuvieri
-  hammerhead, hammerhead shark
-  electric ray, crampfish, numbfish, torpedo
-  stingray
-  cock
-  hen
-  ostrich, Struthio camelus
-  brambling, Fringilla montifringilla
-  goldfinch, Carduelis carduelis
-  house finch, linnet, Carpodacus mexicanus
-  junco, snowbird
-  indigo bunting, indigo finch, indigo bird, Passerina cyanea
-  robin, American robin, Turdus migratorius
- ...

[点击下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/classification_imagenet_sdk/synset.txt)

### SDK包含两个分类器：
size 支持 128, 256, 512三种图片尺寸
如：size = 512;
imageClass 支持imagenet类别0~999
如：imageClass = 156;

### 运行例子 - BigGAN
- 测试图片类别11，图片尺寸：512X512
![img1](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image11.png)

- 测试图片类别156，图片尺寸：512X512
![img2](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image156.png)

- 测试图片类别821，图片尺寸：512X512
![img3](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biggan_sdk/image821.png)

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Number of inter-op threads is 4
[INFO ] - Number of intra-op threads is 4
[INFO ] - Generated image has been saved in: build/output/
```

### 开源算法
#### 1. sdk使用的开源算法
- [BigGAN-Generator-Pretrained-Pytorch](https://github.com/ivclab/BigGAN-Generator-Pretrained-Pytorch)
- [预训练模型 biggan-128](https://tfhub.dev/deepmind/biggan-128/2)
- [预训练模型 biggan-256](https://tfhub.dev/deepmind/biggan-256/2)
- [预训练模型 biggan-512](https://tfhub.dev/deepmind/biggan-512/2)


#### 2. 模型如何导出 ?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- 导出模型
```text
from src.biggan import BigGAN128
from src.biggan import BigGAN256 
from src.biggan import BigGAN512 

import torch 
import torchvision 

from scipy.stats import truncnorm 

import argparse 

if __name__ == '__main__': 
    parser = argparse.ArgumentParser() 
    parser.add_argument('-t', '--truncation', type=float, default=0.4) 
    parser.add_argument('-s', '--size', type=int, choices=[128, 256, 512], default=512) 
    parser.add_argument('-c', '--class_label', type=int, choices=range(0, 1000), default=156) 
    parser.add_argument('-w', '--pretrained_weight', type=str, required=True)
    args = parser.parse_args() 

    truncation = torch.clamp(torch.tensor(args.truncation), min=0.02+1e-4, max=1.0-1e-4).float()  
    c = torch.tensor((args.class_label,)).long()

    if args.size == 128: 
        z = truncation * torch.as_tensor(truncnorm.rvs(-2.0, 2.0, size=(1, 120))).float() 
        biggan = BigGAN128() 
    elif args.size == 256: 
        z = truncation * torch.as_tensor(truncnorm.rvs(-2.0, 2.0, size=(1, 140))).float() 
        biggan = BigGAN256()
    elif args.size == 512: 
        z = truncation * torch.as_tensor(truncnorm.rvs(-2.0, 2.0, size=(1, 128))).float() 
        biggan = BigGAN512() 

    biggan.load_state_dict(torch.load(args.pretrained_weight)) 
    biggan.eval() 

    #Generate model for DJL
    listSample = [z, c, torch.tensor(0.2)]
    # Use torch.jit.trace to generate a torch.jit.ScriptModule via tracing.
    traced_script_module = torch.jit.trace(biggan, listSample)
    # sm = torch.jit.script(tra)
    # Save the TorchScript model
    traced_script_module.save("traced_model.pt")


    with torch.no_grad(): 
        img = biggan(z, c, truncation)  


    img = 0.5 * (img.data + 1) 
    pil = torchvision.transforms.ToPILImage()(img.squeeze()) 
    pil.show()
```


### 其它帮助信息
https://aias.top/guides.html

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