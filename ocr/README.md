# OCR 自定义模板识别

文字识别（OCR）目前在多个行业中得到了广泛应用，比如金融行业的单据识别输入，餐饮行业中的发票识别，
交通领域的车票识别，企业中各种表单识别，以及日常工作生活中常用的身份证，驾驶证，护照识别等等。
OCR（文字识别）是目前常用的一种AI能力。
一般OCR的识别结果是一种按行识别的结构化输出，能够给出一行文字的检测框坐标及文字内容。
但是我们更想要的是带有字段定义的结构化输出，由于表单还活着卡证的多样性，全都预定义好是不现实的。
所以，设计了自定义模板的功能，能够让人设置参照锚点（通过锚点匹配定位，图片透视变换对齐），以及内容识别区
来得到key-value形式的结构化数据。
当前精简试用版(无数据库，redis等)包含了下面3个功能菜单：
1. 模板自定义
2. 基于模板识别
3. 自由文本识别
（目前需要图片都是摆正的，即没有旋转角度，自动转正功能在优化中。）

## 环境
* JDK 11 或以上版本。

ubuntu:

```bash
sudo apt-get install openjdk-11-jdk-headless
```
For centos: （建议centos8）

```bash
sudo yum install java-11-openjdk-devel
```
For macOS:

```bash
brew tap homebrew/cask-versions
brew update
brew cask install adoptopenjdk11
```

或者下载安装：[Oracle JDK](https://www.oracle.com/technetwork/java/javase/overview/index.html)


## 前端部署

#### 下载安装：
[OCR_UI](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/dist.zip)

#### nginx部署运行：
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
#### nginx部署运行：
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_dist.png){:height="50%" width="50%"}

```bash
# 后端服务器参数配置 - 域名:端口 或者 IP:端口（端口不要变）
 window.g = {
  Base_URL: 'http://127.0.0.1:8089',
}
```

## 后端部署

#### 下载jar包（因为使用了opencv，jar包有点大）：
[jar包](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/aais-ocr-demo.jar)   
 

```bash
# 运行程序（模板配置文件，模板图片存放于当前目录）

java -javaagent:aais-ocr-demo.jar -jar aais-ocr-demo.jar

```

#### 样例文件
[样例模板图片](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ticket.jpeg)   
[样例模板文件](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/lable-data.json)  
```bash
# 模板配置文件路径
# lable-data.json 存放于当前目录（与jar包同目录）
# 模板图片存储路径
#   mac:
#     path: ./file/
#   linux:
#     path: ./file/
#   windows:
#     path: C:\ocr\file\
```

## 打开浏览器

输入地址： http://localhost:8080

#### 1. 标注模板:
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_anchor.png){:height="50%" width="50%"}

```bash
# 参照锚点设置规则：
1. 框选4个及以上(最少4个)参照字段，并尽量分散（向四角方向）
2. 参照锚点必须是位置固定不变，文字固定不变
3. 单个参照字段不可跨行，且中间没有大片空白
4. 参照锚点文字内容需唯一，即不会重复出现的文字
```

![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_content.png){:height="50%" width="50%"}

```bash
# 内容识别区设置规则：
1. 识别结果以<key,value>形式展示，字段名需手工设置
2. 字段名需使用有业务意义的字母数字组合，如：name，age, address
3. 字段名不能含有特殊字符及空格
```

#### 2. 基于模板文字识别:
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_recognize.png){:height="50%" width="50%"}

#### 3. 自由文本识别:  
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/images/ocr_freetxt.png){:height="50%" width="50%"}
  
#### 4. 接口文档:  
http://127.0.0.1:8089/swagger-ui.html
  
## 完善中的功能：
```bash
1. 图片的自动转正对齐
2. 表格识别
```
