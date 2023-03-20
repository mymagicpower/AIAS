
### Download the model, place it in the models directory, and unzip
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/text_recognition_models.zip

## Text recognition (OCR) toolbox

Text recognition (OCR) is currently widely used in multiple industries, such as document recognition input in the financial industry, invoice recognition in the catering industry, ticket recognition in the transportation field, various form recognition in enterprises, and identification card, driver's license, passport recognition commonly used in daily work and life. OCR (text recognition) is a commonly used AI capability.

### OCR Toolbox Functions:

### 1. Direction detection

- OcrDirectionExample
- 0 degrees
- 90 degrees
- 180 degrees
- 270 degrees

  ![detect_direction](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/detect_direction.png)

### 2. Image rotation

- RotationExample

### 3. Text recognition (supports skewed text natively, 1 & 2 can be used as auxiliary when needed)

- OcrV3RecognitionExample

### 4. Image rotation

### 5. Layout analysis (supports 5 categories, used for pipeline processing of text recognition and table recognition)

- Text
- Title
- List
- Table
- Figure

```text
    # Layout analysis model URI
    layout: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ppyolov2_r50vd_dcn_365e_publaynet_infer.zip
```
### 6. Table recognition

- Generate HTML table
- Generate Excel file

```text
    # Table recognition model URI
    table-en: https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/en_table.zip
```

### Run OCR recognition examples
### 1.1 Text recognition:

- Example code: OcrV3RecognitionExample.java
- After running successfully, you should see the following output on the command line:

```text
time: 766
time: 2221
烦恼！
无数个
吃饱了就有
烦恼
没有吃饱只有一个
```

- Output image effect is as follows:
![text_with_angle](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/text_with_angle.png)


### 2. Image rotation:
Each call to the rotateImg method rotates the image counterclockwise by 90 degrees.

- Example code: RotationExample.java
- Image before rotation:
- 
![ticket_0](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket_0.png)
- The resulting image after rotation is as follows:
![rotate_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/rotate_result.png)

#### 3. Multi-threaded text recognition:
- Example code: OcrV3MultiThreadRecExample.java

#### 4. Layout analysis:
- After running successfully, you should see the following output on the command line:
```text
[INFO ] - [
	class: "Text", probability: 0.98750, bounds: [x=0.081, y=0.620, width=0.388, height=0.183]
	class: "Text", probability: 0.98698, bounds: [x=0.503, y=0.464, width=0.388, height=0.167]
	class: "Text", probability: 0.98333, bounds: [x=0.081, y=0.465, width=0.387, height=0.121]
	class: "Figure", probability: 0.97186, bounds: [x=0.074, y=0.091, width=0.815, height=0.304]
	class: "Table", probability: 0.96995, bounds: [x=0.506, y=0.712, width=0.382, height=0.143]
]
```
- Output image effect is as follows:
![layout](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/layout_detect_result.jpeg)

#### 5. Table recognition:
- After running successfully, you should see the following output on the command line:
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

- Output image effect is as follows:
![table](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/table.jpeg)

- The generated Excel file looks like this:
![excel](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/excel.png)


### Open source algorithms
#### 1. Open source algorithms used by SDK
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)

#### 2. How to export models?
- [export_model](https://github.com/PaddlePaddle/PaddleOCR/blob/release%2F2.5/tools/export_model.py)
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
