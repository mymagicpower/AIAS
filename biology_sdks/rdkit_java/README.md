## 化学信息学的开源工具包
RDKit是一个用于化学信息学的开源工具包，基于对化合物2D和3D分子操作，利用机器学习方法进行化合物描述符生成，
fingerprint生成，化合物结构相似性计算，2D和3D分子展示等。
将化学与机器学习联系起来的、非常实用的库。可以在很多种化学文件如mol2，mol，Smiles，sdf等之间互相转化，并能将其展示成2D、3D等形式供开发人员使用。
这里给出一个java实现。

![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/biology_sdks/rdkit.jpeg)

### 例子包括
- 读写分子
- 特征提取 & 分子相似性计算
相似度计算给出了三种计算方式的例子。
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/biology_sdks/mol.png)

## 运行例子 - SimpleSmilesExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - smi1: c1ccccc1
[INFO ] - smi2: c1ccccn1
[INFO ] - AllBitSimilarity: 0.98681640625
[INFO ] - CosineSimilarity: 0.4147806778921701
[INFO ] - DiceSimilarity: 0.5454545454545454

```

### 依赖库
[下载本地依赖库](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/biology_sdks/native.zip)     
Java的System.load 和 System.loadLibrary都可以用来加载库文件。
如果使用System.loadLibrary：参数为库文件名
例如你可以这样载入一个windows平台下JNI库文件 System.loadLibrary ("GraphMolWrap")，
这里GraphMolWrap必须在 java.library.path这一jvm变量所指向的路径中。
- 默认情况下，Windows平台下包含下面的路径：     
  1）和jre相关的目录    
  2）程序当前目录   
  3）Windows目录       
  4）系统目录(system32)      
  5）系统环境变量path指定的目录     
 
- 在linux下添加一个java.library.path的方法如下：    
  在/etc/profile 后面加上一行 export LB_LIBRARY_PATH=路径