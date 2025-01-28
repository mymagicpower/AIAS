### 目录：
https://www.aias.top/


### API 能力平台
提供开箱即用的人工智能能力平台。

####  当前版本包含了下面功能：
1. 自由文本识别（支持旋转、倾斜的图片）
2. 文本图片转正 （一般情况下不需要，因为ocr 原生支持旋转、倾斜的图片 ）
3. 机器翻译
4. 语音识别

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>OCR文字识别</p>   
          - 自由文本识别（支持旋转、倾斜的图片）<br>
          - 文本图片转正 （一般情况下不需要，因为ocr 原生支持旋转、倾斜的图片 ）<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/table.jpg" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>语音识别</p>   
          - 英文语音识别，<br>
          - 中文语音识别。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_zh.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>202种语言互相翻译</p>    
          - 支持202种语言互相翻译<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/assets/nllb.png" width = "400px"/>
        </div>
      </td>
    </tr>                                            
  </table>
</div>




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

### 2. 后端部署
#### 2.1 环境要求：
- 系统JDK 1.8+，建议11

#### 2.2 下载模型：
```bash
### 模型下载地址：
链接: https://pan.baidu.com/s/1YosO46RhK11kKO8rK0q6SA?pwd=4ih8

### 假设系统为linux,假设路径如下：
/home/models/

```

#### 2.3 更新模型地址：
```bash
### 1. 设置配置yml文件：
api_platform\src\main\resources\application.yml
spring:
  profiles:
    active: mac


### 2. 选择系统配置文件，替换成实际的模型路径，以linux为例：application-linux.yml
model:
  ......
  ocrv4:
    # server detection model URI
    det: /home/models/ocr/ch_PP-OCRv4_det_infer.zip
    # server recognition model URI
    rec: /home/models/ocr/ch_PP-OCRv4_rec_infer.zip

```


### 3. 运行程序：
运行编译后的jar：
```bash
# 运行程序  
# -Dfile.encoding=utf-8 参数可以解决操作系统默认编码导致的中文乱码问题
nohup java -Dfile.encoding=utf-8 -jar xxxxx.jar > log.txt 2>&1 &
```

### 4. 打开浏览器
- 输入地址： http://localhost:8089


#### 1. 通用文本识别  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/free.jpg)
 
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocrweb_free.jpg)


#### 2. 文本转正
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocrweb_mlsd.jpg)




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