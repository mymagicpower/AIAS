### 目录：
http://aias.top/

### 下载模型
- 链接: https://pan.baidu.com/s/1QKUSP7IaIY3U3pK6cqvdCg?pwd=g75s

### 以图搜图【无向量引擎版】
#### 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作

#### 功能介绍
- 以图搜图：上传图片搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


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
            root   /Users/calvin/Documents/image_search/dist/;
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
#### 2.1 环境要求：
- 系统JDK 1.8+

- application.yml   
1). 根据需要编辑图片上传根路径rootPath    
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
    # folder for unzip files
    rootPath: ~/file/data_root/
  linux:
    path: /home/aias/file/
    rootPath: /home/aias/file/data_root/
  windows:
    path: C:\\
    rootPath: C:\\data_root\\
    ...
```
1). 更新模型路径   
```bash
# Model URI
model:
  # Embedding Model
  imageModel: /Users/calvin/products/4_apps/simple_image_search/image_search/models/CLIP-ViT-B-32-IMAGE.pt
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
```


2). 根据需要编辑图片baseurl 
```bash
image:
  #baseurl是图片的地址前缀
  baseurl: http://127.0.0.1:8089/files/
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar image-search-0.1.0.jar

```

### 3. 功能使用
打开浏览器
- 输入地址： http://localhost:8090

#### 3.1 图片上传
1). 点击上传按钮上传文件.  
[测试图片数据](https://pan.baidu.com/s/1QtF6syNUKS5qkf4OKAcuLA?pwd=wfd8)
2). 点击特征提取按钮. 
等待图片特征提取，特征存入json文件。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/mini_search_3.png)

#### 3.3 以图搜图
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/mini_search_2.png)

### 4. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_search/swagger.png)


### 官网：
[官网链接](http://www.aias.top/)


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
