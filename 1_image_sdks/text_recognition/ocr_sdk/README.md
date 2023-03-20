### 官网：
[官网链接](https://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1noMIVIThKAmec6D-Ai7TKA?pwd=dc6p

## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:

#### 1. 方向检测
- OcrDirectionExample
- 0度
- 90度
- 180度
- 270度   
  ![detect_direction](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_direction.png)

#### 2. 图片旋转
- RotationExample

#### 3. 文字识别 (原生支持倾斜文本, 1 & 2 需要时可以作为辅助)
- OcrV3RecognitionExample

#### 4. 图片旋转

#### 5. 版面分析（支持5个类别, 用于配合文字识别，表格识别的流水线处理）
- Text
- Title
- List
- Table
- Figure

```text
    # 版面分析 model URI
    layout: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ppyolov2_r50vd_dcn_365e_publaynet_infer.zip
```
#### 6. 表格识别
- 生成html表格
- 生成excel文件
```text
    # 表格识别 model URI
    table-en: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/en_table.zip
```

### 运行OCR识别例子
#### 1.1 文字识别：
- 例子代码: OcrV3RecognitionExample.java    
- 运行成功后，命令行应该看到下面的信息:
```text
time: 766
time: 2221
烦恼！
无数个
吃饱了就有
烦恼
没有吃饱只有一个
```

- 输出图片效果如下：
![text_with_angle](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/text_with_angle.png)


#### 2. 图片旋转：
每调用一次rotateImg方法，会使图片逆时针旋转90度。
- 例子代码: RotationExample.java 
- 旋转前图片:
![ticket_0](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_0.png)
- 旋转后图片效果如下：
![rotate_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/rotate_result.png)

#### 3 多线程文字识别：
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


### 参考文章：
https://blog.csdn.net/dqcfkyqdxym3f8rb0/article/details/89819785#comments
https://www.jianshu.com/p/9eb9d6f6f837
https://www.jianshu.com/p/173d329afa3a
https://blog.csdn.net/zhouguangfei0717/article/details/103026139/
https://blog.csdn.net/u014133119/article/details/82222656
https://blog.csdn.net/wsp_1138886114/article/details/83374333
以上文章供参考，并不一定是最好的，建议根据相关关键字进一步去搜索。


### 开源算法
#### 1. sdk使用的开源算法
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)

#### 2. 模型如何导出 ?
(readme.md 里提供了推理模型的下载链接)
- [export_model](https://github.com/PaddlePaddle/PaddleOCR/blob/release%2F2.5/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)


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