
### Download the model and place it in the models directory and unzip it
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/api_platform.zip

# AI Empowerment Platform

The AI Empowerment Platform provides interfaces for upper-layer applications in the form of REST APIs.
The current CPU version includes the following functions:

1. Free text recognition (currently requires that the images are upright, that is, without rotation angles. The automatic correction function is being optimized.)
2. Face detection (return the detection box coordinates, detection box coordinate order: up, right, down, left)
3. Face feature extraction (512-dimensional features)
4. Face 1:1 comparison

### 1. Front-end deployment

### 1.1 Run directly:
```bash
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 nginx deployment operation (mac environment is used as an example):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/api-platform/dist/;
index  index.html index.htm;
        }
......

# Reload the configuration:
sudo nginx -s reload

# After deploying the application, restart:
cd /usr/local/Cellar/nginx/1.19.6/bin

# Quick stop
sudo nginx -s stop

# Start
sudo nginx
```

### 2. Backend deployment

### 2.1 jar package

Build jar package

### 2.2 Running the program
```bash
# Running the program

java -jar api-platform-0.1.0.jar
```

## Open the browser

Enter the address: http://localhost:8080

#### 1. Free text recognition:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/ocr.png)

#### 2. Face detection:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_detect.png)

#### 3. Face feature extraction (512-dimensional features):
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_feature.png)
  
#### 4. Face 1:1 comparison:
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/face_comare.png)
  
#### 5. Interface documentation:
http://127.0.0.1:8089/swagger-ui.html
  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/ai_platform/images/swagger.png)
