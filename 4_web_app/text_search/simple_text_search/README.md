## 目录：
http://aias.top/

#### 模型下载：
- 链接:https://pan.baidu.com/s/16933J3dX16xnjbYaay-4og?pwd=cwxk

```bash
# Model URI
model:
  # 模型路径,注意路径最后要有分隔符
  modelPath: /Users/calvin/products/4_apps/simple_text_search/text-search/models/m3e/
  # 模型名字
  modelName: traced_m3e_base_model.pt
  # for chinese - ture, others - false
  chinese: true
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
```

#### 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作


### 句向量模型【支持15种语言】

句向量是指将语句映射至固定维度的实数向量。将不定长的句子用定长的向量表示，为NLP下游任务提供服务。
支持 15 种语言： 
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.

- 句向量    
  ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


句向量应用：

- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 文本聚类，文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 文本分类，表示成句向量，直接用简单分类器即训练文本分类器



### 1. 前端部署

#### 1.1 安装运行：
```bash
# 安装依赖包
npm install
# 运行
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
            root   /Users/calvin/Documents/text_search/dist/;
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

## 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+

- application.yml       
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
  linux:
    path: /home/aias/file/
  windows:
    path: file:/D:/aias/file/
  # 文件大小 /M
  maxSize: 3000
    ...
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar text-search-0.1.0.jar

```


## 3. 打开浏览器
- 输入地址： http://localhost:8090

- 上传CSV数据文件
1). 点击上传按钮上传CSV文件.  
[测试数据](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/example.csv)
2). 点击特征提取按钮. 
等待CSV文件解析，特征提取，特征存入向量引擎。通过console可以看到进度信息。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/storage.png)

- 文本搜索
  输入文字，点击查询，可以看到返回的清单，根据相似度排序。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png)

## 4s. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/swagger.png)



### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   


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
