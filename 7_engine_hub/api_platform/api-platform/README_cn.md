
### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1-eUlAYPNXH3xVM1PDgWxbA?pwd=5cjq

### AI 赋能平台
AI赋能平台以REST API形式为上层应用提供接口。
当前CPU版包含功能如下：
1. 自由文本识别（目前需要图片都是摆正的，即没有旋转角度，自动转正功能在优化中。）
2. 人脸检测（返回检测框坐标，检测框坐标顺序：上右下左）
3. 人脸特征提取（512维特征）
4. 人脸 1:1 比对

## 前端部署

#### nginx部署运行：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/api-platform/dist/;
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
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/images/ocr_dist.png)

```bash
# 后端服务器参数配置 - 域名:端口 或者 IP:端口（端口不要变）
 window.g = {
  Base_URL: 'http://127.0.0.1:8089',
}
```

## 后端部署
```bash
# 编译 & 运行程序

java -jar aais-api-platform-0.1.0.jar

```

## 打开浏览器

输入地址： http://localhost:8080

#### 1. 自由文本识别:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/ocr.png)

#### 2. 人脸检测:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_detect.png)

#### 3. 人脸特征提取（512维特征）:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_feature.png)
  
#### 4. 人脸 1:1 比对:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_comare.png)
  
#### 5. 接口文档:  
http://127.0.0.1:8089/swagger-ui.html
  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/swagger.png)
  
## 计划开发的功能：
```bash
1. 车辆检测
2. 行人检测
3. 视频处理
4. 内容自动生成
5. 自然语言处理
6. ...
```


### QQ群:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/OCR/OCR_QQ.png)
  