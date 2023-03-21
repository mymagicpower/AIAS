
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/iocr.zip

## OCR custom template recognition (supports table recognition)

Text recognition (OCR) is currently widely used in multiple industries, such as document recognition input in the financial industry, invoice recognition in the catering industry, ticket recognition in the transportation field, various form recognitions in enterprises, and identification card, driver's license, passport recognition commonly used in daily work and life. OCR (text recognition) is currently a commonly used AI capability.

The general OCR recognition result is a structured output recognized by line, which can give the detection frame coordinate and text content of a line of text. However, what we want more is the structured output with field definition. Due to the diversity of forms or certificates, it is unrealistic to predefine all of them. Therefore, the function of custom templates is designed, which can allow people to set reference anchor points (positioning through anchor point matching, image perspective transformation alignment), as well as content recognition areas to obtain structured data in key-value form.

The current simplified trial version (without database, redis, etc.) includes the following functions:

1. Template customization
2. Recognition based on templates
3. Free text recognition
4. Table text recognition (the image needs to be a clipped single table image)
5. Table automatic detection text recognition (supports table text mixing, automatically detects table recognition text, and supports multiple tables)
   (The images need to be straightened, that is, without rotation angles.)

### Environment

- JDK 1.8 or above.

### 1. Front-end deployment

### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 Deploy and run nginx (mac environment is used as an example):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/ocr/dist/;
index  index.html index.htm;
        }
......

# Reload the configuration:
sudo nginx -s reload

# After deploying the application, restart it:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Fast stop
sudo nginx -s stop

# Start
sudo nginx
```

### 2. Back-end deployment

### 2.1 jar package

Build a jar package

### 2.2 Run the program
```bash
java -jar iocr-demo-0.1.0.jar
```

#### 2.3 样例文件
[Sample template image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket.jpeg)   
```bash
# Template configuration file path
# <path>/templates
# Template image storage path
# <path>/images

# Path information
#   mac:
#     path: ./file/
#   linux:
#     path: ./file/
#   windows:
#     path: C:\\ocr\\file\\
```

## 3. Function test

### 3.1 Open the browser

Enter the address: [http://localhost:8080](http://localhost:8080/)

### 3.2 Mark up the template
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_anchor.png)

```bash
# Reference anchor point setting rules:
1. It is recommended to select and box 4 or more (at least 3) reference fields, and try to be dispersed (towards the four corners)
   1). If 4 or more matching anchor frames are selected, perspective transformation is performed
   2). If 3 matching anchor frames are selected, affine transformation is performed
   3). If the matched anchor is less than 3, it is directly calculated according to the relative coordinates
2. The reference anchor point must be a fixed position and fixed text
3. A single reference field cannot span multiple lines, and there is no large blank space in the middle
4. The text content of the reference anchor point needs to be unique, that is, the text that will not appear repeatedly
```
Note:
The anchor point can be left unset, but it requires that the template image and the candidate detection image must be images without a background. Therefore, image preprocessing is required, detection of cutouts, and straightening. The image size does not need to be consistent, and horizontal and vertical stretching can be moderately stretched (because the scaling does not affect according to the relative coordinates, as long as the text can be recognized).
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_content.png)

```bash
# Content recognition area setting rules:
1. The recognition result is displayed in <key, value> form, and the field name needs to be set manually
2. The field name needs to use alphanumeric combinations with business meaning, such as: name, age, address
3. The field name cannot contain special characters and spaces
```

#### 3.3 Text recognition based on templates
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_recognize.png)

#### 3.4 General text recognition
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_freetxt.png)
 
#### 3.5 Table text recognition/table automatic detection text recognition
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/single_table.png)

  
### 4. Interface documentation
http://127.0.0.1:8089/swagger-ui.html

