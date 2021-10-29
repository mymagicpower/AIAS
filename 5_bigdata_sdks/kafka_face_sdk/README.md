## kafka-人脸检测SDK
人脸识别技术目前已经广泛应用于包括人脸门禁系统、刷脸支付等各行各业。随着人脸识别技术的提升，
应用越来越广泛。目前中国的人脸识 别技术已经在世界水平上处于领先地位，在安防行业，
国内主流安防厂家也都推出了各自的人脸识别产品和解决方案，泛安防行业是人脸识别技术主要应用领域。
这个例子给出了，人脸识别技术是如何与大数据技术栈协同工作的。
后续结合人脸特征提取，特征向量保存到向量搜索引擎，形成人像底库，然后就可以实现人像大数据搜索。
 
- 人脸检测    
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/face_detection.jpeg)

#### 1. 启动 zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. 启动 kafka:
启动前先需环境配置kafka的server.properties(添加message.max.bytes=10485760), 支持大消息。
因为图片转成base64字符串后，会超过kafka的默认消息大小设置。如果不增加配置，kafka不会接收消息。
`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. 创建 topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic face-data`

#### 4. 查看创建的topic

`kafka-topics --list --zookeeper localhost:2181`

#### 5. 运行例子 - FaceDetectionExample

#### 6. 运行例子 - MyKafkaProducer
读取图片，转成base64格式发送给kafka。

#### 7. 查看 FaceDetectionExample的输出Console
consumer接受到图片的base64数据, 转换成图片并解析：
```bash
[
	class: "Face", probability: 0.99958, bounds: [x=0.485, y=0.198, width=0.122, height=0.230]
	class: "Face", probability: 0.99952, bounds: [x=0.828, y=0.270, width=0.116, height=0.225]
	class: "Face", probability: 0.99937, bounds: [x=0.180, y=0.234, width=0.119, height=0.231]
]
```

#### Mac环境安装kafka 
```bash
brew install kafka
```
#### Mac环境配置kafka，支持大消息 
编辑/usr/local/etc/kafka/server.properties，增加下面的配置项：
```bash
message.max.bytes=10485760
```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
