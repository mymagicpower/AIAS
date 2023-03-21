
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/ultranet.zip

## Kafka-Face Detection SDK

Face recognition technology is currently widely used in various industries including face access control systems, face payment, etc. With the improvement of face recognition technology, its applications are becoming more and more extensive. At present, China's face recognition technology is leading the world. In the security industry, mainstream security manufacturers in China have also launched their own face recognition products and solutions, and the pan-security industry is the main application field of face recognition technology. This example shows how face recognition technology collaborates with big data technology stack. Combining face feature extraction, feature vectors can be saved to vector search engines to form a portrait database, and then face big data search can be implemented.

- Face detection
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/face_detection.jpeg)

#### 1. Start the zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

#### 2. Start kafka:
Before starting, you need to configure the kafka server.properties file (add message.max.bytes=10485760) to support large messages. Because after the picture is converted to a base64 string, it will exceed the default message size setting of Kafka. If the configuration is not increased, Kafka will not receive the message.
`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. Create a topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic face-data`

#### 4. View the created topic

`kafka-topics --list --zookeeper localhost:2181`

#### 5. Run the example- FaceDetectionExample

#### 6. Run the example- MyKafkaProducer
Read the picture, convert it to base64 format, and send it to Kafka.

#### 7. View the output console of FaceDetectionExample
The consumer receives the base64 data of the picture, converts it to a picture, and parses it:
```bash
[
	class: "Face", probability: 0.99958, bounds: [x=0.485, y=0.198, width=0.122, height=0.230]
	class: "Face", probability: 0.99952, bounds: [x=0.828, y=0.270, width=0.116, height=0.225]
	class: "Face", probability: 0.99937, bounds: [x=0.180, y=0.234, width=0.119, height=0.231]
]
```

#### Installing Kafka in Mac environment
```bash
brew install kafka
```
#### Configure Kafka in Mac environment to support large messages

Edit /usr/local/etc/kafka/server.properties and add the following configuration item:
```bash
message.max.bytes=10485760
```
