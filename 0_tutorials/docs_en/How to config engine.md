### Engine configuration
Includes CPU computing configurations for PaddlePaddle, Pytorch, MxNet, and Tensorflow (automatic configuration and manual configuration).
During the first run, automatic configuration will download the native engine from Amazon servers, which may be slow. Manual configuration, on the other hand, will download from a maven server (which can point to a domestic server), which is much faster. Additionally, local libraries will be bundled together, so they will not be loaded during runtime after deployment.

#### Architecture:
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/images/arc.png"  width = "600"/>
</div>   

#### Supported engines and operating systems:
- MXNet - full support   
- PyTorch - full support   
- TensorFlow - supports inference and NDArray operations   
- ONNX Runtime - supports basic inference   
- PaddlePaddle - supports basic inference   
- TFLite - supports basic inference   
- TensorRT - supports basic inference   
- DLR - supports basic inference   

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/images/table.png"  width = "600"/>
</div>   

- Additional information: The following versions can be directly supported
CentOS Linux release 7.9.2009 (Core) and above
ubuntu 18.04 and above


#### Detailed configuration information (including CPU, GPU) reference links:
- [PaddlePaddle](http://docs.djl.ai/engines/paddlepaddle/paddlepaddle-engine/index.html)
- [Pytorch](http://docs.djl.ai/engines/pytorch/pytorch-engine/index.html)
- [MxNet](https://docs.djl.ai/engines/mxnet/mxnet-engine/index.html)
- [Tensorflow](https://docs.djl.ai/engines/tensorflow/tensorflow-engine/index.html) 
- [ONNX](https://docs.djl.ai/engines/onnxruntime/onnxruntime-engine/index.html)
- [TensorFlow Lite](https://docs.djl.ai/engines/tflite/tflite-engine/index.html)
- [DLR](https://docs.djl.ai/engines/dlr/index.html)
- [TensorRT](https://docs.djl.ai/engines/tensorrt/index.html)
  TensorRT driver version can be found in the TRT_VERSION of the following Dockerfile:
  https://github.com/deepjavalibrary/djl/blob/master/docker/tensorrt/Dockerfile  



#### Below are the configuration reference examples:
#### version 0.20.0 (versions may not be updated in time, please refer to the above documentation)
#### 1. PaddlePaddle engine
1). Automatic configuration
```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-engine</artifactId>
    <version>0.20.0</version>
    <scope>runtime</scope>
</dependency>
```
2). macOS - CPU
```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cpu</artifactId>
    <classifier>osx-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>
```

3). Linux - CPU
```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cpu</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>
```

4). Linux - GPU
Need to set the environment variable LD_LIBRARY_PATH, such as setting it in /etc/profile.
```text
LD_LIBRARY_PATH=$HOME/.djl.ai/paddle/x.x.x-<cuda-flavor>-linux-x86_64
```

```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cu102</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cu112</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>

```
- CUDA supported versions
```text
- <artifactId>paddlepaddle-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search for ai.djl.paddlepaddle: <https://mvnrepository.com/>
# or <https://repo1.maven.org/maven2/ai/djl/paddlepaddle/>
CUDA 11.2: paddlepaddle-native-cu112
CUDA 11.0: paddlepaddle-native-cu110
CUDA 10.2: pytorch-native-cu102
CUDA 10.1: pytorch-native-cu101
```

5). Windows - CPU
```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cpu</artifactId>
    <classifier>win-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>
```

6). Windows GPU
```text
<dependency>
    <groupId>ai.djl.paddlepaddle</groupId>
    <artifactId>paddlepaddle-native-cu112</artifactId>
    <classifier>win-x86_64</classifier>
    <version>2.2.2</version>
    <scope>runtime</scope>
</dependency>
```

- CUDA supported versions
```text
- <artifactId>paddlepaddle-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search for ai.djl.paddlepaddle: <https://mvnrepository.com/>
# or <https://repo1.maven.org/maven2/ai/djl/paddlepaddle/>
CUDA 11.2: paddlepaddle-native-cu112
CUDA 11.0: paddlepaddle-native-cu110
CUDA 10.2: pytorch-native-cu102
CUDA 10.1: pytorch-native-cu101
```

#### 2. Pytorch engine
1). Automatic configuration
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-engine</artifactId>
    <version>0.20.0</version>
    <scope>runtime</scope>
</dependency>

```
#####If you need to specify the PyTorch version explicitly, you can use the following configuration method:
- http://docs.djl.ai/engines/pytorch/pytorch-engine/index.html

<div align="center">
  <table>
    <tr>
      <th>
        <div align="center">
            PyTorch engine version  
        </div>
      </th>     
      <th>
        <div align="center">
            PyTorch native library version
        </div>
      </th>
    </tr>  
    <tr>
      <td>
        <div align="center">   
            pytorch-engine:0.20.0   
        </div>
      </td>     
      <td>
        <div align="center">
            1.11.0, 1.12.1, 1.13.0
        </div>
      </td>
    </tr>  
    <tr>
      <td>
        <div align="center">   
            pytorch-engine:0.19.0   
        </div>
      </td>     
      <td>
        <div align="center">
            1.10.0, 1.11.0, 1.12.1
        </div>
      </td>
    </tr>  
    <tr>
      <td>
        <div align="center">   
            pytorch-engine:0.18.0
        </div>
      </td>     
      <td>
        <div align="center">
            1.9.1, 1.10.0, 1.11.0
        </div>
      </td>
    </tr>  
    <tr>
      <td>
        <div align="center">   
            pytorch-engine:0.17.0      
        </div>
      </td>     
      <td>
        <div align="center">
            1.9.1, 1.10.0, 1.11.0
        </div>
      </td>
    </tr>                                         
  </table>
</div>
......


2). macOS - CPU
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu</artifactId>
    <classifier>osx-x86_64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

```

3). macOS - M1
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu</artifactId>
    <classifier>osx-aarch64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

```

4). Linux - CPU
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu</artifactId>
    <classifier>linux-x86_64</classifier>
    <scope>runtime</scope>
    <version>1.13.0</version>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

```

5). For aarch64 build
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu-precxx11</artifactId>
    <classifier>linux-aarch64</classifier>
    <scope>runtime</scope>
    <version>1.13.0</version>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>
```

6). For Pre-CXX11 build
```text
# We also provide packages for the system like CentOS 7/Ubuntu 14.04 with GLIBC >= 2.17. 
# All the package were built with GCC 7, we provided a newer libstdc++.so.6.24 in the package that 
# contains CXXABI_1.3.9 to use the package successfully.
ai.djl.pytorch:pytorch-jni:1.11.0-0.17.0
ai.djl.pytorch:pytorch-native-cu113-precxx11:1.11.0:linux-x86_64 - CUDA 11.3
ai.djl.pytorch:pytorch-native-cpu-precxx11:1.11.0:linux-x86_64 - CPU

<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cu117-precxx11</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

#---------------------------------------------------------------#

<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu-precxx11</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

```

7). Linux - GPU
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cu117</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>

```
- CUDA Supported Versions
```text
- <artifactId>pytorch-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.pytorch query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/pytorch/>
CUDA 11.7: pytorch-native-cu117
CUDA 11.6: pytorch-native-cu116
CUDA 11.3: pytorch-native-cu113
CUDA 11.1: pytorch-native-cu111
CUDA 10.2: pytorch-native-cu102
CUDA 10.1: pytorch-native-cu101
CUDA 10.0: pytorch-native-cu110
CUDA 9.2: pytorch-native-cu92
```

8). Windows - CPU
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cpu</artifactId>
    <classifier>win-x86_64</classifier>
    <scope>runtime</scope>
    <version>1.13.0</version>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>
```

9). Windows - GPU
```text
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-native-cu117</artifactId>
    <classifier>win-x86_64</classifier>
    <version>1.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ai.djl.pytorch</groupId>
    <artifactId>pytorch-jni</artifactId>
    <version>1.13.0-0.20.0</version>
    <scope>runtime</scope>
</dependency>
```

- CUDA Supported Versions
```text
- <artifactId>pytorch-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.pytorch query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/pytorch/>
CUDA 11.7: pytorch-native-cu117
CUDA 11.6: pytorch-native-cu116
CUDA 11.3: pytorch-native-cu113
CUDA 11.1: pytorch-native-cu111
CUDA 10.2: pytorch-native-cu102
CUDA 10.1: pytorch-native-cu101
CUDA 10.0: pytorch-native-cu110
CUDA 9.2: pytorch-native-cu92
```

#### 3. MxNet engine
1). Auto configuration
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-engine</artifactId>
    <version>0.20.0</version>
    <scope>runtime</scope>
</dependency>
```
2). macOS - CPU
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-native-mkl</artifactId>
    <classifier>osx-x86_64</classifier>
    <version>1.9.1</version>
    <scope>runtime</scope>
</dependency>
```

3). Linux - CPU
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-native-mkl</artifactId>
    <classifier>linux-x86_64</classifier>
    <scope>runtime</scope>
    <version>1.9.1</version>
</dependency>
```

4). Linux - GPU
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-native-cu112mkl</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>1.9.1</version>
    <scope>runtime</scope>
</dependency>
```
- CUDA Supported Versions

```text
  <artifactId>mxnet-native-cuXXXmkl</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.mxnet query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/mxnet/>
CUDA 11.2: mxnet-native-cu112mkl
CUDA 11.0: mxnet-native-cu101mkl
CUDA 10.2: mxnet-native-cu101mkl
CUDA 10.1: mxnet-native-cu102mkl
CUDA 9.2: mxnet-native-cu102mkl
```

5). Windows - CPU
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-native-mkl</artifactId>
    <classifier>win-x86_64</classifier>
    <scope>runtime</scope>
    <version>1.9.1</version>
</dependency>
```

6). Windows - GPU
```text
<dependency>
    <groupId>ai.djl.mxnet</groupId>
    <artifactId>mxnet-native-cu112mkl</artifactId>
    <classifier>win-x86_64</classifier>
    <version>1.9.1</version>
    <scope>runtime</scope>
</dependency>
```

- CUDA Supported Versions
```text
  <artifactId>mxnet-native-cuXXXmkl</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.mxnet query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/mxnet/>
CUDA 11.2: mxnet-native-cu112mkl
CUDA 11.0: mxnet-native-cu101mkl
CUDA 10.2: mxnet-native-cu101mkl
CUDA 10.1: mxnet-native-cu102mkl
CUDA 9.2: mxnet-native-cu102mkl
```

#### 4. Tensorflow engine
1). Auto configuration
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-engine</artifactId>
    <version>0.20.0</version>
    <scope>runtime</scope>
</dependency>
```
2). macOS - CPU
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-native-cpu</artifactId>
    <classifier>osx-x86_64</classifier>
    <version>2.7.0</version>
    <scope>runtime</scope>
</dependency>
```

3). Linux - CPU
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-native-cpu</artifactId>
    <classifier>linux-x86_64</classifier>
    <scope>runtime</scope>
    <version>2.7.0</version>
</dependency>
```

4). Linux - GPU
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-native-cu113</artifactId>
    <classifier>linux-x86_64</classifier>
    <version>2.7.0</version>
    <scope>runtime</scope>
</dependency>
```
- CUDA Supported Versions
```text
  <artifactId>tensorflow-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.tensorflow query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/tensorflow/>
CUDA 11.3:  tensorflow-native-cu113
CUDA 11.0:  tensorflow-native-cu110
CUDA 10.1:  tensorflow-native-cu101
```

5). Windows - CPU
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-native-cpu</artifactId>
    <classifier>win-x86_64</classifier>
    <scope>runtime</scope>
    <version>2.7.0</version>
</dependency>
```

6). Windows - GPU
```text
<dependency>
    <groupId>ai.djl.tensorflow</groupId>
    <artifactId>tensorflow-native-cu113</artifactId>
    <classifier>win-x86_64</classifier>
    <version>2.7.0</version>
    <scope>runtime</scope>
</dependency>
```
- CUDA Supported Versions
```text
  <artifactId>tensorflow-native-cuXXX</artifactId>
  <version>X.X.X</version>
```

```text
# Search ai.djl.tensorflow query: <https://mvnrepository.com/>
# Or <https://repo1.maven.org/maven2/ai/djl/tensorflow/>
CUDA 11.3:  tensorflow-native-cu113
CUDA 11.0:  tensorflow-native-cu110
CUDA 10.1:  tensorflow-native-cu101
```


