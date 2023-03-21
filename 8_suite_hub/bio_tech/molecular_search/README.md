
### Molecular Search

This example provides molecular search, supporting the upload of smi files, using RDKit to extract molecular features, and performing subsequent retrieval based on Milvus vector engine.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/arc.png"  width = "600"/>
</div> 

### Engine features

- Underlying feature vector similarity search
- Millisecond-level searching for billions of data on a single server
- Near-real-time search, supporting distributed deployment
- Inserting, deleting, searching, updating data at any time

### Molecular background introduction

SMILES (Simplified Molecular Input Line Entry System) is a specification for describing the structure of a molecule using an ASCII string. As SMILES uses a string of characters to describe a three-dimensional chemical structure, it must convert the chemical structure into a generation tree. This system uses a vertical-first traversal tree algorithm. When converting, hydrogen must be removed and the ring must be opened. When representing, the atom at the end of the broken bond must be marked with a number, and the side chain must be written in parentheses. SMILES strings can be imported into most molecule editing software and converted into 2D graphics or a three-dimensional model of the molecule.

### Molecular feature extraction

RDKit is an open-source toolkit for cheminformatics. It uses machine learning methods to generate compound descriptors, fingerprints, calculate chemical structure similarity, and display 2D and 3D molecules based on 2D and 3D molecule operations. It is a very practical library that links chemistry with machine learning. It can convert between many types of chemical files, such as mol2, mol, Smiles, sdf, etc., and display them in 2D, 3D, and other forms for developers to use.
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

#### Vector engine indexing strategy
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/milvus.png"  width = "600"/>
</div> 


### 1. Front-end deployment

### 1.1 Installation and operation:
```bash
#Install dependency packages
npm install
#Run
npm run dev
```

#### 1.2 Build the dist installation package:
```bash
npm run build:prod
```

#### 1.3 Nginx deployment operation (mac environment):
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
#Edit nginx.conf

server {
listen       8080;
server_name  localhost;

location / {
root   /Users/calvin/Documents/molecular_search/dist/;
index  index.html index.htm;
        }
......

#Reload configuration:
sudo nginx -s reload

#After deploying the application, restart it:
cd /usr/local/Cellar/nginx/1.19.6/bin

#Fast stop
sudo nginx -s stop

#Start
sudo nginx
```

### 2. Backend jar deployment

### 2.1 Environmental requirements:

- System JDK 1.8+ (recommended 1.8, 11)
- application.yml

```bash
#File storage path
file:
  mac:
    path: ~/file/
  linux:
    path: /home/aias/file/
  windows:
    path: D:/aias/file/
  #File size /M
  maxSize: 3000
    ...
```

#### 2.2 Run the program:
```bash
java -jar molecular-search-0.1.0.jar

```

## 3. Backend vector engine deployment (docker)

### 3.1 Environmental requirements:

- Need to install the Docker runtime environment. For Mac environments, you can use Docker Desktop.

### 3.2 Pull Milvus (version 1.1.0) vector engine image (used for calculating feature value vector similarity)
[Installation document](https://milvus.io/docs/v1.1.0/milvus_docker-cpu.md)
##### For the latest version, please refer to the official website
- Milvus vector engine reference link
  [Milvus vector engine official website](https://milvus.io/)      
  [Milvus vector engine Github](https://github.com/milvus-io)

```bash
# Pull the vector engine image
sudo docker pull milvusdb/milvus:1.1.0-cpu-d050721-5e559c
```

#### 3.3 Download configuration file
```bash
mkdir -p /Users/calvin/milvus/conf
cd /Users/calvin/milvus/conf
wget https://raw.githubusercontent.com/milvus-io/milvus/v1.1.0/core/conf/demo/server_config.yaml

#Note:
#The default cache_size: 4GB, insert_buffer_size: 1GB
#It is recommended to reduce them for development machines, such as cache_size: 256MB, insert_buffer_size: 256MB
```

#### 3.4 Start Docker container
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

### 3.5 Edit the vector engine connection configuration information

- application.yml
- Edit the vector engine connection IP address 127.0.0.1 according to your needs to the IP address of the host where the container is located.

```bash
################ Vector Engine ###############
search:
  host: 127.0.0.1
  port: 19530
  indexFileSize: 1024 # maximum size (in MB) of each index file
  nprobe: 16
  nlist: 512
  collectionName: mols #collection name

```

### 4. Open the browser

- Enter the address: [http://localhost:8090](http://localhost:8090/)
- Upload data file
  1). Click the Upload button to upload the file.
[Test data 100](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/test_100.smi)         
[Test data 10,000](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/test_1w.smi)

  2). Click the Feature Extraction button.
      Wait for the file to be parsed, feature extraction, and feature storage in the vector engine. The progress information can be viewed through the console.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/storage.png"  width = "600"/>
</div> 

- Molecular search
  Enter the molecular smiles and click the query to see the returned list sorted by similarity.

```text
P(=O)(OC[C@H]1O[C@@H](n2c3ncnc(N)c3nc2)[C@H](O)[C@@H]1F)(O)O
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/search.png"  width = "600"/>
</div> 

## 5. Help information

- swagger interface document:
http://localhost:8089/swagger-ui.html
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/6_biomedicine/molecular_search/swagger.png"  width = "500"/>
</div> 

- Reset the vector engine (clear data):
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

        //Check whether the collection exists
        HasCollectionResponse hasCollection = hasCollection(client, collectionName);
        if (hasCollection.hasCollection()) {
            dropIndex(client, collectionName);
            dropCollection(client, collectionName);
        }
       ...

```
- Modify the lib loading path according to the environment
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
