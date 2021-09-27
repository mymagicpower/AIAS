#### 官网：
http://www.aias.top/

#### AIAS (AI Acceleration Suite - AI算法落地加速器套件)
- AIAS提供的能力:
```bash
1. SDK
2. 平台引擎
3. 场景套件
```

- AIAS的目标（加速各场景落地）:
```bash
1. 加速算法落地
2. 为集成商赋能
3. 为企业内部项目赋能
```

![AIAS](https://djl-model.oss-cn-hongkong.aliyuncs.com/images/arch.jpeg)

- 在Data Hub, Model Hub 的基础上扩展出三个新的Hub概念:

```bash
1. SDK Hub
	 包含了对各Model Hub的支持，以及对GitHub优选模型的支持。
2. Engine Hub
   包含了API平台引擎，非结构化搜索引擎，训练引擎，边缘计算引擎等。
3. Suite Hub
   包含了面向ToB，ToC，ToG各场景的套件。比如：泛安防套件，ToB套件（IOCR, 非结构化解析，推荐系统等）...
```



![AIAS](https://djl-model.oss-cn-hongkong.aliyuncs.com/images/hub.jpeg)

#### 功能清单( [x] 标记 - 代表已实现):

- **图像识别 SDK：**

```text
  1).工具箱系列：javacv(opencv，ffmpeg)图像处理工具箱（静态图像，实时视频流处理）。[X]
  2).目标检测：目标检测[X]、目标跟踪、人脸检测&识别[X]
  3).图像分割：图像分割[X]、遥感图像、医疗影像
  4).行为分析：行为识别[X]、姿态估计[X]
  5).GAN：    超分辨率[X]、动作驱动[X]、风格迁移[X]、图像生成[X]
  6).其它类别：OCR[X]、SLAM、深度估计[X]、自动驾驶、强化学习、视频理解、图像融合[X]、图像检索[X]
    ...
```

- **NLP SDK：**

```text
  1).工具箱系列：Tokenizer，sentencepiece，fastText，npy/npz文件处理等。[X]
  1).文本生成[X]
  2).词向量[X]
  3).机器翻译[X]
  4).语义模型[X]
  5).情感分析[X]
  6).句法分析[X]
  7).词法分析[X]
  8).文本审核[X]
    ...
```

- **语音处理 SDK：**

```text
  1).工具箱系列：音素工具箱，librosa，java sound，javacv ffmpeg, fft, vad工具箱等。[X]
  1).声音克隆[X]
  2).语音合成[X]
  3).声纹识别[X]
  4).语音识别[X]
    ...
```

- **生物医药 SDK：**

```text
  1).工具箱系列：RDKit工具箱，DNA工具箱。[X]
  ...
```

- **model Hub SDK封装:**

```text
  1).Paddle Hub SDK[X](充分支持)
  2).Pytorch Hub SDK (部分支持)
  3).Tensorflow Hub SDK (部分支持)
  4).MxNet Hub SDK (部分支持)
  5).Huggingface Hub SDK (部分支持) 
  6).GitHub Model SDK (部分支持 - 优选) 
```

- **平台引擎：**

```text
  1).训练引擎[X]
  2).非结构化搜索引擎[X]
  3).API能力平台[X]
  4).边缘计算引擎
  ...
```



- **场景套件 - ToB：**

```text
  1).OCR自定义模版识别[X]
  2).通用图像搜索[X]
  ...
```



#### QQ群：111257454



###### Contact Info:

QQ: 179209347       
Mail: 179209347@qq.com

