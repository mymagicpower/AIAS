### 目录：
http://aias.top/

### 分子搜索
本例子提供了分子搜索，支持上传smi文件文件，使用RDKit提取分子特征，并基于milvus向量引擎进行后续检索。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/arc.png"  width = "600"/>
</div> 

#### 引擎特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作

#### 分子背景介绍
SMILES（Simplified molecular input line entry system），简化分子线性输入规范，是一种用ASCII字符串明确描述分子结构的规范。
由于SMILES用一串字符来描述一个三维化学结构，它必然要将化学结构转化成一个生成树，此系统采用纵向优先遍历树算法。转化时，先要去掉氢，还要把环打开。表示时，被拆掉的键端的原子要用数字标记，支链写在小括号里。
SMILES字符串可以被大多数分子编辑软件导入并转换成二维图形或分子的三维模型。

#### 分子特征提取
RDKit是一个用于化学信息学的开源工具包，基于对化合物2D和3D分子操作，利用机器学习方法进行化合物描述符生成，fingerprint生成，化合物结构相似性计算，2D和3D分子展示等。将化学与机器学习联系起来的、非常实用的库。可以在很多种化学文件如mol2，mol，Smiles，sdf等之间互相转化，并能将其展示成2D、3D等形式供开发人员使用。

<table>
  <tr>
    <td>
      <div align="center">
      <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/rdkit.jpeg"  width = "300"  />
      </div>
    </td>
    <td>
      <div align="center">
      <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/mol.png"  width = "300"  />
      </div>
    </td>
  </tr>
</table>

#### 向量引擎索引策略
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/milvus.png"  width = "600"/>
</div> 


#### 1. 前端部署

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
            root   /Users/calvin/Documents/molecular_search/dist/;
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

#### 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+ (建议1.8, 11)

- application.yml       
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
  linux:
    path: /home/aias/file/
  windows:
    path: D:/aias/file/
  # 文件大小 /M
  maxSize: 3000
    ...
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar molecular-search-0.1.0.jar

```

## 3. 后端向量引擎部署（docker）
#### 3.1 环境要求：
- 需要安装docker运行环境，Mac环境可以使用Docker Desktop

#### 3.2 拉取Milvus（1.1.0版）向量引擎镜像（用于计算特征值向量相似度）
[安装文档](https://milvus.io/docs/v1.1.0/milvus_docker-cpu.md)
##### 最新版本请参考官网
- Milvus向量引擎参考链接     
[Milvus向量引擎官网](https://milvus.io/cn/docs/overview.md)      
[Milvus向量引擎Github](https://github.com/milvus-io)

```bash
# 拉取 向量引擎 镜像
sudo docker pull milvusdb/milvus:1.1.0-cpu-d050721-5e559c
```

#### 3.3 下载配置文件
```bash
mkdir -p /Users/calvin/milvus/conf
cd /Users/calvin/milvus/conf
wget https://raw.githubusercontent.com/milvus-io/milvus/v1.1.0/core/conf/demo/server_config.yaml

# 提示：
# 默认cache_size: 4GB，insert_buffer_size: 1GB
# 开发机建议改小，如：cache_size: 256MB，insert_buffer_size: 256MB
```

#### 3.4 启动 Docker 容器
```bash
sudo docker run -d --name milvus_cpu_1.1.0 \
-p 19530:19530 \
-p 19121:19121 \
-v /Users/calvin/milvus/db:/var/lib/milvus/db \
-v /Users/calvin/milvus/conf:/var/lib/milvus/conf \
-v /Users/calvin/milvus/logs:/var/lib/milvus/logs \
-v /Users/calvin/milvus/wal:/var/lib/milvus/wal \
milvusdb/milvus:1.1.0-cpu-d050721-5e559c
```

#### 3.5 编辑向量引擎连接配置信息
- application.yml
- 根据需要编辑向量引擎连接ip地址127.0.0.1为容器所在的主机ip
```bash
################ 向量引擎 ###############
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 16
  nlist: 512
  collectionName: mols #collection name

```

#### 4. 打开浏览器
- 输入地址： http://localhost:8090

- 上传数据文件
1). 点击上传按钮上传文件.  
[测试数据100条](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/test_100.smi)         
[测试数据1万条](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/test_1w.smi)
2). 点击特征提取按钮. 
等待文件解析，特征提取，特征存入向量引擎。通过console可以看到进度信息。
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/storage.png"  width = "600"/>
</div> 

- 分子搜索
输入分子smiles，点击查询，可以看到返回的清单，根据相似度排序。
```text
P(=O)(OC[C@H]1O[C@@H](n2c3ncnc(N)c3nc2)[C@H](O)[C@@H]1F)(O)O
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/search.png"  width = "600"/>
</div> 

## 5. 帮助信息
- swagger接口文档:  
http://localhost:8089/swagger-ui.html
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/swagger.png"  width = "500"/>
</div> 

- 重置向量引擎(清空数据): 
me.aias.tools.MilvusInit.java 
```bash
        String host = "127.0.0.1";
        int port = 19530;
        final String collectionName = "mols"; // collection name

        MilvusClient client = new MilvusGrpcClient();
        // Connect to Milvus server
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
        try {
            Response connectResponse = client.connect(connectParam);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
        }

        // 检查 collection 是否存在
        HasCollectionResponse hasCollection = hasCollection(client, collectionName);
        if (hasCollection.hasCollection()) {
            dropIndex(client, collectionName);
            dropCollection(client, collectionName);
        }
       ...

```
- 根据环境修改lib加载路径
  >mac:              
    libPath: lib/native/macosx.x86_64/libGraphMolWrap.jnilib                    
  linux:              
    libPath: lib/native/linux.x86_64/libGraphMolWrap.so                    
  windows:              
    libPath: lib/native/win32.x86_64/GraphMolWrap.dll              

```bash
    me.aias.common.rdkit.RDKitInstance.java  

    public final class RDKitInstance {
        static {
            try {
                Path path = Paths.get("lib/native/macosx.x86_64/libGraphMolWrap.jnilib");
                System.load(path.toAbsolutePath().toString());
            } catch (UnsatisfiedLinkError e) {
                System.err.println("Native code library failed to load.\n" + e);
                System.exit(1);
            }
    //        System.loadLibrary("GraphMolWrap");
        }
    }

```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   