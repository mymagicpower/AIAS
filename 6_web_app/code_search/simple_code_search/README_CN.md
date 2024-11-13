## 目录：
http://aias.top/

### 下载模型，放置于models目录
- 链接：https://pan.baidu.com/s/1eKaVbBwGOcx0IFeYTG0Gjg?pwd=5c0x 


### 更新yaml配置文件的模型路径
```bash
# Model URI
model:
  # 模型路径,注意路径最后要有分隔符
  # /Users/calvin/products/4_apps/simple_text_search/text-search/models/m100/
  # D:\\ai_projects\\products\\2_nlp_sdks\\embedding\\code2vec_sdk\\models\\
  # D:\\ai_projects\\products\\2_nlp_sdks\\embedding\\codet5p_110m_sdk\\models\\
  # D:\\ai_projects\\products\\2_nlp_sdks\\embedding\\mpnet_base_v2_sdk\\models\\
  modelPath: D:\\ai_projects\\products\\2_nlp_sdks\\embedding\\codet5p_110m_sdk\\models\\
  # 模型名字
  # all-MiniLM-L12-v2.pt
  # all-MiniLM-L6-v2.pt
  # codet5p-110m.pt
  # all-mpnet-base-v2.pt
  modelName: codet5p-110m.pt
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4
  # 最大代码字符长度
  maxLength: 1024
```

### 代码语义搜索【无向量引擎版】 - simple_code_search
#### 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作


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


#### 2.2 运行程序：
```bash
# 运行程序

java -jar code-search-0.1.0.jar

```

## 3. 打开浏览器
- 输入地址： http://localhost:8090

- 上传CSV数据文件
1). 点击上传按钮上传jsonl文件.  
[测试数据](https://aias-home.oss-cn-beijing.aliyuncs.com/data/testData.jsonl)
2). 点击特征提取按钮. 
等待文件解析，特征提取。通过console可以看到进度信息。
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/storage.png)

- 相似代码搜索
  输入代码片段，点击查询，可以看到返回的清单，根据相似度排序。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/data/images/codesearch.png)

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/swagger.png)

