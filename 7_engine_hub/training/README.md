# 该项目仅用于学习研究使用，项目落地建议使用Python训练框架,如：paddlepaddle, pytorch, tensorflow等等。

# AI 训练平台
AI训练平台提供分类模型训练能力。并以REST API形式为上层应用提供接口。
当前版包含功能如下：
-分类模型训练 （imagenet数据集预训练的resnet50模型）
-模型训练可视化
-图片分类推理
-图片特征提取（512维特征）
-图片 1:1 比对

## 前端部署

#### 下载安装：
[platform-train-ui](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/dist.zip)

#### nginx部署运行：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/platform/dist/;
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

#### 配置hosts文件：
```bash
# 客户端(浏览器)机器的hosts文件添加映射< 127.0.0.1	train.aias.me>, 
# 其中127.0.0.1替换成jar包运行的服务器ip地址

127.0.0.1	train.aias.me
```

## 后端部署

#### 下载jar包：
[jar包](https://aias-home.oss-cn-beijing.aliyuncs.com/jars/aais-platform-train-0.1.0.jar)   
 

```bash
# 运行程序

java -jar aais-platform-train-0.1.0.jar

```

## 打开浏览器

输入地址： http://localhost:8080

#### 1. 训练数据准备-ZIP格式压缩包:
压缩包内需包含2个目录（名字需严格一致）：    
-TRAIN ：包含训练数据，每个文件夹对应一个分类（每个分类图片数量尽量保持平衡）    
-VALIDATION ：包含验证数据，每个文件夹对应一个分类

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/folder.png)

-[320张车辆图片测试数据下载](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/Cars_320.zip)   

#### 2. 上传数据并开始训练:
-选择zip文件并上传
-点击训练按钮开始训练
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/storage.png)

#### 3. 查看训练过程:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png)
  
#### 4. 图片分类测试:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/classification.png)
  
#### 5. 特征提取测试: 
图片特征提取使用的是新训练的模型。特征来自模型的特征提取层。 
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/feature.png)

#### 6. 图片比对测试:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/comparision.png)

#### 7. API文档： 
http://127.0.0.1:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/swagger.png)
  
#### 编辑jar包中的application.yml
根据需要编辑application.yml中的图片上传路径,模型保存路径
（windows环境可以使用7-zip直接编辑，无需对jar包解压缩重新压缩）
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
    imageRootPath: ~/file/image_root/ #压缩包解压缩文件夹
    newModelPath: ~/file/model/ #模型训练好后存放的文件夹
  linux:
    path: /home/aias/file/
    imageRootPath: /home/aias/file/image_root/ #压缩包解压缩文件夹
    newModelPath: /home/aias/file//model/ #模型训练好后存放的文件夹
  windows:
    path: C:\aias\file\
    imageRootPath: C:\aias\file\image_root\ #压缩包解压缩文件夹
    newModelPath: C:\aias\file\modelv2\ #模型训练好后存放的文件夹
  # 文件大小 /M
  maxSize: 3000
```

### 如何将训练的模型用于图像搜索引擎？
训练的模型可以用于图像搜索引擎的通用图像搜索，替换已有的模型，提升搜索引擎的精度。     
图像搜索引擎：    
https://gitee.com/mymagicpower/AIAS/tree/main/image_search_cpu

#### 1. 找到训练好的模型文件：（newModelPath: ~/file/model/ #模型训练好后存放的文件夹）
-new_resnet_50-0001.params
-synset.txt
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/model.png)

#### 2. 配置搜索引擎参数：
1). 修改参数 newModel.enabled，更新为true
```bash
#是否开启自训练模型
newModel:
  enabled: true
```
2). 复制训练好的模型文件到搜索引擎的指定目录（newModelPath指定的路径）
（windows环境可以使用7-zip对jar包里的classes/config/application-dev.yml进行编辑）
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/params.png)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
  
