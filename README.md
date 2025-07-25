


<div align="center">
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

[![star](https://gitee.com/mymagicpower/AIAS/badge/star.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/stargazers)   [![fork](https://gitee.com/mymagicpower/AIAS/badge/fork.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/members)
</div>
<br>

<hr>
<div align="center">

[官网 http://aias.top/](http://aias.top/)

</div>
<br>

<div align="center">
  <table>
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

<br/>
<hr>

<br/>

#### 模型下载：
- 链接:https://pan.baidu.com/s/16933J3dX16xnjbYaay-4og?pwd=cwxk


#### 支持的开发运行环境
- CPU
- Windows x64, Linux x64, macOS x64

- GPU (CUDA)
- Windows x64, Linux x64


#### 培训课程基础入门（视频）
- 视频地址：
* [x] https://space.bilibili.com/34811275/lists
- 培训文档位置：
* 0_docs\


### 1: 面向 Java 程序员的 AI 训练平台【模型定制】
- 项目位置：AIAS/2_training_platform
- 面向java程序员，满足图像识别分类定制化需求。
- 支持图像分类模型训练
- 支持图像1:1
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>AI 训练平台 <br>- training</p>   
          AI训练平台提供分类模型训练能力。<br>
          并以REST API形式为上层应用提供接口。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png" width = "400px"/>
        </div>
      </td>
    </tr>                                         
  </table>
</div>
  
#### 使用说明：
- https://zhuanlan.zhihu.com/p/21746563748

<br/>
<hr>
<br/>

### 2: AI Web应用【开箱即用】
- 项目位置：AIAS/4_web_app
- Web应用，前端VUE，后端Springboot
- 可以直接部署使用，使用UI或者调用API集成到现有的系统中。
- 支持的能力清单：
```text
  1). 图像生成
  2). 人脸搜索
  3). 以图搜图
      ...
```


<br/>
#### 2.1 IOCR - 自定义模版识别：
- 项目位置：AIAS/4_web_app/iocr

<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 参照锚点设置 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_anchor.jpeg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>2. 内容识别区设置</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_content.jpeg" width = "400px"/>
        </div>
      </td>
    </tr>                                                                    
  </table>
</div>

<br/>

#### 2.2 以图搜图：
- 项目位置：AIAS/4_web_app/image_search
- 一共提供了三个不同的版本
- 无向量引擎的版本，适合100万图片以下：mini_image_search
- 向量引擎的版本，无管理系统的精简版：simple_image_search
- 向量引擎的版本，完整的产品级应用：image_search
<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 图片上传 <br>
          - 1). 支持服务器端文件夹上传 <br>
          ，大量图片使用，如千万张图片入库。 <br>
          - 2). 点击提取人脸特征按钮. <br>
          - 3). 支持客户端文件夹上传.  
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/storage.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>2. 图像搜索</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/search.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                                    
  </table>
</div>

<br/>

#### 2.3 跨模态：以文搜图，以图搜图：
- 项目位置：AIAS/4_web_app/image_text_search
- 一共提供了2个不同的版本
- 无向量引擎的版本，适合100万图片以下：mini_image_text_search
- 向量引擎的版本，适合100万图片以上：image_text_search
<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 图片上传 <br>
          - 2. 点击提取特征按钮. <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/storage.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          - 1. 以文搜图 <br>
          - 输入文本：雪地上两只狗. <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search2.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>2. 以图搜图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                                     
  </table>
</div>

<br/>


#### 2.4 人脸搜索：
- 项目位置：AIAS/4_web_app/face_search

<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 图片上传 <br>
          - 1). 点击上传按钮上传zip压缩包. <br>
          - 2). 点击提取人脸特征按钮. <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/data.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>2. 人脸搜索</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/face_search/search.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                                    
  </table>
</div>

<br/>

#### 2.5 一键抠图：
- 项目位置：AIAS/4_web_app/image_seg
- 一键抠图是一种图像处理技术，旨在自动将图像中的前景对象从背景中分离出来。
- 它可以帮助用户快速、准确地实现抠图效果，无需手动绘制边界或进行复杂的图像编辑操作。
- 一共提供了2个不同的版本
- 一键抠图完整版，包含：框选一键抠图，通用一键抠图，人体一键抠图，动漫一键抠图：image_seg
- 框选一键抠图：image_seg_sam2
<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 框选一键抠图例子1 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_seg_sam2/sam2_seg1.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          - 2. 框选一键抠图例子2 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_seg_sam2/sam2_seg2.jpg" width = "400px"/>
        </div>
      </td>
    </tr>                                                                    
  </table>
</div>


<br/>

#### 2.6 文本搜索：
- 项目位置：AIAS/4_web_app/text_search
- 随时对数据进行插入、删除、搜索、更新等操作
- 结合大模型实现RAG功能
- 根据需要替换其它的特征提取模型：1_sdks\2_nlp_sdks\embedding
- 一共提供了2个不同的版本
- 无向量引擎的版本，适合100万条数据以下：simple_text_search
- 向量引擎的版本，适合100万条数据以上：text_search
<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          - 1. 数据上传 <br>
          - 1). 点击上传按钮上传CSV文件. <br>
          - 2). 点击提取特征按钮. <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/storage.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>2. 文本搜索</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                                    
  </table>
</div>



<br/>
#### 2.7 图像生成：
- 项目位置：AIAS/4_web_app/aigc_image_gen
- 模型下载：
- 图像生成模型链接: https://pan.baidu.com/s/1znJi092mth3z68Oq_j2lsA?pwd=dmra
- 预处理模型链接: https://pan.baidu.com/s/1h75UaEqg_paias8Z1pEjOQ?pwd=yqek

<div align="center">
  <table>      
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 1. Canny 辅助生成 <br>
          - Canny 边缘检测预处理器，<br>
          - 可很好识别出图像内各对象的边缘轮廓，<br>
          - 常用于生成线稿。<br>
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
          <p>图像生成系列</p>    
          - 2. Mlsd 辅助生成 <br>
          - MLSD 线条检测用于生成房间、<br>
          - 直线条的建筑场景效果比较好。<br>
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
          <p>图像生成系列</p>    
          - 3. Scribble 涂鸦辅助生成 <br>
          - 不用自己画，<br>
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
          <p>图像生成系列</p>    
          - 4. SoftEdge 辅助生成 <br>
          - SoftEdge 边缘检测，<br>
          - 可保留更多柔和的边缘细节，<br>
          - 类似手绘效果。 <br>
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
          <p>图像生成系列</p>    
          - 5. OpenPose 辅助生成 <br>
          -姿态检测可生成图像中角色动作姿态的骨架图<br>
          - (含脸部特征以及手部骨架检测)，<br>
          - 这个骨架图可用于控制生成角色的姿态动作。 <br>
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
          <p>图像生成系列</p>    
          - 6. 语义分割辅助生成 <br>
          - 语义分割可多通道应用，<br>
          - 原理是用颜色把不同类型的对象分割开，<br>
          - 让AI能正确识别对象类型和需求生成的区界。 <br>
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
          <p>图像生成系列</p>    
          - 7. 深度估计辅助生成<br>
          - 通过提取原始图片中的深度信息， <br>
          - 生成具有原图同样深度结构的图 <br>
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
          <p>图像生成系列</p>    
          - 8. 法线贴图辅助生成<br>
          - 根据图片生成法线贴图， <br>
          - 然后根据法向贴图生成新图。 <br>
          - 适合CG或游戏美术师。 <br>
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
          <p>图像生成系列</p>    
          - 9. 线稿提取辅助生成 <br>
          - Lineart 边缘检测预处理器，<br>
          - 可很好识别出图像内各对象的边缘轮廓，<br>
          - 用于生成线稿。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_sd_new.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 10. 卡通线稿辅助生成 <br>
          - 卡通边缘检测预处理器，<br>
          - 可很好识别出卡通图像内各对象的边缘轮廓，<br>
          - 用于生成线稿。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime_sd_new.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 11. 内容重洗辅助生成<br>
          - 图片内容变换位置，<br>
          - 打乱次序生成新图<br>
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

<br/>
<br/>

### 3: AI 能力平台【开箱即用】
- 项目位置：AIAS/3_api_platform 
- Web应用，前端VUE，后端Springboot
- 可以直接部署使用，使用UI或者调用API集成到现有的系统中。
- 支持的能力清单：
```text
  1). OCR文字识别
  2). 机器翻译
  3). 语音识别
  4). 一键高清
  5). 一键抠图
  6). 黑白照片上色
  7). 图像生成
      ...
```
#### 3.1 功能说明：
- https://zhuanlan.zhihu.com/p/21873070647

#### 3.2 开发环境搭建：
- https://zhuanlan.zhihu.com/p/24587444753
- AIAS\3_api_platform\README.md

#### 3.3 如果有任何需求，请点击在线文档填写：
- [在线需求建议文档](https://ycncebx6zwkh.feishu.cn/wiki/X1lMwa7waixcYTkZf6tcxo0Qnjm?from=from_copylink)

#### 3.4 模型下载：
- https://pan.baidu.com/s/1RIKaZJXMPbGXnB2sKtWsgQ?pwd=1uuf

<br/>

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>OCR文字识别</p>   
          - 自由文本识别<br>支持旋转、倾斜的图片<br>
          - 文本图片转正 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/ocr.jpg" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>语音识别</p>   
          - 英文语音识别<br>
          - 中文语音识别
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/asr.jpg" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>202种语言互相翻译</p>    
          - 支持202种语言互相翻译<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/translation.jpg" width = "400px"/>
        </div>
      </td>
    </tr>       
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 图片一键高清: <br>提升图片4倍分辨率<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/sr.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 头像一键高清<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/facegan.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 人脸一键修复: <br>自动修复图中人脸<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/faceres.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>通用一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/seggeneral.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>人体一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/seghuman.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>动漫一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/seganime.jpg" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>黑白照片上色</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/ddcolor.jpg" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像预处理系列</p>    
          - 1. Canny 边缘检测 <br>
          - 常用于生成线稿<br>
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
          <p>图像预处理系列</p>    
          - 2. MLSD 线条检测 <br>
          - 线条检测用于生成房间、<br>
          - 直线条的建筑场景<br>
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
          <p>图像预处理系列</p>    
          - 3. Scribble 涂鸦 <br>
          - 自动生成类似涂鸦效果的草图线条<br>
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
          <p>图像预处理系列</p>    
          - 4. SoftEdge 边缘检测 <br>
          - 边缘检测可保留更多柔和的边缘细节，<br>
          - 类似手绘效果 <br>
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
          <p>图像预处理系列</p>    
          - 5. OpenPose 姿态检测 <br>
          - 可生成图像中角色动作姿态的骨架图 <br>
          - 含脸部特征以及手部骨架检测
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
          <p>图像预处理系列</p>    
          - 6. 语义分割 <br>
          - 用颜色把不同类型的对象分割开 <br>
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
          <p>图像预处理系列</p>    
          - 7. Depth 深度估计<br>
          - 生成具有原图同样深度结构的深度图 <br>
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
          <p>图像预处理系列</p>    
          - 8. 法线贴图<br>
          - 生成凹凸信息的法线贴图 <br>
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
          <p>图像预处理系列</p>    
          - 9. Lineart生成线稿 <br>
          - 识别出图像内各对象的边缘轮廓<br>
          - 用于生成线稿 
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
          <p>图像预处理系列</p>    
          - 10. 生成卡通图线稿 <br>
          - 识别出卡通图像内各对象的边缘轮廓<br>
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
          <p>图像预处理系列</p>    
          - 11. 内容重洗 <br>
          - 图片内容变换位置，打乱次序<br>
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

<br/>
<hr>
<br/>



<br/>


<br/>
<hr>
<br/>

#### 其它研究专题:

#### 1). AI + 量子计算
<div align="left">
<img src="https://qubits.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "150"  />
</div>

- 培训教程: https://zhuanlan.zhihu.com/p/503483952
- Gitee:  https://gitee.com/mymagicpower/qubits
- GitHub: https://github.com/mymagicpower/qubits

#### 2). AI + 生物医药
<div align="left">
<img src="https://bio-computing.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

- 培训教程: https://zhuanlan.zhihu.com/p/696026008
- Gitee:  https://gitee.com/mymagicpower/bio-computing
- GitHub: https://github.com/mymagicpower/bio-computing


