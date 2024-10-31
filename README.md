


<div align="center">
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/logo.png"  width = "200"  />
</div>

[![star](https://gitee.com/mymagicpower/AIAS/badge/star.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/stargazers)   [![fork](https://gitee.com/mymagicpower/AIAS/badge/fork.svg?theme=gvp)](https://gitee.com/mymagicpower/AIAS/members)
<h4 align="center">
    <p>
        <b>中文</b> |
        <a href="https://github.com/mymagicpower/AIAS/blob/main/README_EN.md">English</a>
    <p>
</h4>
<h4 align="center">
    <p>
        <b>官网</b> |
        <a href="http://www.aias.top/">http://www.aias.top/</a>
    <p>
</h4>
</div>

<br>
<hr>

#### 1. 官网: 
- 网址：http://www.aias.top/
- 帮助：https://aias.top/guides.html

#### 2. 开源地址: 

- Gitee:  https://gitee.com/mymagicpower/AIAS
- GitHub: https://github.com/mymagicpower/AIAS

#### 3. 人工智能算法：
- https://zhuanlan.zhihu.com/p/693738275
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

#### 项目清单:

- 1. 1_image_sdks - [图像识别 SDK]
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
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 face_sdks</p>
          1：人脸检测(含关键点) <br>
          - face_detection_sdk<br>
          2：人脸对齐<br>
          - face_alignment_sdk<br>
          - 根据人脸关键点对齐。<br>  
          3：人脸特征提取与比对
          - face_feature_sdk<br>
          4：人脸分辨率提升<br>
          - face_sr_sdk<br>
          5：图片人脸修复<br>
          - face_restoration_sdk<br>
          6：口罩检测 <br>
          - mask_sdk
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/retinaface_detected.png"  width = "400px"/>
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
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>图片特征提取(512维)SDK <br>- feature_extraction_sdk</p>
          提取图片512维特征值，<br>并支持图片1:1特征比对，<br>给出置信度。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/feature_extraction_sdk/comparision.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>图像&文本的跨模态检索<br>- image_text_40_sdk</p>
          - 支持40种语言<br>
          -图像&文本特征向量提取<br>
          -相似度计算<br>
          -softmax计算置信度
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>图像矫正 <br>- image_alignment_sdk</p>
          -自动检测边缘，透视变换转正<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/MlsdSquareExample.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>文本图像超分辨 <br>- image_text_sr_sdk</p>
          -可以用于提升电影字幕清晰度。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TextSrExample.jpg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>图像超分辨(4倍)<br>- super_resolution_sdk</p>
          -提升图片4倍分辨率。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/super_resolution_sdk/stitch0.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>黑白图片上色 <br>- image_colorization_sdk</p>
          -应用到黑白图像中<br>从而实现黑白照片的上色。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/cv/image_colorization_sdk/color.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>一键抠图工具箱 <br>- 1. 通用一键抠图</p>
          - seg_unet_sdk <br>
          -包括三个模型：满足不同精度，速度的要求。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/cv/seg_unet_sdk/general.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>一键抠图工具箱 <br>- 2. 动漫一键抠图</p>
          - seg_unet_sdk <br>
          无需手动绘制边界，<br>大大提高了抠图的效率和精准度。<br>应用场景如：<br>
          - 广告设计<br>
          - 影视后期制作<br>
          - 动漫创作等<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/cv/seg_unet_sdk/anime.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>一键抠图工具箱 <br>- 3. 衣服一键抠图</p>
          - seg_unet_sdk <br>
          应用场景： <br>
          - 电子商务 <br>
          - 社交媒体 <br>
          - 广告设计 <br>
          - 时尚设计 <br>
          - 虚拟试衣 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/cv/seg_unet_sdk/cloth.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>一键抠图工具箱 <br>- 4. 人体一键抠图</p>
          - seg_unet_sdk <br>
          - 将人体从背景中抠出，<br>形成一个透明背景的人体图像。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/cv/seg_unet_sdk/human.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                        
  </table>
</div>

- 2. 2_nlp_sdks - [自然语言 SDK]
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
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>机器翻译</p>
          1. 202种语言互相翻译<br>  
          - translation/trans_nllb_sdk<br> 
          - 支持202种语言互相翻译,<br>
          - 支持 CPU / GPU。 <br> 
          2. 中英互相翻译  <br> 
          - translation/translation_sdk <br> 
          - 可以进行英语和中文之间的翻译,<br>
          - 支持 CPU / GPU。    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    </tr>
      <td style="width:220px">
        <div align="left">
          <p>文本特征提取向量工具箱</p>
            - embedding/*<br>
            -1. 4个中文SDK：<br>
            1).m3e_cn_sdk<br>
            2).text2vec_base_chinese_sdk<br>
            3).text2vec_base_chinese_sentence_sdk<br>
            4).text2vec_base_chinese_paraphrase_sdk<br>
            -2. 3个多语言SDK：<br>
            1).sentence_encoder_15_sdk<br>（支持 15 种语言）<br>
            2).sentence_encoder_100_sdk<br>（支持100种语言）<br>
            3).text2vec_base_multilingual_sdk<br>（支持50+种语言）<br>
            -3. 3个代码语义SDK：<br>
            1).code2vec_sdk<br>
            2).codet5p_110m_sdk<br>
            3).mpnet_base_v2_sdk<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png"  width = "400px"/>
        </div>
      </td>
    </tr>                                                         
  </table>
</div>


- 3. 3_audio_sdks - [语音处理 SDK]
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



- 4. 4_video_sdks - [视频解析SDK]
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


- 5. 5_bigdata_sdks - [大数据SDK]
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

- 6. 6_web_app - [Web应用，前端VUE，后端Springboot]
```text
  1). 训练引擎
  2). 代码语义搜索
  3). 机器翻译
  4). 一键抠图 
  5). 图像分辨率增强
  6). 图像&文本的跨模态相似性比对检索【支持40种语言】
  7). 文本向量搜索，可配合大模型使用
  8). 人像搜索
  9). 语音识别
      ...
```

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
    <tr>
      <td>
        <div align="left">
          <p>代码语义搜索 <br>- code_search</p>  
            用于软件开发过程中的，<br>代码搜代码，语义搜代码。<br>
            主要特性：<br>
            - 底层使用特征向量相似度搜索<br>
            - 单台服务器十亿级数据的毫秒级搜索<br>
            - 近实时搜索，支持分布式部署<br>
            - 随时对数据进行插入、<br>删除、搜索、更新等操作
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/data/images/codesearch.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>机器翻译<br>- text_translation</p>   
          - 支持202种语言互相翻译。<br>
          - 支持 CPU / GPU<br>
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
          <p>一键抠图 Web 应用<br>- image_seg</p>   
          当前版本包含了下面功能：<br>
          - 1. 通用一键抠图<br>
          - 2. 人体一键抠图<br>
          - 3. 动漫一键抠图
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_all.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>图片一键高清<br>- image_gan</p>   
          当前版本包含了下面功能：<br>
          - 图片一键高清: 提升图片4倍分辨率。<br>
          - 头像一键高清<br>
          - 人脸一键修复<br>
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
          <p>图像&文本的跨模态检索<br>
          - image_text_search</p>  
          - 支持40种语言<br>
          - 以图搜图：上传图片搜索<br>
          - 以文搜图：输入文本搜索<br>
          - 数据管理：提供图像压缩包(zip格式)上传<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>文本向量搜索 <br>- text_search</p>   
          - 语义搜索，通过句向量相似性，<br>检索语料库中与query最匹配的文本 <br>
          - 文本聚类，文本转为定长向量，<br>通过聚类模型可无监督聚集相似文本 <br>
          - 文本分类，表示成句向量，<br>直接用简单分类器即训练文本分类器 <br>
          - RAG，用于大模型搜索增强生成
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>人像搜索 <br>- face_search</p>   
          - 搜索管理<br>
          - 存储管理<br>
          - 用户管理<br>
          - 角色管理<br>
          - 菜单管理<br>
          - 部门管理<br>
          - 岗位管理<br>
          - 字典管理<br>
          - 系统日志<br>
          - SQL监控<br>
          - 定时任务<br>
          - 服务监控
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/face_search/images/search.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>语音识别Web 应用 <br>- asr</p>   
          - 英文语音识别，<br>
          - 中文语音识别。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_zh.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                    
  </table>
</div>

- 7. 7_aigc - [图像生成]
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
          - Lineart 边缘检测预处理器可很好识别出<br>图像内各对象的边缘轮廓，用于生成线稿。<br>
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
            1. 文生图<br>
            - txt2image_sdk<br>
            - 输入提示词（英文），<br>生成图片（英文）<br>
            2. 图生图<br>
            - 根据图片及提示词（英文）<br>生成图片<br>
            - image2image_sdk 
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
          <p>4. Controlnet 图像生成<br>-4.1. Canny 边缘检测</p>
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
          <p>4. Controlnet 图像生成<br>-4.2. MLSD 线条检测</p>
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
          <p>4. Controlnet 图像生成<br>-4.3. Scribble 涂鸦</p>
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
          <p>4. Controlnet 图像生成<br>-4.4. SoftEdge 边缘检测</p>
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
          <p>4. Controlnet 图像生成<br>-4.5. OpenPose 姿态检测</p>
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
          <p>4. Controlnet 图像生成<br>-4.6. Segmentation 语义分割</p>
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
          <p>4. Controlnet 图像生成<br>-4.7. Depth 深度检测</p>
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
          <p>4. Controlnet 图像生成<br>-4.8. Normal Map 法线贴图</p>
            - controlnet_normal_sdk<br>   
            - 根据图片生成法线贴图，<br>适合CG或游戏美术师。<br>法线贴图能根据原始素材生成<br>一张记录凹凸信息的法线贴图，<br>便于AI给图片内容进行更好的光影处理，<br>它比深度模型对于细节的保留更加的精确。<br>法线贴图在游戏制作领域用的较多，<br>常用于贴在低模上模拟高模的复杂光影效果。
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
          <p>4. Controlnet 图像生成<br>-4.9. Lineart 生成线稿</p>
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
          <p>4. Controlnet 图像生成<br>-4.10. Lineart Anime 生成线稿</p>
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
          <p>4. Controlnet 图像生成<br>-4.11. Content Shuffle</p>
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


- 8 8_desktop_app - [桌面App]
```text
  1). 大模型离线桌面App
  2). OCR文字识别桌面App
  3). 图像高清放大桌面App
      ...
```
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>大模型离线桌面App <br>- desktop_app_llm</p>   
            - 支持中/英文
            - 模型支持chatglm3，llama3，alpaca3等<br>
            - 支持4位，8位量化，16位半精度模型。<br>
            - 支持windows及mac系统<br>
            - 支持CPU，GPU<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/apps/desktop_llm1.png" width = "400px"/>
        </div>
      </td>
    </tr>         
    <tr>
      <td>
        <div align="left">
          <p>OCR文字识别桌面App <br>- desktop_app_ocr</p>   
          - 图片文字识别<br>
          - 支持windows, linux, mac 一键安装
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/ocr.jpeg" width = "400px"/>
        </div>
      </td>
    </tr>         
    <tr>
      <td>
        <div align="left">
          <p>图像高清放大桌面App <br>- desktop_app_upscale</p>   
            - 单张图片分辨率放大<br>
            - 批量图片分辨率放大<br>
            - 支持 windows, macos, ubuntu<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/upscale.png" width = "400px"/>
        </div>
      </td>
    </tr>                                         
  </table>
</div>

- 9 archive - [废弃不再维护的项目]


#### 联系方式:
- 微信号入群：aiastop
- 如果对您有帮助的话，请作者喝杯咖啡吧：
 <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/money_wechat.jpeg" width = "300px"/>
</div>

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


