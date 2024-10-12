


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
  2). 目标检测：目标检测、目标跟踪、人脸检测&识别
  3). 图像分割：图像分割、医疗影像
  4). 行为分析：行为识别、姿态估计
  5). GAN：    超分辨率、动作驱动、风格迁移、图像生成
  6). 其它类别：OCR、深度估计、视频理解、图像检索
      ...
```

<div align="center">
  <table>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 1：OCR方向检测与旋转 - ocr_sdks/ocr_direction_det_sdk</p>
          OCR图像预处理。     
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
          <p>OCR工具箱 2：OCR文字识别 - ocr_sdks/ocr_v3_sdk</p>
            1.  V3 文本检测: <br>
            - 中文文本检测<br>
            - 英文文本检测<br>
            - 多语言文本检测<br> 
            2.  V3 文本识别:<br> 
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
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/OcrV3RecExample2.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 3：OCR文字识别 - ocr_sdks/ocr_v4_sdk</p>
          原生支持旋转倾斜文本文字识别。     
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
          <p>OCR工具箱 4：版面分析 - ocr_sdks/ocr_layout_sdk</p>
               可以用于配合文字识别，表格识别的流水线处理使用。   <br>
               1.  中文版面分析<br>
               2.  英文版面分析<br>
               3.  中英文文档 - 表格区域检测<br>
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
          <p>OCR工具箱 5： 表格识别 - ocr_sdks/ocr_table_sdk</p>
               中英文表格识别。  
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/TableENRecExample.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>    
    <tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>OCR工具箱 6： led文字识别 - ocr_sdks/ocr_led_sdk</p>
               led表盘文字识别。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/led_rec_result.png"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 1：人脸检测(含5个人脸关键点) - face_sdks/face_detection_sdk</p>
          人脸检测(含5个人脸关键点)提供了两个模型的实现。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/retinaface_detected.png"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 2：人脸对齐- face_sdks/face_alignment_sdk</p>
          根据人脸关键点对齐人脸。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_align.png"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 3：人脸特征提取与比对- face_sdks/face_feature_sdk</p>
          人脸识别完整的pipeline：人脸检测(含人脸关键点) --> 人脸对齐 --> 人脸特征提取 --> 人脸比对
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_feature.png"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 4：人脸分辨率提升- face_sdks/face_sr_sdk</p>
          包含两个功能：<br>
          1.单张人脸图片超分辨。<br>
          2.自动检测人脸，然后对齐人脸后提升分辨率。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/single_face_sr.png"  width = "400px"/>
        </div>
      </td>
    </tr>
      <tr>
      <td style="width:220px">
        <div align="left">
          <p>人脸工具箱 5：图片人脸修复- face_sdks/face_restoration_sdk</p>
            - 自动检测人脸及关键地，然后抠图，然后根据人脸关键点转正对齐。<br>
            - 对所有转正对齐的人脸提升分辨率。<br>
            - 使用分割模型提取人脸，逆向变换后贴回原图。   
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_res.png"  width = "400px"/>
        </div>
      </td>
    </tr>              
    <tr>
      <td>
        <div align="left">
          <p>动物分类识别 - classification/animal_sdk</p>   
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
          <p>菜品分类识别 - classification/dish_sdk</p> 
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
          <p>单目深度估计 - depth_estimation_sdk</p>
          MiDaS_Small实现单目深度估计，模型可通过输入图像估计其中的深度信息。    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/depth_estimation_sdk/depth.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>动作驱动 - first_order_sdk</p>
          sdk以一段动作视频去驱动一张图片运动。可以驱动任意类型的运动。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/gan_sdks/result.gif"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>实例分割 - instance_segmentation_sdk</p>
          支持对图片中80个分类的目标进行实例分割。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/Instance_segmentation_sdk/result.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>人群密度检测 - crowd_sdk</p>
          统计人数，计算密度图。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/density.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>烟火检测 - fire_smoke_sdk</p>
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
          <p>口罩检测 - mask_sdk</p>
          口罩检测，给出检测框。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>行人检测 - pedestrian_sdk</p>
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
          <p>反光衣检测 - reflective_vest_sdk</p>
          实现施工区域或者危险区域人员穿戴检测。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/reflective_detect_result.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>智慧工地检测 - smart_construction_sdk</p>
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
          <p>车辆检测 - vehicle_sdk</p>
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

- 2. 2_nlp_sdks - [自然语言 SDK]
```text
  1). 工具箱系列：sentencepiece，fastText，npy/npz文件处理等。
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

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>轻量句向量SDK【英文】 - embedding/sentence_encoder_en_sdk</p>   
           句向量是指将语句映射至固定维度的实数向量。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png" width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>词向量SDK【英文】- embedding/word_encoder_en_sdk</p>
          每个单词或词组被映射为实数域上的向量。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/word_vector_en.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>fastText - kits/fasttext_sdk</p>
          fastText是一个快速文本分类算法。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/fastText.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>解析npy/npz文件 - kits/npy_npz_sdk</p>
          java读取python numpy保存的npz、npy文件。。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/numpy.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>Sentencepiece分词 - kits/sentencepiece_sdk</p>
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
          <p>jieba分词 - lexical_analysis/jieba_sdk</p>
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
          <p>词法分析SDK [中文] - lexical_analysis/lac_sdk</p>
          词法分析模型能整体性地完成中文分词、词性标注、专名识别任务。    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/lac_network.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>短文本相似度SDK [中文] - semantic_simnet_bow_sdk</p>
          计算两个句子的cosin相似度。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>情感分析SDK [中文]- sentiment_analysis/senta_bilstm_sdk</p>
          判断该文本的情感极性类别并给出相应的置信度。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>对话情绪识别SDK [中文] - sentiment_analysis/senta_textcnn_sdk</p>
          对话情绪识别（Emotion Detection）专注于识别智能对话场景中用户的情绪，
          针对智能对话场景中的用户文本，自动判断该文本的情绪类别并给出相应的置信度。    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>情感倾向分析SDK - sentiment_analysis/sentiment_analysis_sdk</p>
          情感倾向分析（Sentiment Classification）针对带有主观描述的中文文本，可自动判断该文本的情感极性类别并给出相应的置信度。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>文本审核SDK [中文] - porn_detection_sdk</p>
          色情检测模型可自动判别文本是否涉黄并给出相应的置信度，对文本中的色情描述、低俗交友、污秽文爱进行识别。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/text_review.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>中文翻译为英文 - translation_zh_en_sdk</p>
          中文翻译为英文SDK。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg"  width = "400px"/>
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
          <p>语音识别（ASR）【短语音】 - asr_whisper_sdk</p>   
           中文语音识别。     
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/asr.jpeg" width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td>
        <div align="left">
          <p>语音识别（ASR）【长语音】 - asr_whisper_long_sdk</p>   
           中文语音识别。     
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
          <p>语音处理包Librosa- librosa_sdk</p> 
          python语音处理库librosa的java实现。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/phoneme.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>TTS 文本转为语音 - tts_sdk</p>
          TTS 文本转为语音。     
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
          <p>摄像头口罩检测 - camera_facemask_sdk</p> 
          读取本地摄像头，实时（需要有显卡的台式机，否则会比较卡顿）检测口罩。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png"  width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>MP4检测口罩 - mp4_facemask_sdk</p>
           读取本地MP4文件，实时（需要有显卡的台式机，否则会比较卡顿）检测口罩。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png"  width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>rtsp取流检测口罩 - rtsp_facemask_sdk</p>
          通过rtsp取流，实时（需要有显卡的台式机，否则会比较卡顿）检测口罩。 
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
          <p>flink-情感倾向分析【英文】SDK - flink_sentiment_analysis_sdk</p> 
          情感倾向分析（Sentiment Classification）
          针对带有主观描述的文本，可自动判断该文本的情感极性类别并给出相应的置信度。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/sentiment_analysis.jpeg"  width = "400px"/>
        </div>
      </td>
    </tr> 
    <tr>
      <td style="width:220px">
        <div align="left">
          <p>kafka-情感倾向分析【英文】SDK - kafka_sentiment_analysis_sdk</p>
          情感倾向分析（Sentiment Classification）
          针对带有主观描述的文本，可自动判断该文本的情感极性类别并给出相应的置信度。
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

- 6. 6_metaverse - [2D虚拟人]
```text
  1). Live2DDemo
      ...
```
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>2D虚拟人APP - Live2DDemo</p>   
          最近元宇宙的概念越来越火。虚拟人技术是其中重要的组成部分。
          其原理是通过视频来捕捉人脸，并且将人的面部动作同步到人物身上。人们只需要一个摄像头就可以制造出一个生动活泼的虚拟形象了。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_metaverse/Live2DVirtualHumanDemo/2D.png" width = "400px"/>
        </div>
      </td>
    </tr>                                               
  </table>
</div>


- 7. 7_engine_hub - [平台引擎]
```text
  1). 训练引擎
      ...
```

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>AI 训练平台 - training</p>   
          AI训练平台提供分类模型训练能力。并以REST API形式为上层应用提供接口。
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

- 8. 8_aigc - [图像生成]
```text
  1). 图像生成 stable_diffusion
      ...
```
<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>java版StableDiffusion - stable_diffusion</p>   
          文生图：输入提示词（仅支持英文），生成图片（仅支持英文）
          图生图：根据图片及提示词（仅支持英文）生成图片
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/stable_diffusion/sample.png" width = "400px"/>
        </div>
      </td>
    </tr>                                               
  </table>
</div>

#### 1. 官网: 
- 网址：http://www.aias.top/
- 帮助：https://aias.top/guides.html

#### 2. 开源地址: 

- Gitee:  https://gitee.com/mymagicpower/AIAS
- GitHub: https://github.com/mymagicpower/AIAS

#### 3. 人工智能算法：
- https://zhuanlan.zhihu.com/p/693738275


#### 4. 其它研究专题:

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


