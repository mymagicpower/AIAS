### This project is only for study and research purposes. For project development, it is recommended to use Python training frameworks such as PaddlePaddle, PyTorch, TensorFlow, etc.

### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/resnet50_v2.zip

### AI Training Platform

The AI training platform provides classification model training capabilities and provides interfaces to upper-layer applications in the form of REST APIs. The current version includes the following functions:

- Classification model training (resnet50 model pre-trained with Imagenet dataset)
- Model training visualization
- Image classification inference
- Image feature extraction (512-dimensional feature)
- Image 1:1 comparison

## Front-end deployment

### nginx deployment operation:
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
#Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/platform/dist/;
index  index.html index.htm;
        }
......

#Reload configuration:
sudo nginx -s reload

#After deploying the application, restart:
cd /usr/local/Cellar/nginx/1.19.6/bin

#Fast stop
sudo nginx -s stop

#Start
sudo nginx

```

#### Configure the hosts file:
```bash
#Add the mapping <127.0.0.1 train.aias.me> to the hosts file of the client (browser) machine,
#where 127.0.0.1 is replaced with the IP address of the server where the JAR package is running.

127.0.0.1  train.aias.me
```

## Back-end deployment
```bash
# Compile & run the program
java -jar aais-platform-train-0.1.0.jar

```

## Open the browser

Enter the address: http://localhost:8080

#### 1. 训练数据准备-ZIP格式压缩包:
### 1. Training data preparation-ZIP format compression package:

The compressed package must contain 2 directories (named strictly the same):
-TRAIN: Contains training data, and each folder corresponds to a classification (try to keep the number of images in each classification as balanced as possible)
-VALIDATION: Contains validation data, and each folder corresponds to a classification

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/folder.png)

-[Download 320 vehicle image test data](https://github.com/mymagicpower/AIAS/releases/download/apps/Cars_320.zip)   

### 2. Upload data and start training:

- Select the zip file and upload it
- Click the train button to start training
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/storage.png)

#### 3. View the training process:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png)
  
#### 4. Image classification test:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/classification.png)
  
#### 5. Feature extraction test:
The image feature extraction uses the newly trained model. The features come from the feature extraction layer of the model.
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/feature.png)

#### 6. Image comparison test:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/comparision.png)

#### 7. API documentation:
http://127.0.0.1:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/swagger.png)

### Edit the application.yml in the JAR package

Edit the image upload path and model save path in the application.yml as needed
(Windows environment can use 7-zip to edit directly, no need to decompress and recompress the JAR package)

```bash
#File storage path
file:
  mac:
    path: ~/file/
    imageRootPath: ~/file/image_root/ #Folder for decompressing compressed files
    newModelPath: ~/file/model/ #Folder for storing models after training
  linux:
    path: /home/aias/file/
    imageRootPath: /home/aias/file/image_root/ #Folder for decompressing compressed files
    newModelPath: /home/aias/file//model/ #Folder for storing models after training
  windows:
    path: C:\\aias\\file\\
    imageRootPath: C:\\aias\\file\\image_root\\ #Folder for decompressing compressed files
    newModelPath: C:\\aias\\file\\modelv2\\ #Folder for storing models after training
  #File size / M
  maxSize: 3000
```
