
### Download the model and put it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/ultranet.zip

## How does AI technology work with big data technology stack?

AI model training relies heavily on annotated data. To annotate a large amount of data, a big data platform is necessary to provide technical support. The trained model can also be used for the big data technology stack in reverse.

### Scenario 1: ToB

In the big data platform within the enterprise, two typical links may use AI technology:

- Data acquisition link - unstructured data parsing, such as images, text, audio, etc.
- Data mining and analysis services - image search, recommendation based on deep learning, NLP Q&A, intelligent customer service, etc.
- AI & Big Data

### Scenario 2: General Security

In recent years, AI has been widely used in the field of general security. Face recognition technology has been widely used in various industries including face access control systems, face payment, etc. With the improvement of face recognition technology, its application is becoming more and more extensive. Currently, China's face recognition technology is at the forefront of the world. In the security industry, mainstream security manufacturers have also launched their own face recognition products and solutions. The general security industry is the main application field of face recognition technology. This example shows how face recognition technology works with the big data technology stack. With subsequent face feature extraction, the feature vectors are saved to the vector search engine to form a portrait database, and then a big data search of the portrait can be realized. A typical architecture in the security field is shown in the figure below. After extracting the image containing the portrait on the edge side, it is sent to the cloud for further processing by a higher-precision large model in the cloud.
 
- Face Detection
![face](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/bigdata_sdks/face_detection.jpeg)

### Image Recognition - Kafka, Flink, Face Recognition

The following example shows the process of combining image recognition with Kafka and Flink:

### 1. Start Zookeeper:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

### 2. Start Kafka:

Before starting, you need to configure the environment for Kafka's server.properties (add message.max.bytes=10485760) to support large messages. Because after the image is converted into a base64 string, it will exceed Kafka's default message size setting. If the configuration is not increased, Kafka will not receive messages.
`kafka-server-start  /usr/local/etc/kafka/server.properties`

#### 3. Create a topic:

`kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic face-data`

#### 4. View the created topic

`kafka-topics --list --zookeeper localhost:2181`

#### 5. Run example - FaceDetectionExample
Flink starts and listens to the "face-data" topic of Kafka.

```bash
    ...
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "test-consumer-group");

    // get input data by connecting to the kafka
    FlinkKafkaConsumer<String> consumer =
            new FlinkKafkaConsumer<>("face-data", new SimpleStringSchema(), props);

    // Run inference with Flink streaming
    DataStream<String> detection = env
            .addSource(consumer)
            .flatMap(new SEFlatMap());
   ...         
```

### 6. Run example - MyKafkaProducer

Read the image, convert it to base64 format, and send it to the "face-data" topic of Kafka.

### 7. View the output Console of FaceDetectionExample

The consumer receives the base64 data of the image, converts it into an image, and parses it:
```bash
[
	class: "Face", probability: 0.99958, bounds: [x=0.485, y=0.198, width=0.122, height=0.230]
	class: "Face", probability: 0.99952, bounds: [x=0.828, y=0.270, width=0.116, height=0.225]
	class: "Face", probability: 0.99937, bounds: [x=0.180, y=0.234, width=0.119, height=0.231]
]
```

#### Installing Kafka on Mac
```bash
brew install kafka
```
#### Configuring Kafka on Mac to support large messages
Edit /usr/local/etc/kafka/server.properties and add the following configuration item:
```bash
message.max.bytes=10485760
```

