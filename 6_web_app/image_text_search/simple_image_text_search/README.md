### 目录：
http://aias.top/

### 下载模型，更新 image_text_search的yaml配置文件
- 链接: https://pan.baidu.com/s/1bc3MGsZjWz6TKFmSMUn5Ow?pwd=33b3
```bash
# Model URI
model:
  # Embedding Model
  textModel: /Users/calvin/ai_projects/image_text_search/image_text_search/image_text_search/models/M-BERT-Base-ViT-B/M-BERT-Base-ViT-B.pt
  imageModel: /Users/calvin/ai_projects/image_text_search/image_text_search/image_text_search/models/CLIP-ViT-B-32-IMAGE.pt
```

### 图像&文本的跨模态相似性比对检索【支持40种语言】
#### 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作

#### 功能介绍
- 以图搜图：上传图片搜索
- 以文搜图：输入文本搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


#### 背景介绍
OpenAI 发布了两个新的神经网络：CLIP 和 DALL·E。它们将 NLP（自然语言识别）与 图像识别结合在一起，对日常生活中的图像和语言有了更好的理解。
之前都是用文字搜文字，图片搜图片，现在通过CLIP这个模型，可是实现文字搜图片，图片搜文字。其实现思路就是将图片跟文本映射到同一个向量空间。如此，就可以实现图片跟文本的跨模态相似性比对检索。      
- 特征向量空间（由图片 & 文本组成）    
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

#### CLIP - “另类”的图像识别
目前，大多数模型学习从标注好的数据集的带标签的示例中识别图像，而 CLIP 则是学习从互联网获取的图像及其描述, 即通过一段描述而不是“猫”、“狗”这样的单词标签来认识图像。
为了做到这一点，CLIP 学习将大量的对象与它们的名字和描述联系起来，并由此可以识别训练集以外的对象。
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip.png)
如上图所示，CLIP网络工作流程： 预训练图编码器和文本编码器，以预测数据集中哪些图像与哪些文本配对。
然后，将CLIP转换为zero-shot分类器。此外，将数据集的所有分类转换为诸如“一只狗的照片”之类的标签，并预测最佳配对的图像。

#### 支持的语言列表：
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/languages.png)

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
            root   /Users/calvin/Documents/image_text_search/dist/;
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
    path: file:/D:/aias/file/
    rootPath: file:/D:/aias/file/data_root/
    ...
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

java -jar image-text-search-0.1.0.jar

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

#### 3.2 跨模态搜索 - 文本搜图
  输入文字描述，点击查询，可以看到返回的图片清单，根据相似度排序。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/mini_search_1.png)

#### 3.3 跨模态搜索 - 以图搜图
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
