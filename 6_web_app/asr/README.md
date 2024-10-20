### 目录：
https://www.aias.top/


### 下载模型，更新 asr_backend 的yaml配置文件
- 链接: https://pan.baidu.com/s/12DB6Z-f6Akdz8OxsPuoj9A?pwd=6irx

```bash
model:
  # 如果是CPU推理，最高设置为 CPU 核心数 (Core Number)
  poolSize: 2
  # tiny, base, small
  type: base
  uri:
    # tiny 模型
    tiny: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_tiny.pt
    # base 模型
    base: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_base.pt
    # small 模型
    small: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_small.pt
```

### 语音识别
本例子提供了英文语音识别，中文语音识别。

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
            root   /Users/calvin/Documents/asr/dist/;
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

## 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+

- application.yml   
```bash
model:
  # 如果是CPU推理，最高设置为 CPU 核心数 (Core Number)
  poolSize: 2
  # 提供了三个模型从小到大：tiny, base, small，模型越大速度越慢，精度越高
  type: base
  uri:
    # tiny 模型
    tiny: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_tiny.pt
    # base 模型
    base: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_base.pt
    # small 模型
    small: D:\\ai_projects\\asr\\asr_backend\\models\\traced_whisper_small.pt
```


#### 2.2 运行程序：
```bash
# 运行程序

java -jar aias-asr-0.1.0.jar

```

## 3. 打开浏览器
- 输入地址： http://localhost:8090

#### 3.1. 英文语音识别
- 1). 输入音频文件地址，在线识别
- 2). 点击上传按钮，上传音频文件识别
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_en.png"  width = "600"/>
</div> 

#### 3.2. 中文语音识别
- 1). 输入音频文件地址，在线识别
- 2). 点击上传按钮，上传音频文件识别
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_zh.png"  width = "600"/>
</div> 

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_swagger.png"  width = "600"/>
</div> 

#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html