## 目录：
http://www.aias.top/

## OCR 自定义模板识别(支持表格识别)

文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。
一般OCR的识别结果是一种按行识别的结构化输出，能够给出一行文字的检测框坐标及文字内容。
但是我们更想要的是带有字段定义的结构化输出，由于表单还活着卡证的多样性，全都预定义好是不现实的。
所以，设计了自定义模板的功能，能够让人设置参照锚点（通过锚点匹配定位，图片透视变换对齐），以及内容识别区
来得到key-value形式的结构化数据。

当前精简试用版(无数据库，redis等)包含了下面功能：
1. 模板自定义
2. 基于模板识别
3. 自由文本识别
4. 表格文本识别（图片需是剪切好的单表格图片）
5. 表格自动检测文本识别（支持表格文字混编，自动检测表格识别文字，支持多表格）
（需要图片都是摆正的，即没有旋转角度。）

### 环境
* JDK 1.8 或以上版本。

### 1. 前端部署

#### 1.1 直接运行：
```bash
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境为例)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/ocr/dist/;
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

### 2. 后端部署

#### 2.1 jar包
构建jar包 
 
#### 2.2 运行程序
```bash
java -jar iocr-demo-0.1.0.jar
```

#### 2.3 样例文件
[样例模板图片](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ticket.jpeg)   
```bash
# 模板配置文件路径
# <path>/templates
# 模板图片存储路径
# <path>/images

# path 路径信息
#   mac:
#     path: ./file/
#   linux:
#     path: ./file/
#   windows:
#     path: C:\ocr\file\
```

## 3. 功能测试

#### 3.1 打开浏览器
输入地址： http://localhost:8080

#### 3.2 标注模板
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_anchor.png)

```bash
# 参照锚点设置规则：
1. 建议框选4个及以上(最少3个)参照字段，并尽量分散（向四角方向）
   1). 如果匹配4个及以上的锚点框，则进行透视变换
   2). 如果匹配3个锚点框，则进行仿射变换
   3). 如果匹配的锚点少于三个则直接根据相对坐标计算
2. 参照锚点必须是位置固定不变，文字固定不变
3. 单个参照字段不可跨行，且中间没有大片空白
4. 参照锚点文字内容需唯一，即不会重复出现的文字
```
注意：
锚点可以不设置，但是要求模板图片和候选待检测图片必须是无背景的图片。
所以需要对图片预处理，检测抠图，转正。图片大小无需一致，且横向和纵向可以适度拉伸（因为根据相对坐标计算，所以横纵放缩不影响，只要能识别文字）。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_content.png)

```bash
# 内容识别区设置规则：
1. 识别结果以<key,value>形式展示，字段名需手工设置
2. 字段名需使用有业务意义的字母数字组合，如：name，age, address
3. 字段名不能含有特殊字符及空格
```

#### 3.3 基于模板文字识别
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_recognize.png)

#### 3.4 通用文本识别  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_freetxt.png)
 
#### 3.5 表格文字识别 / 表格自动检测文字识别
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/single_table.png)

  
### 4. 接口文档  
http://127.0.0.1:8089/swagger-ui.html

### 5. Change List:
- 2021-10-24:
1. 模板自动变换 (考虑到文字识别错误导致匹配不上)
   1). 如果匹配4个及以上的锚点框，则进行透视变换
   2). 如果匹配3个锚点框，则进行仿射变换
   3). 如果匹配的锚点少于三个则直接根据相对坐标计算
2. 距离计算方式可配置
   1). L2距离计算，计算最小距离匹配字段
   2). IoU距离计算，计算锚点框与检测框的交并比（无交集，则为空）
3. 增加模板匹配的debug功能，输出变换后的图片效果，及变换坐标后的匹配效果图。
  
  
### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)  