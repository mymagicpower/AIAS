### 目录：
https://www.aias.top/

### 下载模型,配置yml文件 image_backend\src\main\resources\application-xxx.yml
- 链接: https://pan.baidu.com/s/1zm8tN94p2UgUS3HN0NTHRA?pwd=uzfy
```bash
model:
  # 设备类型 cpu gpu
  device: cpu
  # 最大设置为 CPU 核心数 (Core Number)
  poolSize: 4
  # 遮罩层
  mask: false
  # 模型路径
  modelPath: D:\\ai_projects\\products\\1_image_sdks\\seg_unet_sdk\\models\\
  # 通用分割模型
  bigModelName: u2net.onnx
  middleModelName: silueta.onnx
  smallModelName: u2netp.onnx
  # 人体分割模型
  humanModelName: human.onnx
  # 动漫分割模型
  animeModelName: anime.onnx
  # 衣服分割模型
  clothModelName: cloth.onnx
```

### 一键抠图

一键抠图是一种图像处理技术，旨在自动将图像中的前景对象从背景中分离出来。它可以帮助用户快速、准确地实现抠图效果，无需手动绘制边界或进行复杂的图像编辑操作。
一键抠图的原理通常基于计算机视觉和机器学习技术。它使用深度神经网络模型，通过训练大量的图像样本，学习如何识别和分离前景对象和背景。这些模型能够理解图像中的像素信息和上下文，并根据学习到的知识进行像素级别的分割。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_all.png)

使用一键抠图可以带来许多实际用途，包括但不限于以下几个方面：
1. 图像编辑：一键抠图可以用于图像编辑软件中，帮助用户轻松地将前景对象从一个图像中提取出来，并将其放置到另一个图像或背景中，实现合成效果。
2. 广告设计：在广告设计中，一键抠图可以用于创建吸引人的广告素材。通过将产品或主题从原始照片中抠出，可以更好地突出产品或主题，并与其他元素进行组合。
3. 虚拟背景：一键抠图可以用于视频会议或虚拟现实应用中，帮助用户实现虚拟背景效果。通过将用户的前景对象从实际背景中抠出，可以将其放置在虚拟环境中，提供更丰富的交互体验。
4. 图像分析：一键抠图可以用于图像分析和计算机视觉任务中。通过将前景对象与背景分离，可以更好地理解和分析图像中的不同元素，如目标检测、图像分类、图像分割等。

当前版本包含了下面功能：
- 1. 通用一键抠图
- 2. 人体一键抠图
- 3. 动漫一键抠图



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


#### 1. 通用一键抠图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_general.jpg)

#### 2. 人体一键抠图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_human.jpg)

#### 3. 动漫一键抠图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_anime.jpg)



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