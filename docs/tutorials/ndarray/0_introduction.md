<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  

### NDArray 的背景介绍
在Python的世界，调用NDArray的标准包叫做NumPy。为了给Java开发者创造同一种工具，亚马逊云服务开源了DJL，一个基于Java的深度学习库。尽管它包含了深度学习模块，但是它最核心的NDArray库可以被用作NumPy的java替代工具库。
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_intro.png"  width = "600"/>
</div>   

同时它具备优良的可扩展性，全平台支持，以及强大的后端引擎支持 (TensorFlow, PaddlePaddle, PyTorch, Apache MXNet等)。无论是CPU还是GPU, PC还是安卓，DJL都可以轻而易举的完成任务。
#### 架构： 
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_arc.png"  width = "600"/>
</div>   

在这个系列文章中，我们将带你了解NDArray，并且教你如何写与Numpy同样简单的Java代码以及如何将NDArray使用在现实中的应用之中。
NDArray相当于python numpy的java实现，解决复杂的矩阵运算问题。多维数组存在于native C++ 内存里，如此可以方便调用下面的加速库：
- 矩阵加速库：LAPACK, BLAS    
- CPU加速库：oneDNN(MKLDNN)    
- GPU加速库：CUDA, cuDNN   
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndarray.png"  width = "600"/>
</div>   

NDArray提供了丰富的api，如：
四则运算： add, sub, mul, div, …
矩阵运算：matMul
比较运算：eq, gt, ….
归约运算：sum, max, min, …
其它运算：abs, exp,…
改变形状：reshape, swapAxes, …
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndarray2.png"  width = "600"/>
</div>   

为了更好的管理NDArray，管理数据的创建及生命周期, 创建所有NDArray都需要通过NDManager。如此可以高效的利用内存，防止内存泄露等问题。
NDManager是DJL中的一个class可以帮助管理NDArray的内存使用。通过创建NDManager，可以及时的对内存进行清理。当这个block里的任务运行完成时，内部产生的NDArray都会被清理掉。这个设计保证了我们在大规模使用NDArray的过程中，可以更高效的利用内存。
```text
try (NDManager manager = NDManager.newBaseManager()) {
  ...
}
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndmanager.png"  width = "600"/>
</div>   

熟练掌握了NDArray的使用后，除了可以完成复杂的矩阵处理，计算外，如何用于模型的推理部署呢？
模型的推理可以概括为三个步骤：
- 数据预处理    
- 推理    
- 数据后处理    
在预处理过程中，我们需要完成把图片转成RGB数组，把文字转换成索引id，把音频转换成float数组，数据归一化等操作。
在后处理过程中，我们需要完成把概率转换为对应的标签，把文字索引转换回文字等操作。
在数据预处理/数据后处理过程中，NDArray起了最关键的作用。
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_process.png"  width = "600"/>
</div>   

DJL为了统一代码处理逻辑，增加代码的复用性，提供了高级接口Translator：
```text
public NDList processInput(TranslatorContext ctx, I)
public O processOutput(TranslatorContext ctx, NDList list)
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_process2.png"  width = "600"/>
</div>  

对NDArray有了基本的了解后，通过学习后续的教程，可以进一步掌握NDArray的使用。

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/tree/main/0_tutorials/ndarray_lessons)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/tree/main/0_tutorials/ndarray_lessons)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
