
<div align="left">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

[![star](https://gitee.com/mymagicpower/AIAS/badge/star.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/stargazers)   [![fork](https://gitee.com/mymagicpower/AIAS/badge/fork.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/members)  
- 官网: http://www.aias.top/
- Gitee:  https://gitee.com/mymagicpower/AIAS  
- GitHub: https://github.com/mymagicpower/AIAS

#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型在线/离线加载方式（在线自动加载，及离线加载配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html

<!-- <div align="center">
  <div align="center">欢迎交流，QQ群： 111257454，请给源码项目点个 <b><font color="#CE0000">Star</font></b> 吧！！！</div>
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/qq.png"  width = "200"  />
</div> -->

作者：Calvin    
Mail: 179209347@qq.com




#### AIAS (AI Acceleration Suite - AI算法落地加速器套件)
- AIAS提供的参考能力:
```bash
1. SDK：包含了对各Model Hub，以及GitHub优选模型的支持。
2. 平台引擎：包含了API平台引擎，搜索引擎，训练引擎，边缘计算引擎等。
3. 场景套件：包含了面向ToB，ToC，ToG各场景的套件，比如：生物医药套件。
```

- AIAS的目标:
```bash
1. 加速算法落地
2. 为集成商赋能
3. 为企业内部项目赋能
```

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/roadmap.jpg"  width = "600"  />
</div>
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/AIAS.png"  width = "600"  />
</div>


<div align="center">
  <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/aias_scope.png"  width = "600"  />

  <table>
    <tr>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/aias_edge.png"  width = "250"  />
        </div>
      </td>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/aias_edge_cloud.png"  width = "250"  />
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/aias_training1.png"  width = "250"  />
        </div>
      </td>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/aias_search.png"  width = "250"  />
        </div>
      </td>
    </tr>  
    <tr>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/face_search.png"  width = "250"  />
        </div>
      </td>
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png"  width = "250"  />
        </div>
      </td>
    </tr>   
  </table>

</div>

#### 项目清单:

- 1. image_sdks - [图像识别 SDK](http://aias.top/cv_sdk.html)    
```text
  1). 工具箱系列：图像处理工具箱（静态图像）
  2). 目标检测：目标检测、目标跟踪、人脸检测&识别
  3). 图像分割：图像分割、遥感图像、医疗影像
  4). 行为分析：行为识别、姿态估计
  5). GAN：    超分辨率、动作驱动、风格迁移、图像生成
  6). 其它类别：OCR、SLAM、深度估计、自动驾驶、强化学习、视频理解、图像融合、图像检索
      ...
```

- 2. nlp_sdks - [自然语言 SDK](http://aias.top/nlp_sdk.html)   
```text
  1). 工具箱系列：Tokenizer，sentencepiece，fastText，npy/npz文件处理等。
  2). 文本生成
  3). 词向量
  4). 机器翻译
  5). 语义模型
  6). 情感分析
  7). 句法分析
  8). 词法分析
  9). 文本审核
      ...
```

- 3. audio_sdks - [语音处理 SDK](http://aias.top/voice_sdk.html)     
```text
  1). 工具箱系列：音素工具箱，librosa，java sound，javacv ffmpeg, fft, vad工具箱等。
  2). 声音克隆
  3). 语音合成
  4). 声纹识别
  5). 语音识别
      ...
```

- 4. engine_hub - [平台引擎](http://aias.top/platform.html)      
```text
  1). 训练引擎
  2). 搜索引擎
  3). API能力平台
  4). 边缘计算引擎
      ...
```

- 5. suite_hub - [场景套件](http://aias.top/suite.html)
```text
  1). ToB: 问答系统等
  2). 生物医药  
  3). 数字虚拟人   
      ...
```

#### 项目归档:
不再维护的项目会移至下面的repo，但仍具有参考学习的价值。归档的项目选择标准：没有对应的开源项目，即不支持训练。
或者有更好的替代项目。
- Gitee:  https://gitee.com/mymagicpower/AIAS_Archive
- GitHub: https://github.com/mymagicpower/AIAS_Archive



#### 其它研究专题:

#### 1. AI + 量子计算
<div align="left">
<img src="https://qubits.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "150"  />
</div>

- 官网: http://qubits.top/      
- Gitee:  https://gitee.com/mymagicpower/qubits     
- GitHub: https://github.com/mymagicpower/qubits     

#### 2. AI + 生物医药
<div align="left">
<img src="https://bio-computing.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

- 官网: http://biocomputing.top/     
- Gitee:  https://gitee.com/mymagicpower/bio-computing     
- GitHub: https://github.com/mymagicpower/bio-computing     

#### 3. AI 桌面工具集 - 简单易用的AI桌面工具
<div align="left">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

- 官网: http://www.aiarts.top/           
- Gitee:  https://gitee.com/mymagicpower/easy_AI_apps        
- GitHub: https://github.com/mymagicpower/easy_AI_apps  


#### 3.1. 图像生成
- 文生图：输入提示词，生成图片（仅支持英文）
- 图生图：根据图片及提示词生成图片
- 图像无损放大：比如将 512*512 放大到 1024 * 1024
- 分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。
- 显卡CUDA：建议11.X版本。
- [百度云盘下载](https://pan.baidu.com/s/1AGicrktOAzym6MhLCxy7nQ?pwd=61xc)

<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/sd.jpeg"  width = "500"/>
</div> 


#### 3.2. 图像搜索
- 图片管理：选择图片目录，提取图片特征值
- 以图搜图：用图片搜索图片
- 以图搜图：根据文字描述搜索图片（基于图片的语义理解）
- [百度云盘下载](https://pan.baidu.com/s/1drC2hOIC0x2XVvk1Gdb8HA?pwd=2zt8)

<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/search.jpeg"  width = "500"/>
</div> 



#### 3.3. 图片无损放大
- 单张图片分辨率放大
- 批量图片分辨率放大
- [百度云盘下载](https://pan.baidu.com/s/1ukxPrakYrjMYPwDWwNHZ6g?pwd=riea)

<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/upscale.png"  width = "500"/>
</div> 


#### 3.4. OCR 文字识别
- 图片文字识别
- 图片表格识别（TODO）
- [百度云盘下载](https://pan.baidu.com/s/1Ddw0dUwAKv2A7M9CtVv8Hg?pwd=dk8b)

<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/ocr.jpeg"  width = "500"/>
</div> 

#### 3.N.持续更新中......
