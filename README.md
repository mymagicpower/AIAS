


<div align="center">
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

[![star](https://gitee.com/mymagicpower/AIAS/badge/star.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/stargazers)   [![fork](https://gitee.com/mymagicpower/AIAS/badge/fork.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/members)
</div>

<br>
<hr>




<br>

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/AIAS.png"  width = "600"  />

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

#### 支持的开发运行环境
- CPU
- Windows x64, Linux x64, macOS x64

- GPU (CUDA)
- Windows x64, Linux x64

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
  
#### 1.1 使用说明：
- https://zhuanlan.zhihu.com/p/21746563748

#### 1.2 Java 模型训练培训教程
- [1_简介](https://zhuanlan.zhihu.com/p/21970343809)
- [2_模型库](https://zhuanlan.zhihu.com/p/21971423493)
- [3_计算图](https://zhuanlan.zhihu.com/p/21972965668)
- [4_数据集迭代器](https://zhuanlan.zhihu.com/p/21973590418)
- [5_模型监听器](https://zhuanlan.zhihu.com/p/21974185659)
- [6_保存和加载模型](https://zhuanlan.zhihu.com/p/21974854322)
- [7_模型推理](https://zhuanlan.zhihu.com/p/21975384175)
- [8_训练UI界面](https://zhuanlan.zhihu.com/p/21975767565)
- [9_MLP网络](https://zhuanlan.zhihu.com/p/21976108949)
- [10_Vgg16实现图片分类](https://zhuanlan.zhihu.com/p/21977082859)
- [11_ResNet50 实现图片分类](https://zhuanlan.zhihu.com/p/21978360806)
- [12_ResNet50-图像1:1比对](https://zhuanlan.zhihu.com/p/21979539629)
- [13_yolo-目标检测](https://zhuanlan.zhihu.com/p/21981802325)


<br/>
<hr>
<br/>

### 2: AI 能力平台【开箱即用】
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
#### 2.1 功能说明：
- https://zhuanlan.zhihu.com/p/21873070647

#### 2.2 开发环境搭建：
- https://zhuanlan.zhihu.com/p/24587444753
- AIAS\3_api_platform\README.md

#### 2.3 如果有任何需求，请点击在线文档填写：
- [在线需求建议文档](https://ycncebx6zwkh.feishu.cn/wiki/X1lMwa7waixcYTkZf6tcxo0Qnjm?from=from_copylink)


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
<hr>
<br/>



### 视频培训课程:   制作中......
- 相关源码
- 技术答疑

#### 培训课程系列1. 基础篇
- 1. JavaCV - java版的OpenCV实现传统图像处理（提供常用代码例子）
- 2. NDArray - java版的numpy，用于高性能处理矩阵（提供常用代码例子）

#### 培训课程系列2. 算法篇
- 1. 深度学习算法基础
    - 基础知识：前馈神经网络，卷积神经网络，循环神经网络
    - 图像识别：图像分类，图像分割，目标检测
- 2. java版的模型开发与训练
- 3. pytorch 模型开发与训练

#### 培训课程系列3. 应用篇 - JavaAI实战系列
- 1. 图像处理_SDK（培训常用图像处理，并提供可商用的源码）
    - 人脸工具箱
    - 人脸高清修复
    - 图文高清_黑白上色

- 2. NLP_SDK（培训常用自然语言处理，并提供可商用的源码）
    - 代码特征向量提取
    - 中文特征向量提取 
    - 多语言文本特征向量提取
    - 机器翻译

- 3. Web应用（培训如何开发web类应用，并提供可商用的源码）
    - OCR，OCR自定义模版
    - 人脸搜索
    - 以图搜图
    - 图像文本跨模态搜索
    - 文本搜索
    - 代码语义搜索
    - 一键抠图
    - 图像高清
    - 机器翻译

- 4. AIGC 图像生成（培训如何开发图像生成类应用，并提供可商用的源码）
    - AIGC提示词如何撰写
    - 图像生成预处理
    - 图像生成SD工具箱
    - 模型微调（LoRA）
	
- 5. AI桌面应用开发（培训如何开发桌面应用，并提供可商用的源码）
    - 大模型桌面应用
    - OCR桌面应用
    - 图像高清放大
	
- 6. 大模型
    - 大模型算法原理（transformer，训练，微调，推理优化）
    - 知识库，RAG增强生成等
    - 提示词工程 

<br/>
<hr>
<br/>

#### 其它研究专题:

#### 1). AI + 量子计算
<div align="left">
<img src="https://qubits.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "150"  />
</div>

- 官网: http://www.qubits.top/
- Gitee:  https://gitee.com/mymagicpower/qubits
- GitHub: https://github.com/mymagicpower/qubits

#### 2). AI + 生物医药
<div align="left">
<img src="https://bio-computing.oss-cn-shanghai.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

- 官网: http://www.biocomputing.top/
- Gitee:  https://gitee.com/mymagicpower/bio-computing
- GitHub: https://github.com/mymagicpower/bio-computing


