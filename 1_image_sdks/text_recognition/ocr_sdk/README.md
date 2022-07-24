## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:
#### 1. 方向检测   
- 0度   
- 90度   
- 180度   
- 270度   
![detect_direction](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_direction.png)

#### 2. 图片旋转

#### 3. 文字识别(提供4组模型，请看文档)
- mobile模型
- light模型
- server模型
- v3模型
- 
##### 模型列表（根据需要自行替换）：
```text
  mobile模型:
    # mobile detection model URI
    检测: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_mobile_v2.0_det_infer.zip
    # mobile recognition model URI
    识别: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_mobile_v2.0_rec_infer.zip
  light模型:
    # light detection model URI
    检测: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv2_det_infer.zip
    # light recognition model URI
    识别: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv2_rec_infer.zip
  server模型:
    # server detection model URI
    检测: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_server_v2.0_det_infer.zip
    # server recognition model URI
    识别: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_server_v2.0_rec_infer.zip
  v3模型:
    # v3 detection model URI
    检测: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv3_det_infer.zip
    # v3 recognition model URI
    识别: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv3_rec_infer.zip
```

5. 版面分析（支持5个类别, 用于配合文字识别，表格识别的流水线处理）
- Text
- Title
- List
- Table
- Figure

```text
    # 版面分析 model URI
    layout: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ppyolov2_r50vd_dcn_365e_publaynet_infer.zip
```

5. 表格识别
- 生成html表格
- 生成excel文件
```text
    # 表格识别 model URI
    table-en: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/en_table.zip
```

### 运行OCR识别例子
#### 1.1 文字方向检测：
- 例子代码: OcrV3DetectionExample.java    
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/detect_result.png
[INFO ] - [
	class: "0", probability: 1.00000, bounds: [x=0.073, y=0.069, width=0.275, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.652, y=0.158, width=0.222, height=0.040]
	class: "0", probability: 1.00000, bounds: [x=0.143, y=0.252, width=0.144, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.628, y=0.328, width=0.168, height=0.026]
	class: "0", probability: 1.00000, bounds: [x=0.064, y=0.330, width=0.450, height=0.023]
]
```
- 输出图片效果如下：
![detect_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_result.png)

#### 2. 图片旋转：
每调用一次rotateImg方法，会使图片逆时针旋转90度。
- 例子代码: RotationExample.java 
- 旋转前图片:
![ticket_0](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_0.png)
- 旋转后图片效果如下：
![rotate_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/rotate_result.png)

#### 3.1 单线程文字识别：
再使用本方法前，请调用上述方法使图片文字呈水平(0度)方向。  
- 例子代码: OcrV3RecognitionExample.java  
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "检票：B1", probability: -1.0e+00, bounds: [x=0.761, y=0.099, width=0.129, height=0.028]
	class: "Z31C014941", probability: -1.0e+00, bounds: [x=0.110, y=0.109, width=0.256, height=0.026]
	class: "九江站", probability: -1.0e+00, bounds: [x=0.649, y=0.188, width=0.214, height=0.042]
	class: "南昌站", probability: -1.0e+00, bounds: [x=0.138, y=0.193, width=0.209, height=0.038]
	class: "D6262", probability: -1.0e+00, bounds: [x=0.431, y=0.205, width=0.139, height=0.031]
	class: "Nanchang", probability: -1.0e+00, bounds: [x=0.173, y=0.276, width=0.136, height=0.026]
	class: "Jiujiang", probability: -1.0e+00, bounds: [x=0.684, y=0.276, width=0.118, height=0.026]
	class: "03车02A号", probability: -1.0e+00, bounds: [x=0.628, y=0.347, width=0.159, height=0.024]
	class: "2019年06月07日06：56开", probability: -1.0e+00, bounds: [x=0.099, y=0.349, width=0.424, height=0.023]
	class: "二等座", probability: -1.0e+00, bounds: [x=0.692, y=0.415, width=0.092, height=0.031]
	class: "网折", probability: -1.0e+00, bounds: [x=0.420, y=0.420, width=0.058, height=0.028]
	class: "￥39.5元", probability: -1.0e+00, bounds: [x=0.104, y=0.425, width=0.127, height=0.026]
	class: "折", probability: -1.0e+00, bounds: [x=0.482, y=0.438, width=0.049, height=0.076]
	class: "限乘当日当次车", probability: -1.0e+00, bounds: [x=0.101, y=0.498, width=0.239, height=0.023]
	class: "3604211990****2417", probability: -1.0e+00, bounds: [x=0.101, y=0.646, width=0.365, height=0.024]
	class: "买票请到12306发货请到95306", probability: -1.0e+00, bounds: [x=0.197, y=0.733, width=0.420, height=0.019]
	class: "中国铁路祝您旅途愉快", probability: -1.0e+00, bounds: [x=0.252, y=0.793, width=0.304, height=0.019]
	class: "3227030010607C014941上海南售", probability: -1.0e+00, bounds: [x=0.101, y=0.878, width=0.508, height=0.021]
```
- 输出图片效果如下：
![ocr_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_result.jpeg)

#### 3.2 多线程文字识别：
- 例子代码: OcrV3MultiThreadRecExample.java

#### 4. 版面分析：
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "Text", probability: 0.98750, bounds: [x=0.081, y=0.620, width=0.388, height=0.183]
	class: "Text", probability: 0.98698, bounds: [x=0.503, y=0.464, width=0.388, height=0.167]
	class: "Text", probability: 0.98333, bounds: [x=0.081, y=0.465, width=0.387, height=0.121]
	class: "Figure", probability: 0.97186, bounds: [x=0.074, y=0.091, width=0.815, height=0.304]
	class: "Table", probability: 0.96995, bounds: [x=0.506, y=0.712, width=0.382, height=0.143]
]
```
- 输出图片效果如下：
![layout](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/layout_detect_result.jpeg)

#### 5. 表格识别：
- 运行成功后，命令行应该看到下面的信息:
```text
<html>
 <body>
  <table>
   <thead>
    <tr>
     <td>Methods</td>
     <td>R</td>
     <td>P</td>
     <td>F</td>
     <td>FPS</td>
    </tr>
   </thead>
   <tbody>
    <tr>
     <td>SegLink[26]</td>
     <td>70.0</td>
     <td>86.0</td>
     <td>770</td>
     <td>89</td>
    </tr>
    <tr>
     <td>PixelLink[4j</td>
     <td>73.2</td>
     <td>83.0</td>
     <td>77.8</td>
     <td></td>
    </tr>
...
   </tbody>
  </table> 
 </body>
</html>
```

- 输出图片效果如下：
![table](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/table.jpeg)

- 生成excel效果如下：
![excel](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/excel.png)


### 重要说明：
paddleOCR的文字检测识别，默认支持文字是旋转歪斜（即：支持自动文字转正）。
这个功能暂时没有时间适配（所以需要保证图片是摆正的，或者使用下面的预处理工具对整个图片转正，虽然不如原算法处理的完美）。

### OCR图像预处理项目：
https://gitee.com/mymagicpower/AIAS/tree/main/1_image_sdks/imagekit_java

### 参考文章：
https://blog.csdn.net/dqcfkyqdxym3f8rb0/article/details/89819785#comments
https://www.jianshu.com/p/9eb9d6f6f837
https://www.jianshu.com/p/173d329afa3a
https://blog.csdn.net/zhouguangfei0717/article/details/103026139/
https://blog.csdn.net/u014133119/article/details/82222656
https://blog.csdn.net/wsp_1138886114/article/details/83374333
以上文章供参考，并不一定是最好的，建议根据相关关键字进一步去搜索。


### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   