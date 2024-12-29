
### Introduction to NDArray

In the world of Python, the standard package for working with NDArrays is NumPy. To create a similar tool for Java developers, Amazon Web Services open-sourced DJL, a deep learning library based on Java. Although it contains deep learning modules, its core NDArray library can be used as a Java replacement for NumPy.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_intro.png"  width = "600"/>
</div>   

At the same time, DJL has excellent scalability, full platform support, and powerful backend engine support (TensorFlow, PaddlePaddle, PyTorch, Apache MXNet, etc.). Whether it's CPU or GPU, PC or Android, DJL can easily complete tasks.

#### Architecture:

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_arc.png"  width = "600"/>
</div>   

In this article series, we will introduce you to NDArray and teach you how to write Java code that is just as simple as Numpy and how to use NDArray in real-world applications.
NDArray is a Java implementation of Python's NumPy that solves complex matrix operations. Multidimensional arrays exist in native C++ memory, making it easy to call the following acceleration libraries:
- Matrix acceleration library: LAPACK, BLAS
- CPU acceleration library: oneDNN(MKLDNN)   
- GPU acceleration library: CUDA, cuDNN

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndarray.png"  width = "600"/>
</div>   

NDArray provides a rich API, such as:
Arithmetic operations: add, sub, mul, div, …
Matrix operations: matMul
Comparison operations: eq, gt, ….
Reduction operations: sum, max, min, …
Other operations: abs, exp,…
Reshape: reshape, swapAxes, …

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndarray2.png"  width = "600"/>
</div>   

To better manage NDArray and the creation and lifecycle of data, all NDArrays need to be created through NDManager. This can efficiently use memory and prevent memory leaks and other issues.

NDManager is a class in DJL that helps manage the memory usage of NDArray. By creating an NDManager, memory can be cleaned up promptly. When the tasks in this block are completed, the NDArrays generated internally will be cleared. This design ensures that we can efficiently use memory when using NDArray on a large scale.

```text
try (NDManager manager = NDManager.newBaseManager()) {
  ...
}
```
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_ndmanager.png"  width = "600"/>
</div>   

After mastering the use of NDArray, how can it be used for model inference deployment?

Model inference can be summarized in three steps:
- Data preprocessing
- Inference
- Data post-processing

In the preprocessing process, we need to convert images into RGB arrays, convert text into index IDs, convert audio into float arrays, normalize data, etc.
In the post-processing process, we need to convert probabilities into corresponding labels, convert text indexes back to text, etc.
NDArray plays the most critical role in data preprocessing/data post-processing.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_process.png"  width = "600"/>
</div>   

To unify code processing logic, increase code reusability, and provide advanced interfaces, DJL provides a high-level interface called Translator.

```text
public NDList processInput(TranslatorContext ctx, I)
public O processOutput(TranslatorContext ctx, NDList list)
```

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/tutorials/ndarray/images/djl_process2.png"  width = "600"/>
</div>  

After gaining a basic understanding of NDArray, you can further master its use by studying the following tutorials.

### Code download link:
[Github](https://github.com/mymagicpower/AIAS/tree/main/0_tutorials/ndarray_lessons)    
