## 文字识别（OCR）工具箱
文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。

### OCR工具箱功能:
1. 方向检测   
- 0度   
- 90度   
- 180度   
- 270度   
![detect_direction](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_direction.png)

2. 图片旋转

3. 文字识别(提供三个模型)
- mobile模型   
- light模型   
- 服务器端模型 
  
4. 版面分析（支持5个类别, 用于配合文字识别，表格识别的流水线处理）
- Text
- Title
- List
- Table
- Figure

5. 表格识别
- 生成html表格
- 生成excel文件


### 运行OCR识别例子
#### 1.1 文字方向检测：
- 例子代码: OcrDetectionExample.java    
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

#### 1.2 文字方向检测帮助类（增加置信度信息显示，便于调试）：
- 例子代码: OcrDetectionHelperExample.java 
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Result image has been saved in: build/output/detect_result_helper.png
[INFO ] - [
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.073, y=0.069, width=0.275, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.652, y=0.158, width=0.222, height=0.040]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.143, y=0.252, width=0.144, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.628, y=0.328, width=0.168, height=0.026]
	class: "0 :1.0", probability: 1.00000, bounds: [x=0.064, y=0.330, width=0.450, height=0.023]
]
```
- 输出图片效果如下：
![detect_result_helper](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_result_helper.png)

#### 2. 图片旋转：
每调用一次rotateImg方法，会使图片逆时针旋转90度。
- 例子代码: RotationExample.java 
- 旋转前图片:
![ticket_0](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_0.png)
- 旋转后图片效果如下：
![rotate_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/rotate_result.png)

#### 3. 文字识别：
再使用本方法前，请调用上述方法使图片文字呈水平(0度)方向。  
- 例子代码: LightOcrRecognitionExample.java  
- 运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "你", probability: -1.0e+00, bounds: [x=0.319, y=0.164, width=0.050, height=0.057]
	class: "永远都", probability: -1.0e+00, bounds: [x=0.329, y=0.349, width=0.206, height=0.044]
	class: "无法叫醒一个", probability: -1.0e+00, bounds: [x=0.328, y=0.526, width=0.461, height=0.044]
	class: "装睡的人", probability: -1.0e+00, bounds: [x=0.330, y=0.708, width=0.294, height=0.043]
]
```
- 输出图片效果如下：
![ocr_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_result.png)

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
![excel](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/excel.jpeg)

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   