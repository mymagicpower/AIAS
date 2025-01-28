


#### 项目清单:

- 1. 1_image_sdks - [图像识别 SDK]
```text
  1). 工具箱系列：图像处理工具箱（静态图像）
  2). 目标检测
  3). 其它类别：OCR等
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



