### 目录：
http://aias.top/


### 人脸大数据搜索介绍
- 人像高精度搜索：人脸特征提取(使用人脸特征模型提取512维特征)前先做 - 人脸检测，人脸关键点提取，人脸对齐

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/search_engine.png)


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

#### 2.2 导入SQL文件到MySQL数据库：
使用命令行导入，或者mysql workbench, navicat 图形界面导入。     
```bash
face-search/sql/data.sql
```

#### 2.3 编辑环境配置信息
- application-dev.yml    
1). 根据需要编辑数据库名称face-search，用户名，密码 
```bash
      url: jdbc:log4jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:face-search}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true
      username: ${DB_USER:root}
      password: ${DB_PWD:??????}

```
2). 根据需要编辑图片上传根路径imageRootPath(需配置到nginx,用于web访问)     
```bash
# 文件存储路径
file:
  mac:
    ...
    imageRootPath: /Users/calvin/aias/image_root/ #图片文件根目录
  linux:
    ....
    imageRootPath: /home/aias/image_root/ #图片文件根目录
  windows:
    ...
    imageRootPath: file:/D:/aias/image_root/ ##图片文件根目录
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
  #baseurl是图片的地址前缀，根据需要将127.0.0.1换成nginx所在服务器的ip地址及端口
  baseurl: http://127.0.0.1:8080/aias/
```

#### 2.4 运行程序：
```bash
# 运行程序

java -jar aiplatform-search-1.0.jar

```

## 3. 后端向量引擎部署（Milvus 2.0）
#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus向量引擎镜像（用于计算特征值向量相似度）
下载 milvus-standalone-docker-compose.yml 配置文件并保存为 docker-compose.yml        
[单机版安装文档](https://milvus.io/docs/v2.0.0/install_standalone-docker.md)        

##### 最新版本请参考官网
- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

```bash
wget https://github.com/milvus-io/milvus/releases/download/v2.0.0/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 启动 Docker 容器
```bash
sudo docker-compose up -d
```

#### 3.4 编辑向量引擎连接配置信息
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
  dimension: 512 #dimension of each vector
  collectionName: faces #collection name

```

## 4. 打开浏览器
- 输入地址： http://localhost:8080

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/image_search/images/login.png)

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

- 重置Milvus向量引擎(清空数据):  
```bash
me.calvin.modules.search.tools.MilvusInit.java
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   