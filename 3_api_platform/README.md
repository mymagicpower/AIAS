### 目录：
https://www.aias.top/

#### 模型下载：
- 链接:https://pan.baidu.com/s/16933J3dX16xnjbYaay-4og?pwd=cwxk

### 1. 非图像生成类模型下载【8.2G】：
```bash
# 假设系统为 windows , 假设下载后的路径如下：
D:\ai_projects\AIAS\3_api_platform\api-platform\models>:

2025/02/17  19:25    <DIR>          .
2025/02/18  17:18    <DIR>          ..
2025/02/18  13:27    <DIR>          asr
2025/02/08  15:20    <DIR>          color
2025/02/17  19:25    <DIR>          controlnet
2025/02/18  13:30    <DIR>          ocr
2025/02/06  13:51    <DIR>          seg
2025/02/06  12:13    <DIR>          sr
2025/02/18  13:21    <DIR>          trans
```

#### 1.1 更新配置文件 application.yml 的 active 属性，windows环境选择，win：
- 路径：api_platform\src\main\resources\application.yml
```bash
spring:
  profiles:
    # win - windows 环境
    # mac - Mac 环境
    # linux - Linux 环境
    # online - 模型在线加载
    active: win
```
#### 1.2 更新非生成类模型路径，以 windows 为例：application-win.yml
```bash
model:
  # 模型路径
  modelPath: D:\\ai_projects\\AIAS\\3_api_platform\\api-platform\\models\\
```

### 2. 图像生成类模型下载【可选】：总大小约 62G，需要再下载
- 模型默认是延迟加载【首次调用的时候加载模型，也就是说，不下载模型，不影响其它功能使用】
- 生成类模型很大，全部模型加载需要至少24G的显存
- 每次只使用一个图像生成功能，至少需要 4 G 显存
- 建议显存不大的情况下，一次不要点击多个图像生成的功能

- 链接: https://pan.baidu.com/s/1Agt84-DdykIO25hkWzvwRg?pwd=9g5r
```bash
# 假设系统为 windows , 假设下载后的路径如下：
H:\models\aigc>:

2023/08/30  16:36    <DIR>          .
2023/08/30  16:36    <DIR>          ..
2023/06/21  15:22    <DIR>          sd_cpu
2023/06/21  19:10    <DIR>          sd_gpu
```
#### 2.1 更新生成类模型路径，以 windows 为例：application-win.yml
```bash
model:
  sd:
    # 模型路径
    cpuModelPath: H:\\models\\aigc\\sd_cpu\\
    gpuModelPath: H:\\models\\aigc\\sd_gpu\\
```

#### 2.2 显卡配置
- 图像生成对显卡强依赖，CPU运行需要几分钟的时间才能生成一张图片
- 显卡CUDA版本：推荐 11.x 12.x 版本
- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。
- 开关参数路径：api_platform\src\main\resources\application.yml
```bash
model:
  # 设备类型 cpu gpu
  device: gpu
```



### API 能力平台
提供开箱即用的人工智能能力平台。

- Web应用，前端VUE，后端Springboot
- 可以直接部署使用，使用UI或者调用API集成到现有的系统中。
- 支持的能力清单：
```text
  1). OCR文字识别
  2). 机器翻译
  3). 语音识别
  4). 一键高清
  5). 一键抠图
  6). 黑白照片上色
      ...
```

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>OCR文字识别</p>   
          - 自由文本识别<br>支持旋转、倾斜的图片<br>
          - 文本图片转正 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/free.jpg" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>语音识别</p>   
          - 英文语音识别<br>
          - 中文语音识别
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
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 图片一键高清: <br>提升图片4倍分辨率<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/imageSr.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 头像一键高清<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceGan.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>图像增强</p>    
          - 人脸一键修复: <br>自动修复图中人脸<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceSr.png" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>通用一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_general.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>人体一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_human.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>动漫一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_anime.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>框选一键抠图</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_seg_sam2/sam2_seg1.jpg" width = "400px"/>
        </div>
      </td>
    </tr>      
    <tr>
      <td>
        <div align="left">
          <p>黑白照片上色</p>    
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/images/ap-images/ddcolor.jpg" width = "400px"/>
        </div>
      </td>
    </tr>                                                      
  </table>
</div>

<br/>
<hr>
<br/>




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