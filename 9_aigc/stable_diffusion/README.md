
#### Image Generation
- Text-to-Image: Input prompts (English only) to generate images (English only)
  -GPU version StableDiffusionGPU.java
  -CPU version StableDiffusionCPU.java
- Image-to-Image: Generate images based on input images and prompts (English only)
  -GPU version Img2ImgStableDiffusionGPU.java
  -GPU: CUDA 11.7 version
- Reference test data: resolution 512 * 512, 25 steps, CPU (i5 processor) 5 minutes. 3060 graphics card 20 seconds.



#### [Model Download](https://drive.google.com/file/d/1FdDCcGsytSHdKMYLMuNIGwdnzF_kF2tJ/view?usp=share_link)
- CPU model saved in stable_diffusion/pytorch_cpu
- GPU model saved in stable_diffusion/pytorch_gpu

### Text-to-Image Test

- Prompt: a photo of an astronaut riding a horse on mars
- Negative Prompt:
- Generated Image:

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png"  width = "400"/>
</div> 



#### Image Generation Prompts Reference
- https://arthub.ai/


#### Art Appreciation
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/sample.png"  width = "600"/>
</div> 


