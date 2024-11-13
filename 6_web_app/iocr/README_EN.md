### 目录：
https://www.aias.top/

### 载模型, 更新配置yml文件 ocr_backend\src\main\resources\application-xxx.yml
- 链接：https://pan.baidu.com/s/1-OEOcYHjSeqbfu7XD3ASgw?pwd=f43t

```bash
model:
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
  ocrv4:
    # server detection model URI
    det: D:\\ai_projects\\products\\4_apps\\iocr\\ocr_backend\\models\\ch_PP-OCRv4_det_infer.zip
    # server recognition model URI
    rec: D:\\ai_projects\\products\\4_apps\\iocr\\ocr_backend\\models\\ch_PP-OCRv4_rec_infer.zip
  mlsd:
    # mlsd model URI
    model: D:\\ai_projects\\AIAS\\6_web_app\\ocr_web_app\\ocr_backend\\models\\mlsd_traced_model_onnx.zip
```

### OCR 自定义模板识别

文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。
一般OCR的识别结果是一种按行识别的结构化输出，能够给出一行文字的检测框坐标及文字内容。
但是我们更想要的是带有字段定义的结构化输出，由于表单还活着卡证的多样性，全都预定义好是不现实的。
所以，设计了自定义模板的功能，能够让人设置参照锚点（通过锚点匹配定位，图片透视变换对齐），以及内容识别区
来得到key-value形式的结构化数据。

当前版本包含了下面功能：
1. 模板自定义
2. 基于模板识别（支持旋转、倾斜的图片）
3. 自由文本识别
4. 文本转正



### 1. 前端部署

#### 1.1 直接运行：
```bash
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境部署管理前端为例)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/ocr_ui/dist/;
            index  index.html index.htm;
        }
     ......
     
# 重新加载配置：
sudo nginx -s reload 

# 部署应用后，重启：
cd /usr/local/Cellar/nginx/1.19.6/bin

# 快速停止
sudo nginx -s stop

# 启动
sudo nginx     
```

### 2. 后端jar部署
#### 环境要求：
- 系统JDK 1.8+，建议11

### 3. 运行程序：
运行编译后的jar：
```bash
# 运行程序
nohup java -Dfile.encoding=utf-8 -jar xxxxx.jar > log.txt 2>&1 &
```

### 4. 打开浏览器
- 输入地址： http://localhost:8089

#### 1. 自定义模板 - 参照锚点设置
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_anchor.jpeg)

#### 2. 自定义模板 - 内容识别区设置
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_content.jpeg)

#### 3. 基于模板文字识别
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_rec.jpeg)

#### 4. 通用文本识别
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_freetxt.jpeg)

#### 5. 文本转正
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocrweb_mlsd.jpg)



#### 使用建议：
- 1. 请先用 ocr sdk 识别文字，查看自动检测的文本框位置，及识别文字的精度
- 2. 标注模板不能成功匹配，主要有两点原因：
- 1）标注的文本框位置，与自动检测的文本框位置不一致，所以请参考上面建议，先运行sdk查看自动检测的效果
- 2）另一个原因是，文字识别精度不够（可能是图片文字过于模糊，也可能是算法本身精度不够）

#### 待修复的Bug：
- 1. 模板标注文本框的时候，需要从左上角向右下角拉框（代码没有自动排序，所以如果从右下角往左上角拉框会报错）

#### 下一步的改进功能：
- 1. 不使用锚点匹配，而是通过文本转正对齐，将表单对齐，然后根据内容区域IoU，判断匹配度。解决锚点匹配困难的问题。



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