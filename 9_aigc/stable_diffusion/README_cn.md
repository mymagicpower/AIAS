### 官网：
[官网链接](https://www.aias.top/)

#### 图像生成
- 文生图：输入提示词（仅支持英文），生成图片（仅支持英文）
- GPU版本 StableDiffusionGPU.java
- CPU版本 StableDiffusionCPU.java

- 图生图：根据图片及提示词（仅支持英文）生成图片
- GPU版本 Img2ImgStableDiffusionGPU.java
- 显卡CUDA：11.7版本

- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。



#### [模型下载](https://pan.baidu.com/s/1mVaxSQ9lVsDVE36CeGNrSw?pwd=ewhg)
- cpu模型保存位置：stable_diffusion/pytorch_cpu
- gpu模型保存位置：stable_diffusion/pytorch_gpu


#### 文生图测试
- 提示词 prompt： a photo of an astronaut riding a horse on mars
- 反向提示词 negative_prompt：
- 生成图片效果：
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png"  width = "400"/>
</div> 



#### 图像生成提示词参考
- https://arthub.ai/


#### 作品欣赏
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/sample.png"  width = "600"/>
</div> 





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