### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于各自项目的models目录
- 链接: https://pan.baidu.com/s/1sQu1mVR6pPqyBL8nil89tg?pwd=g287

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

#### 图像生成提示词参考
- https://arthub.ai/

#### 作品欣赏
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/sample.png"  width = "600"/>
</div> 

#### 测试环境和数据
- 显卡CUDA：11.7版本
- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。

#### 1. 文生图：输入提示词（仅支持英文），生成图片（仅支持英文）
- GPU版本 StableDiffusionGPU.java
- CPU版本 StableDiffusionCPU.java
#### 文生图测试
- 提示词 prompt： a photo of an astronaut riding a horse on mars
- 生成图片效果：
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png"  width = "400"/>
</div> 

### 2. 图生图：根据图片及提示词（仅支持英文）生成图片
- CPU版本 Image2ImageCpu.java
- GPU版本 Image2ImageGpu.java

### 3. Lora 文生图
- CPU版本 LoraTxt2ImageCpu.java

### 4. Controlnet 图像生成
- 显卡CUDA：11.7版本
- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。

#### 4.1. Canny 边缘检测
- CPU版本 ControlNetCannyCpu.java
- GPU版本 ControlNetCannyGpu.java
- Canny 边缘检测预处理器可很好识别出图像内各对象的边缘轮廓，常用于生成线稿。
- 对应ControlNet模型： control_canny

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/canny_sd.png"  width = "600"/>
</div> 

#### 4.2. MLSD 线条检测
- CPU版本 ControlNetMlsdCpu.java
- GPU版本 ControlNetMlsdGpu.java
- MLSD 线条检测用于生成房间、直线条的建筑场景效果比较好。
- 对应ControlNet模型： control_mlsd

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/mlsd_sd.png"  width = "600"/>
</div> 

#### 4.3. Scribble 涂鸦
- CPU版本 ControlNetScribbleHedCpu.java，ControlNetScribblePidiNetCpu.java
- GPU版本 ControlNetScribbleHedGpu.java，ControlNetScribblePidiNetGpu.java
- 不用自己画，图片自动生成类似涂鸦效果的草图线条。
- 对应ControlNet模型： control_scribble

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/scribble_sd.png"  width = "600"/>
</div> 

#### 4.4. SoftEdge 边缘检测
- HED Safe
- PidiNet
- PidiNet Safe
- CPU版本 ControlNetSoftEdgeCpu
- GPU版本 ControlNetSoftEdgeGpu
- SoftEdge 边缘检测可保留更多柔和的边缘细节，类似手绘效果。
- 对应ControlNet模型： control_softedge。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/softedge_sd.png"  width = "600"/>
</div> 

#### 4.5. OpenPose 姿态检测
- CPU版本 ControlNetPoseCpu.java
- GPU版本 ControlNetPoseGpu.java
- OpenPose 姿态检测可生成图像中角色动作姿态的骨架图(含脸部特征以及手部骨架检测)，这个骨架图可用于控制生成角色的姿态动作。
- 对应ControlNet模型： control_openpose。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/openpose_sd.png"  width = "600"/>
</div> 

#### 4.6. Segmentation 语义分割
- CPU版本 ControlNetSegCpu.java
- GPU版本 ControlNetSegGpu.java
- 语义分割可多通道应用，原理是用颜色把不同类型的对象分割开，让AI能正确识别对象类型和需求生成的区界。
- 对应ControlNet模型： control_seg。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/seg_sd.png"  width = "600"/>
</div> 

#### 4.7. Depth 深度检测
- Midas
- CPU版本 ControlNetDepthDptCpu.java
- GPU版本 ControlNetDepthDptGpu.java
- DPT
- CPU版本 ControlNetDepthMidasCpu.java
- GPU版本 ControlNetDepthMidasGpu.java
- 通过提取原始图片中的深度信息，生成具有原图同样深度结构的深度图，越白的越靠前，越黑的越靠后。
- 对应ControlNet模型： control_depth。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/depth_sd.png"  width = "600"/>
</div> 

#### 4.8. Normal Map 法线贴图
- CPU版本 ControlNetNormalbaeCpu.java
- GPU版本 ControlNetNormalbaeGpu.java
- 根据图片生成法线贴图，适合CG或游戏美术师。法线贴图能根据原始素材生成一张记录凹凸信息的法线贴图，便于AI给图片内容进行更好的光影处理，它比深度模型对于细节的保留更加的精确。法线贴图在游戏制作领域用的较多，常用于贴在低模上模拟高模的复杂光影效果。
- 对应ControlNet模型： control_normal。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/normal_sd.png"  width = "600"/>
</div> 

#### 4.9. Lineart 生成线稿
- CPU版本 ControlNetLineArtCpu.java
- GPU版本 ControlNetLineArtGpu.java
- Lineart 边缘检测预处理器可很好识别出图像内各对象的边缘轮廓，用于生成线稿。
- 对应ControlNet模型： control_lineart。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_sd.png"  width = "600"/>
</div> 

#### 4.10. Lineart Anime 生成线稿
- CPU版本 ControlNetLineArtAnimeCpu.java
- GPU版本 ControlNetLineArtAnimeGpu.java
- Lineart Anime 边缘检测预处理器可很好识别出卡通图像内各对象的边缘轮廓，用于生成线稿。
- 对应ControlNet模型： control_lineart_anime。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime_sd.png"  width = "600"/>
</div> 

#### 4.11. Content Shuffle
- CPU版本 ControlNetShuffleCpu.java
- GPU版本 ControlNetShuffleGpu.java
- Content Shuffle 图片内容变换位置，打乱次序，配合模型 control_v11e_sd15_shuffle 使用。
- 对应ControlNet模型： control_shuffle。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/shuffle_sd.png"  width = "600"/>
</div> 



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