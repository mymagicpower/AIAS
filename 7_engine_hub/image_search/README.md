## 目录：
http://aias.top/


## 图像搜索平台介绍

图像搜索平台支持两类图像搜索：
- 通用图像搜索：使用ImageNet数据集上预训练的模型：resnet50提取512维特征
- 人像高精度搜索：人脸特征提取(使用人脸特征模型提取512维特征)前先做 - 人脸检测，人脸关键点提取，人脸对齐

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/search_engine.png)

#### 项目地址

|     |   地址  | 
|---  |--- |
|  github   |  https://github.com/mymagicpower/AIAS/blob/main/image_search_cpu   | 
|  码云   |  https://gitee.com/mymagicpower/AIAS/tree/main/image_search_cpu   | 

#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

####  系统功能
- 搜索管理：提供通用图像搜索，人像搜索，图像信息查看
- 存储管理：提供图像压缩包(zip格式)上传，人像特征提取，通用特征提取
- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位
- 字典管理：可维护常用一些固定的数据，如：状态，性别等
- 系统日志：记录用户操作日志与异常日志，方便开发人员定位排错
- SQL监控：采用druid 监控数据库访问性能，默认用户名admin，密码123456
- 定时任务：整合Quartz做定时任务，加入任务日志，任务运行情况一目了然
- 服务监控：监控服务器的负载情况


## 1. 前端部署

#### 下载安装：
[image-search-ui](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/dist.zip)

#### nginx部署运行：
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
        
        location /aias {
                alias  /Users/calvin/Documents/image_root/;  (请更新成你的文件路径，用于存放上传的图片及显示使用)
                index  index.html index.html;
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
- 需要安装redis
- 需要安装MySQL数据库

#### 2.2 下载jar包：
[jar包](https://aias-home.oss-cn-beijing.aliyuncs.com/jars/aias-aiplatform-search-1.0.jar)   
 
#### 2.3 下载并导入SQL文件到MySQL数据库：
使用命令行导入，或者mysql workbench, navicat 图形界面导入。     
[SQL文件](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/data.sql)

#### 2.4 编辑环境配置信息
windows环境里可以使用7-zip直接对jar包中的配置文件进行编辑。

- application-dev.yml    
1). 根据需要编辑数据库名称image-search，用户名，密码 
```bash
      url: jdbc:log4jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:image-search}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true
      username: ${DB_USER:root}
      password: ${DB_PWD:??????}

```
2). 根据需要编辑图片上传根路径imageRootPath(需配置到nginx)     
```bash
# 文件存储路径
file:
  mac:
    ...
    imageRootPath: ~/file/image_root/ #图片文件根目录
  linux:
    ....
    imageRootPath: /home/aias/file/image_root/ #图片文件根目录
  windows:
    ...
    imageRootPath: C:\aias\file\image_root\ ##图片文件根目录
    ...
```


- application.yml     
1). 根据需要编辑redis连接信息
```bash
  redis:
    #数据库索引
    database: ${REDIS_DB:0}
    host: ${REDIS_HOST:127.0.0.1}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PWD:}
    #连接超时时间
    timeout: 5000

```
2). 根据需要编辑图片baseurl 
```bash
image:
  #baseurl是图片的地址前缀，根据需要将127.0.0.1换成nginx所在服务器的ip地址
  baseurl: http://127.0.0.1:8080/aias/
```

#### 2.5 配置hosts文件：
```bash
# 客户端(浏览器)机器的hosts文件添加映射< 127.0.0.1	search.aias.me>, 
# 其中127.0.0.1替换成jar包运行的服务器ip地址

127.0.0.1	search.aias.me
```

#### 2.6 运行程序：
```bash
# 运行程序（模板配置文件，模板图片存放于当前目录）

java -javaagent:aias-aiplatform-search-1.0.jar -jar aias-aiplatform-search-1.0.jar

```

## 3. 后端向量引擎部署（docker）
#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取 向量引擎 镜像（用于计算特征值向量相似度）
```bash
sudo docker pull milvusdb/milvus:0.10.0-cpu-d061620-5f3c00
```

#### 3.3 下载配置文件
[vector_engine.zip](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/vector_engine.zip)  

#### 3.4 启动 Docker 容器
/Users/calvin/vector_engine为主机路径，根据需要修改。conf下为引擎所需的配置文件。
```bash
docker run -d --name milvus_cpu_0.10.0 \
-p 19530:19530 \
-p 19121:19121 \
-p 9091:9091 \
-v /Users/calvin/vector_engine/db:/var/lib/milvus/db \
-v /Users/calvin/vector_engine/conf:/var/lib/milvus/conf \
-v /Users/calvin/vector_engine/logs:/var/lib/milvus/logs \
-v /Users/calvin/vector_engine/wal:/var/lib/milvus/wal \
milvusdb/milvus:0.10.0-cpu-d061620-5f3c00
```

#### 3.5 编辑向量引擎连接配置信息
- application.yml
- 根据需要编辑向量引擎连接ip地址127.0.0.1为容器所在的主机ip
```bash
##################### 向量引擎 ###############################
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 256
  nlist: 16384
  faceDimension: 512 #dimension of each vector
  faceCollectionName: faces #collection name
  commDimension: 512 #dimension of each vector
  commCollectionName: comm #collection name

```

## 4. 打开浏览器
- 输入地址： http://localhost:8080

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/login.png)

- 通用搜索
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/common_search.png)

- 人像搜索
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/face_search.png)

- 图片上传
1). 点击上传按钮上传zip压缩包.  
2). 如果是人像图片：点击提取人脸特征按钮.  
3). 如果不是人像图片：点击提取特征按钮.  
4). 刷新页面：可以看到"状态"列，如：45/100 的特征提取进度.  

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/storage.png)


## 5. 帮助信息
- 接口文档:  
点击菜单：系统工具-接口文档
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/swagger.png)

- 初始化向量引擎(清空数据):  
127.0.0.1替换成jar运行的主机ip。
http://127.0.0.1:9000/api/search/initSearchEngine

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/init.png)

#### QQ群：
111257454
