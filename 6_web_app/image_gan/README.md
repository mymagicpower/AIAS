### 目录：
https://www.aias.top/

### 下载模型,配置yml文件 image_backend\src\main\resources\application-xxx.yml
- 链接: https://pan.baidu.com/s/1H1EQvrgMfb07pXyyHsFQXQ?pwd=bpm1
```bash
model:
  # 设备类型 cpu gpu
  device: cpu
  # 最大设置为 CPU 核心数 (Core Number)
  poolSize: 1
  # 模型路径
  modelPath: D:\\ai_projects\\products\\4_apps\\image_gan\\image_backend\\models\\
  # 人脸检测模型
  faceModelName: retinaface_traced_model.pt
  # 人像分割模型
  faceSegModelName: parsenet_traced_model.pt
  # 人脸修复模型
  faceGanModelName: gfpgan_traced_model.pt
  # 图像超分模型
  srModelName: realsr_traced_model.pt
```


### 图像增强
图像超分辨是指通过使用计算机算法将低分辨率图像转换为高分辨率图像的过程。在图像超分辨的任务中，我们希望通过增加图像的细节和清晰度来提高图像的质量。
图像超分辨的挑战在于从有限的信息中恢复丢失的细节。低分辨率图像通常由于传感器限制、图像压缩或其他因素而失去了细节。图像超分辨技术通过利用图像中的上下文信息和统计特征来推测丢失的细节。
目前，有多种图像超分辨方法可供选择，包括基于插值的方法、基于边缘的方法、基于学习的方法等。其中，基于学习的方法在图像超分辨领域取得了显著的进展。这些方法使用深度学习模型，如卷积神经网络（CNN）或生成对抗网络（GAN），通过训练大量的图像样本来学习图像的高频细节和结构，从而实现图像超分辨。
图像超分辨技术在许多领域都有应用，包括医学影像、安防监控、视频增强等。它可以改善图像的视觉质量，提供更多细节和清晰度，有助于改善图像分析、图像识别和人机交互等任务的准确性和效果。
人工智能图片人脸修复是一种应用计算机视觉技术和深度学习算法进行图像修复的方法。这种技术可以自动识别图像中的人脸，并进行修复和还原，从而使图像更加完整、清晰和自然。相较于传统的图像修复方法，人工智能图片人脸修复更加高效和准确。它可以快速地修复照片中的缺陷，例如面部皮肤瑕疵、眼睛或嘴巴的闭合问题等，使其看起来更加美观自然。这种技术在图像处理、医学影像、电影制作等领域都有着广泛的应用前景，并且随着人工智能技术的不断发展，其应用领域也会越来越广泛。
  

当前版本包含了下面功能：
- 图片一键高清: 提升图片4倍分辨率。
- 头像一键高清
- 人脸一键修复


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
            root   /Users/calvin/image_ui/dist/;
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


#### 1. 图片一键高清 (提升图片4倍分辨率)
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/imageSr.png)

#### 2. 头像一键高清
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceGan.png)

#### 3. 人脸一键修复
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/faceSr.png)



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