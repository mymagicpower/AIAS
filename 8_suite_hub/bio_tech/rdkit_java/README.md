
## Open source toolkit for chemical informatics

RDKit is an open source toolkit for chemical informatics, based on 2D and 3D molecule operations for compounds, using machine learning methods for compound descriptor generation, fingerprint generation, compound structure similarity calculation, 2D and 3D molecule visualization, and more.
It is a very practical library that connects chemistry with machine learning. It can convert between many types of chemical files such as mol2, mol, Smiles, sdf, etc., and display them in 2D, 3D and other forms for developers to use.
Here is a Java implementation example.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/rdkit.jpeg)


- Read and write molecules
- Image generation & saving
- Feature extraction & molecule similarity calculation
The similarity calculation provides examples of three calculation methods.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/mol.png)

## Running the example - SimpleSmilesExample
After running successfully, the command line should see the following information:
```text
[INFO ] - smi1: c1ccccc1
[INFO ] - smi2: c1ccccn1
[INFO ] - AllBitSimilarity: 0.98681640625
[INFO ] - CosineSimilarity: 0.4147806778921701
[INFO ] - DiceSimilarity: 0.5454545454545454

```
Save the molecular image:
![svg](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/svg.png)


### Dependency library download & configuration environment variable
[Click to download the local dependency library](https://github.com/mymagicpower/AIAS/releases/download/apps/native.zip)     

#### What is java.library.path?
Simply put, it is a parameter that can be specified when JVM starts. Similar to classpath, it specifies the path of class files or jar files. java.library.path specifies the path of other program files linked by JNI, such as dll or so files.

#### How to set java.library.path

##### Command line setting
java -Djava.library.path=<path_to_dll_or_so> <main_class>
##### Code setting
System.setProperty("java.library.path", "/path/to/library");
##### Environment variable setting
java.library.path

Java's System.load and System.loadLibrary can be used to load library files.
If you use System.loadLibrary: the parameter is the name of the library file. For example, you can load a JNI library file under the Windows platform like this: System.loadLibrary ("GraphMolWrap"), where GraphMolWrap must be in the path specified by the jvm variable java.library.path. For Java 8 versions:
- Windows: PATH
- Linux: LD_LIBRARY_PATH
To add a java.library.path in Linux, follow these steps:
Add a line export LB_LIBRARY_PATH=<path_to_so> after /etc/profile
- Mac: JAVA_LIBRARY_PATH
To add a java.library.path in Mac, follow these steps:
Add a line export JAVA_LIBRARY_PATH=<path_to_so> after /etc/profile
    