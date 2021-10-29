## 化学信息学的开源工具包
RDKit是一个用于化学信息学的开源工具包，基于对化合物2D和3D分子操作，利用机器学习方法进行化合物描述符生成，
fingerprint生成，化合物结构相似性计算，2D和3D分子展示等。
将化学与机器学习联系起来的、非常实用的库。可以在很多种化学文件如mol2，mol，Smiles，sdf等之间互相转化，并能将其展示成2D、3D等形式供开发人员使用。
这里给出一个java实现。

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/rdkit.jpeg)

### 例子包括
- 读写分子
- 图片生成 & 保存
- 特征提取 & 分子相似性计算
相似度计算给出了三种计算方式的例子。
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/mol.png)

## 运行例子 - SimpleSmilesExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - smi1: c1ccccc1
[INFO ] - smi2: c1ccccn1
[INFO ] - AllBitSimilarity: 0.98681640625
[INFO ] - CosineSimilarity: 0.4147806778921701
[INFO ] - DiceSimilarity: 0.5454545454545454

```
保存分子图片：
![svg](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/svg.png)


### 依赖库下载&配置环境变量
[点击下载本地依赖库](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/native.zip)     

#### 什么是java.library.path
通俗的说它是JVM启动可以指定的一个参数。类似classpath，指定的是class文件或者jar文件的路径。java.library.path指定的是JNI链接的其他程序文件的路径，比如dll或者so文件。

#### 如何设置java.library.path

##### 命令行设置
java -Djava.library.path=<path_to_dll_or_so> <main_class>
##### 代码设置
System.setProperty(“java.library.path”, “/path/to/library”);
##### 环境变量设置
java.library.path

Java的System.load 和 System.loadLibrary都可以用来加载库文件。
如果使用System.loadLibrary：参数为库文件名，例如你可以这样载入一个windows平台下JNI库文件 System.loadLibrary ("GraphMolWrap")， 这里GraphMolWrap必须在 java.library.path这一jvm变量所指向的路径中。针对Java 8的版本：
- Windows：PATH
- Linux：LD_LIBRARY_PATH
    在linux下添加一个java.library.path的方法如下：
    在/etc/profile 后面加上一行 export LB_LIBRARY_PATH=<path_to_so>
- Mac：JAVA_LIBRARY_PATH
    在Mac下添加一个java.library.path的方法如下：
    在/etc/profile 后面加上一行 export JAVA_LIBRARY_PATH=<path_to_so>
    
### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
  