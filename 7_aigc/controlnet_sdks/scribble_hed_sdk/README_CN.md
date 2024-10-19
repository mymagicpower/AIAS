### 官网：
[官网链接](https://www.aias.top/)

### 图像生成预处理
- 显卡CUDA：11.7版本
- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。

#### 1. Canny 边缘检测
- CannyExample
- Canny 边缘检测预处理器可很好识别出图像内各对象的边缘轮廓，常用于生成线稿。
- 对应ControlNet模型： control_canny

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/canny.png"  width = "600"/>
</div> 

#### 2. MLSD 线条检测
- MlsdExample
- MLSD 线条检测用于生成房间、直线条的建筑场景效果比较好。
- 对应ControlNet模型： control_mlsd

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/mlsd.png"  width = "600"/>
</div> 

#### 3. Scribble 涂鸦
- HedScribbleExample
- PidiNetScribbleGPUExample
- 不用自己画，图片自动生成类似涂鸦效果的草图线条。
- 对应ControlNet模型： control_mlsd

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/scribble.png"  width = "600"/>
</div> 

#### 4. SoftEdge 边缘检测
- HED - HedScribbleExample
- HED Safe - HedScribbleExample
- PidiNet - PidiNetGPUExample
- PidiNet Safe - PidiNetGPUExample
- SoftEdge 边缘检测可保留更多柔和的边缘细节，类似手绘效果。
- 对应ControlNet模型： control_softedge。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/softedge.png"  width = "600"/>
</div> 

#### 5. OpenPose 姿态检测
- PoseExample
- OpenPose 姿态检测可生成图像中角色动作姿态的骨架图(含脸部特征以及手部骨架检测)，这个骨架图可用于控制生成角色的姿态动作。
- 对应ControlNet模型： control_openpose。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/openpose.png"  width = "600"/>
</div> 

#### 6. Segmentation 语义分割
- SegUperNetExample
- 语义分割可多通道应用，原理是用颜色把不同类型的对象分割开，让AI能正确识别对象类型和需求生成的区界。
- 对应ControlNet模型： control_seg。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/seg.png"  width = "600"/>
</div> 

#### 7. Depth 深度检测
- Midas - MidasDepthEstimationExample
- DPT - DptDepthEstimationExample
- 通过提取原始图片中的深度信息，生成具有原图同样深度结构的深度图，越白的越靠前，越黑的越靠后。
- 对应ControlNet模型： control_depth。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/depth.png"  width = "600"/>
</div> 

#### 8. Normal Map 法线贴图
- NormalBaeExample
- 根据图片生成法线贴图，适合CG或游戏美术师。法线贴图能根据原始素材生成一张记录凹凸信息的法线贴图，便于AI给图片内容进行更好的光影处理，它比深度模型对于细节的保留更加的精确。法线贴图在游戏制作领域用的较多，常用于贴在低模上模拟高模的复杂光影效果。
- 对应ControlNet模型： control_normal。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/normal.png"  width = "600"/>
</div> 

#### 9. Lineart 生成线稿
- LineArtExample
- Lineart 边缘检测预处理器可很好识别出图像内各对象的边缘轮廓，用于生成线稿。
- 对应ControlNet模型： control_lineart。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart.png"  width = "600"/>
</div> 

#### 10. Lineart Anime 生成线稿
- LineArtAnimeExample
- Lineart Anime 边缘检测预处理器可很好识别出卡通图像内各对象的边缘轮廓，用于生成线稿。
- 对应ControlNet模型： control_lineart_anime。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime.png"  width = "600"/>
</div> 

#### 11. Content Shuffle
- ContentShuffleExample
- Content Shuffle 图片内容变换位置，打乱次序，配合模型 control_v11e_sd15_shuffle 使用。
- 对应ControlNet模型： control_shuffle。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/shuffle.png"  width = "600"/>
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