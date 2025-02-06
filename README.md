


<div align="center">
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

[![star](https://gitee.com/mymagicpower/AIAS/badge/star.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/stargazers)   [![fork](https://gitee.com/mymagicpower/AIAS/badge/fork.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/members)
<h4 align="center">
    <p>
        <b>中文</b> |
        <a href="README_EN.md">English</a>
    <p>
</h4>
</div>

<br>
<hr>




<br>

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/AIAS.png"  width = "600"  />
</div>


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

### 1: 训练平台【模型定制】
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
  
- 使用说明：
- https://zhuanlan.zhihu.com/p/21746563748

<br/>
<hr>
<br/>

### 2: API平台【开箱即用】
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
      ...
```

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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/free.jpg" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_zh.png" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/assets/nllb.png" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/imageSr.png" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceGan.png" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceSr.png" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_general.jpg" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_human.jpg" width = "400px"/>
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
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_anime.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>框选一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_seg_sam2/sam2_seg1.jpg" width = "400px"/>
        </div>
      </td>
    </tr>                                                    
  </table>
</div>

<br/>
<hr>
<br/>

### 3: 人工智能 sdk
- 1_image_sdks - [图像识别 SDK]
```text
  1). 工具箱系列：图像处理工具箱（静态图像）
  2). 目标检测
  3). 图像分割
  4). GAN
  5). 其它类别：OCR等
      ...
```

<div align="center">
  <table>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 1：方向检测</p>
          - ocr_sdks/<br>ocr_direction_det_sdk<br>
          - OCR图像预处理。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/RotationExample.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 2：OCR文字识别</p>
           1. ocr_sdks/ocr_v3_sdk<br>
            1).  V3 文本检测: <br>
            - 中文文本检测<br>
            - 英文文本检测<br>
            - 多语言文本检测<br> 
            2).  V3 文本识别:<br> 
            - 中文简体<br> 
            - 中文繁体<br> 
            - 英文<br> 
            - 韩语<br> 
            - 日语<br> 
            - 阿拉伯<br> 
            - 梵文<br> 
            - 泰米尔语<br> 
            - 泰卢固语<br> 
            - 卡纳达文<br> 
            - 斯拉夫<br> 
            2. ocr_sdks/ocr_v4_sdk<br> 
            - 原生支持倾斜文本文字识别。<br>  
            - 更高的识别精度<br> 
            - 支持中英文。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3RecExample2.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 4：版面分析</p> 
          - ocr_sdks/ocr_layout_sdk<br>
               可以用于配合文字识别，<br>表格识别的流水线处理使用。<br>
               1).  中文版面分析<br>
               2).  英文版面分析<br>
               3).  中英文文档 - 表格区域检测<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/LayoutCNDetExample.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>    
    <tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 5： 表格识别 </p>
          - ocr_sdks/ocr_table_sdk<br>
          - 中英文表格识别。  
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TableENRecExample.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>       
    <tr>
      <td>
        <div align="left">
          <p>动物分类识别 <br>- classification/animal_sdk</p>   
           动物识别sdk，支持7978种动物的分类识别。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/animal_sdk/tiger.jpeg" width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>菜品分类识别 <br>- classification/dish_sdk</p> 
          菜品识别sdk，支持8416种菜品的分类识别。   
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/dish_sdk/dish.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>烟火检测 <br>- fire_smoke_sdk</p>
          烟火检测，给出检测框和置信度。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/fire_detect_result.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>行人检测 <br>- pedestrian_sdk</p>
          行人检测，给出检测框和置信度。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/ped_result.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>智慧工地检测 <br>- smart_construction_sdk</p>
          支持检测的类别：人体，安全帽。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/helmet_head_person_l.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>车辆检测 <br>- vehicle_sdk</p>
          车辆检测，给出检测框和置信度。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/vehicle_result.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                    
  </table>
</div>

- 2_nlp_sdks - [自然语言 SDK]
```text
  1). 工具箱系列：sentencepiece，fastText，npy/npz文件处理等。
  2). 大模型
  3). 词向量
  4). 机器翻译
      ...
```

<div align="center">
  <table>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>Sentencepiece分词 <br>- kits/sentencepiece_sdk</p>
          Sentencepiece分词的Java实现。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/wordpiece.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>jieba分词 <br>- lexical_analysis/jieba_sdk</p>
          jieba分词java版本的简化实现。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/jieba.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                  
  </table>
</div>


- 3_audio_sdks - [语音处理 SDK]
```text
  1). 工具箱系列：音素工具箱，librosa，java sound，javacv ffmpeg, fft, vad工具箱等。
  2). 声音克隆
  3). 语音合成
  4). 声纹识别
  5). 语音识别
      ...
```

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>中文语音识别（ASR）</p>   
          1. 短语音 <br>
          - asr_whisper_sdk<br>
          2. 长语音 <br>
          - asr_whisper_long_sdk    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/asr.jpeg" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>TTS 文本转为语音 </p>
          - tts_sdk<br>
          - TTS 文本转为语音。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/SV2TTS.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                
  </table>
</div>



- 4_video_sdks - [视频解析SDK]
```text
  1). 摄像头口罩检测 - camera_facemask_sdk
  2). MP4检测口罩 - mp4_facemask_sdk
  3). rtsp取流检测口罩 - rtsp_facemask_sdk
```

<div align="center">
  <table>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>视频流分析</p> 
          1. 摄像头口罩检测 <br>
          - camera_facemask_sdk<br>
          2. MP4检测口罩 <br>
          - mp4_facemask_sdk<br>
          3. rtsp取流检测口罩 <br>
          - rtsp_facemask_sdk
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                 
  </table>
</div>


- 5_bigdata_sdks - [大数据SDK]
```text
  1). flink-情感倾向分析【英文】- flink_sentence_encoder_sdk
  2). kafka-情感倾向分析【英文】- kafka_sentiment_analysis_sdk
      ...
```

<div align="center">
  <table>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>大数据分析</p> 
          flink-情感倾向分析<br>
          flink_sentiment_analysis_sdk<br>
          kafka-情感倾向分析<br>
          kafka_sentiment_analysis_sdk<br>
          针对带有主观描述的文本，<br>
          可自动判断该文本的情感极性类别并给出相应的置信度。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg"  width = "400px"/>
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


