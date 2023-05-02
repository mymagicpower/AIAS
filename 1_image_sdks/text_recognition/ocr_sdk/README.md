
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


### Open source algorithms
#### Open source algorithms used by SDK
- [PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)

