
# Image Preprocessing SDK
In OCR text recognition, the images we get are generally not straight, and there is always some degree of skewness. Therefore, it is necessary to straighten the images. Also, the images may be shot from a perspective angle and need to be re-corrected.

### SDK Features

- Image straightening
- Classic algorithms such as image binarization, grayscale, and denoising.

### Features under development:

- Improve perspective correction
- Improve text direction detection algorithm to determine the angle of the image after straightening, so as to further rotate the image horizontally to make the text horizontal.

## Running Example

After successful execution, the following information should be displayed on the command line:

```text
319.0 , 865.0
319.0 , 113.0
785.0 , 113.0
785.0 , 865.0
startLeft = 319
startUp = 113
width = 467
height = 753
```
The output image effect is as follows:
![ocr_result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_sdk/images/rotation.png)
