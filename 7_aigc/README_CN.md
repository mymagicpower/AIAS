

#### 项目清单:
- 7_aigc - [图像生成]

```text
  1). 图像生成预处理工具箱 controlnet_sdks
  2). 图像生成SD工具箱 stable_diffusion_sdks
      ...
```

- 7.1  图像生成预处理工具箱 controlnet_sdks
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>1. Canny 边缘检测</p>   
          - canny_sdk<br>
          - Canny 边缘检测预处理器可很好识别出<br>   图像内各对象的边缘轮廓，常用于生成线稿。<br>
          - 对应ControlNet模型： control_canny<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/canny.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>2. MLSD 线条检测</p>   
            - mlsd_sdk<br>
            - MLSD 线条检测用于生成房间、<br>   直线条的建筑场景效果比较好。<br>
            - 对应ControlNet模型： control_mlsd<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/mlsd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>3. Scribble 涂鸦</p>   
          - scribble_hed_sdk<br>
          - scribble_pidinet_sdk<br>
          - 图片自动生成类似涂鸦效果的草图线条。<br>
          - 对应ControlNet模型： control_mlsd<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/scribble.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. SoftEdge 边缘检测</p>   
          - softedge_hed_sdk<br>
          - HED - HedScribbleExample<br>
          - HED Safe - HedScribbleExample<br>
          - softedge_pidinet_sdk<br>
          - PidiNet - PidiNetGPUExample<br>
          - PidiNet Safe - PidiNetGPUExample<br>
          - SoftEdge 边缘检测可保留更多柔和的边缘细节，<br>   类似手绘效果。<br>
          - 对应ControlNet模型： control_softedge。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/softedge.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>5. OpenPose 姿态检测</p>   
          - pose_sdk<br>
        - OpenPose 姿态检测可生成图像中角色动作<br>姿态的骨架图(含脸部特征以及手部骨架检测)<br>，这个骨架图可用于控制生成角色的姿态动作。<br>
        - 对应ControlNet模型： control_openpose。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/openpose.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>6. Segmentation 语义分割</p>   
          - seg_upernet_sdk<br>
        - 语义分割可多通道应用，<br>原理是用颜色把不同类型的对象分割开，<br>让AI能正确识别对象类型和需求生成的区界。<br>
        - 对应ControlNet模型： control_seg。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/seg.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>7. Depth 深度检测</p>   
          - depth_estimation_midas_sdk<br>
          - Midas - MidasDepthEstimationExample<br>
          - depth_estimation_dpt_sdks<br>
          - DPT - DptDepthEstimationExample<br>
          - 通过提取原始图片中的深度信息，<br>生成具有原图同样深度结构的深度图，<br>越白的越靠前，越黑的越靠后。<br>
          - 对应ControlNet模型： control_depth。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/depth.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>8. Normal Map 法线贴图</p>   
          - normal_bae_sdk<br>
          - NormalBaeExample<br>
          - 根据图片生成法线贴图，适合CG或游戏美术师。<br>法线贴图能根据原始素材生成<br>一张记录凹凸信息的法线贴图，<br>便于AI给图片内容进行更好的光影处理，<br>它比深度模型对于细节的保留更加的精确。<br>法线贴图在游戏制作领域用的较多，<br>常用于贴在低模上模拟高模的复杂光影效果。<br>
          - 对应ControlNet模型： control_normal。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/normal.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>9. Lineart 生成线稿</p>   
          - lineart_sdk<br>
          - lineart_coarse_sdk<br>
          - Lineart 边缘检测预处理器可很好识别出<br>图像内各对象的边缘轮廓，用于生成线稿。
          - 对应ControlNet模型： control_lineart。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>10. Lineart Anime 生成线稿</p>   
          - lineart_anime_sdk<br>
          - LineArtAnimeExample<br>
          - Lineart Anime 边缘检测预处理器<br>可很好识别出卡通图像内<br>各对象的边缘轮廓，用于生成线稿。<br>
          - 对应ControlNet模型： control_lineart_anime。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>11. Content Shuffle</p>   
          - content_shuffle_sdk<br>
          - ContentShuffleExample<br>
          - Content Shuffle 图片内容变换位置，<br>打乱次序，配合模型 <br>control_v11e_sd15_shuffle 使用。<br>
          - 对应ControlNet模型： control_shuffle。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/shuffle.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                  
  </table>
</div>

- 7.2  图像生成SD工具箱 stable_diffusion_sdks
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>1. 文生图：输入提示词（仅支持英文），<br>生成图片（仅支持英文）</p>
            - txt2image_sdk<br> 
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>2. 图生图：根据图片及提示词<br>（仅支持英文）生成图片</p>
            - image2image_sdk<br> 
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>3. Lora 文生图</p>
            - lora_sdk<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/astronaut.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.1. Canny 边缘检测</p>
            - controlnet_canny_sdk<br>  
            - Canny 边缘检测预处理器可<br>很好识别出图像内各对象<br>的边缘轮廓，常用于生成线稿。<br>  
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/canny_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.2. MLSD 线条检测</p>
            - controlnet_mlsd_sdk<br>
            - MLSD 线条检测用于生成房间、<br>直线条的建筑场景效果比较好。<br> 
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/mlsd_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.3. Scribble 涂鸦</p>
            - controlnet_scribble_sdk<br>    
            - 图片自动生成类似涂鸦效果的草图线条。<br> 
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/scribble_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.4. SoftEdge 边缘检测</p>
            - controlnet_softedge_sdk<br>   
            - SoftEdge 边缘检测可保留更多<br>柔和的边缘细节，类似手绘效果。<br> 
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/softedge_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.5. OpenPose 姿态检测</p>
            - controlnet_pose_sdk<br>   
            - OpenPose 姿态检测可生成图像<br>中角色动作姿态的骨架图<br>(含脸部特征以及手部骨架检测)<br>，这个骨架图可用于控制生成角色的姿态动作。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/openpose_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.6. Segmentation 语义分割</p>
            - controlnet_seg_sdk<br>   
            - 语义分割可多通道应用，<br>原理是用颜色把不同类型的对象分割开，<br>让AI能正确识别对象类型和需求生成的区界。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/seg_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.7. Depth 深度检测</p>
            - controlnet_depth_sdk<br>   
            - 通过提取原始图片中的深度信息，<br>生成具有原图同样深度结构的深度图，<br>越白的越靠前，越黑的越靠后。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/depth_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.8. Normal Map 法线贴图</p>
            - controlnet_normal_sdk<br>   
            - 根据图片生成法线贴图，<br>适合CG或游戏美术师。<br>法线贴图能根据原始素材生成一张记录凹凸信息的法线贴图，<br>便于AI给图片内容进行更好的光影处理，<br>它比深度模型对于细节的保留更加的精确。<br>法线贴图在游戏制作领域用的较多，<br>常用于贴在低模上模拟高模的复杂光影效果。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/normal_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.9. Lineart 生成线稿</p>
            - controlnet_lineart_sdk<br>   
            - controlnet_lineart_coarse_sdk<br>   
            - Lineart 边缘检测预处理器可很好识别出<br>图像内各对象的边缘轮廓，用于生成线稿。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.10. Lineart Anime 生成线稿</p>
            - controlnet_lineart_anime_sdk<br>   
            - Lineart Anime <br>边缘检测预处理器可很好<br>识别出卡通图像内各对象的边缘轮廓，<br>用于生成线稿。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>4. Controlnet 图像生成-4.11. Content Shuffle</p>
            - controlnet_shuffle_sdk<br>   
            - Content Shuffle 图片内容变换位置，<br>打乱次序，配合模型 <br>control_v11e_sd15_shuffle 使用。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/shuffle_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>                                              
  </table>
</div>

