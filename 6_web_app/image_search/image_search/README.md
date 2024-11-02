<div align="center">
  <a href="http://aias.top/">点击返回网站首页</a>
</div>  

### 下载模型
- 链接: https://pan.baidu.com/s/1QKUSP7IaIY3U3pK6cqvdCg?pwd=g75s

### 以图搜图产品
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/arc.png"  width = "600"/>
</div> 

#### 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

#### 系统功能
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


#### 1. 前端搜索页面功能介绍
- 首页
1). 支持图片拖拽，粘贴搜索.  
2). 适配PC，android，ios浏览器.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/simple_1.png"  width = "600"/>
</div> 

- 图片列表页
1). 支持图片拖拽，粘贴搜索.  
2). 图片排版自适应.
3). 图片列表下拉自动加载.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/simple_2.png"  width = "600"/>
</div> 

#### 2. 后台管理功能介绍
- 登录
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/login.png"  width = "600"/>
</div> 

- 用户管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/user.png"  width = "600"/>
</div> 

- 角色管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/role.png"  width = "600"/>
</div> 

- 运维管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/ops.png"  width = "600"/>
</div> 

- 图片上传
1). 支持zip压缩包上传.  
2). 支持服务器端文件夹上传（大量图片上传使用，比如几十万张图片入库）.  
3). 支持客户端文件夹上传.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/storage.png"  width = "600"/>
</div> 

- 图像搜索
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/search.png"  width = "600"/>
</div> 


### 1. 前端部署

#### 1.1 直接运行：
```bash
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境部署管理前端为例，搜索前端页面参考windows安装配置文档)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/image-search-ui/dist/;
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
- 系统JDK 1.8+
- 需要安装并redis
- 需要安装MySQL数据库


### 3. 后端向量引擎部署

#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus向量引擎镜像（用于计算特征值向量相似度）
下载 milvus-standalone-docker-compose.yml 配置文件并保存为 docker-compose.yml     

[单机版安装文档](https://milvus.io/docs/install_standalone-docker.md)       
[引擎配置文档](https://milvus.io/docs/configure-docker.md)   
[milvus-sdk-java](https://github.com/milvus-io/milvus-sdk-java)  


```bash
# 例子：v2.2.8，请根据官方文档，选择合适的版本
wget https://github.com/milvus-io/milvus/releases/download/v2.2.8/milvus-standalone-docker-compose.yml -O docker-compose.yml
```

#### 3.3 启动 Docker 容器
```bash
sudo docker-compose up -d
```

#### 3.4 编辑向量引擎连接配置信息
- aiplatform-system\src\main\resources\application-dev.yml
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
  collectionName: arts #collection name

```


#### 3.5 编辑模型路径信息
- aiplatform-system\src\main\resources\application-dev.yml
```bash
# Model URI
model:
  # P:/aias/models/CLIP-ViT-B-32-IMAGE.pt
  # /Users/calvin/products/4_apps/image_search/image-search/models/CLIP-ViT-B-32-IMAGE.pt
  imageModel: P:/aias/models/CLIP-ViT-B-32-IMAGE.pt
  poolSize: 64

```


#### 3.6 启动redis(mac环境为例）
```bash
nohup /usr/local/bin/redis-server &
```

#### 3.7 启动MySQL，并导入sql DLL
```bash
# Start MySQL
sudo /usr/local/mysql/support-files/mysql.server start

# sql 文件位置：
# image-search/sql/data.sql
# 导入 data.sql， 如：使用 MySQL WorkBench 之类工具导入。
```

### 4. 运行程序：
使用IDE运行 AppRun：
位置：image-search/aiplatform-system/src/main/java/me/calvin/AppRun
或者运行编译后的jar：
```bash
# 运行程序
nohup java -Dfile.encoding=utf-8 -jar xxxxx-1.0.jar > log.txt 2>&1 &
```

### 5. 打开浏览器
- 输入地址： http://localhost:8089

- 图片上传        
1). 点击上传按钮上传zip压缩包.  
2). 点击提取特征按钮.  


### 6. 重置Milvus向量引擎(清空数据)
- :  
```bash
# image-search/aiplatform-system/src/main/java/me/calvin/modules/search/tools
MilvusInit.java
```


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