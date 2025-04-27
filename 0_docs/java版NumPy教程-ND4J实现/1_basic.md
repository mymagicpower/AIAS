<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 对象
在 ND4J 中，INDArray 是核心的对象，它代表了一个多维数组（或矩阵），是 ND4J 的基础数据结构。通过 INDArray 对象，用户可以进行高效的矩阵运算、数组操作以及与其他数学库的集成。以下是对 INDArray 和与其相关的一些核心对象和功能的简介。

INDArray 是 ND4J 中的主要数据类型，类似于 NumPy 中的 ndarray。
它用于表示一个多维的数值数组，可以处理标量、向量、矩阵以及更高维度的张量。INDArray 支持大量的数学和线性代数操作。

#### 主要特性：
- 高效的数组存储：INDArray 使用底层优化的存储结构（例如，使用 C 或 C++ 实现的 BLAS 和 LAPACK 库），使其在性能上接近原生实现。
- 支持多维数组：ND4J 的 INDArray 可以表示任意维度的数组，支持从一维到高维的数据结构。
- 支持广播：类似于 NumPy，ND4J 支持数组广播，可以进行不同形状的数组之间的运算。


#### 1. 创建数据对象 - 向量
- Python
```text
import numpy as np 
a = np.array([1,2,3])  
print (a)

# 输出结果如下：
[1 2 3]
```

- Java
```text
// 1. 创建数据对象 - 向量
int[] vector = new int[]{1, 2, 3};
INDArray nd = Nd4j.create(vector);
System.out.println(nd);

# 输出结果如下：
[[         1,         2,         3]]
```

#### 2. 创建数据对象 - 矩阵
- Python
```text
# 多于一个维度  
import numpy as np 
a = np.array([[1,  2],  [3,  4]])  
print (a)

# 输出结果如下：
[[1  2] 
 [3  4]]
```

- Java
```text
// 2.1 创建数据对象 - 矩阵
int[][] mat = new int[][]{{1, 2}, {3, 4}};
nd = Nd4j.create(mat);
System.out.println(nd);

# 输出结果如下：
[[         1,         2], 
 [         3,         4]]

// 2.2 创建数据对象 - 矩阵
int[] arr = new int[]{1, 2, 3, 4};
nd = Nd4j.create(arr, new long[]{1, 4}, DataType.INT32);
nd = nd.reshape(2, 2);
System.out.println(nd);

# 输出结果如下：
[[         1,         2], 
 [         3,         4]]

// 2.3 创建数据对象 - 指定矩阵维度
nd = Nd4j.create(new float[]{0.485f, 0.456f, 0.406f}, new long[]{1, 1, 3});
System.out.println(nd);

# 输出结果如下：
[[[    0.4850,    0.4560,    0.4060]]]
```

#### 3. 数据类型转换
- Java
```text
// 3. 数据类型转换
nd = Nd4j.create(arr, new long[]{1, 4}, DataType.INT32);
// 将 INDArray 转换为 FLOAT 类型
INDArray ndFloat = nd.castTo(DataType.FLOAT);
System.out.println(ndFloat);

# 输出结果如下：
[[    1.0000,    2.0000,    3.0000,    4.0000]]
```

#### 4. 数组修改
- Python
```text
import numpy as np

# 4. 创建一个 3x3 的零矩阵
nd = np.zeros((3, 3))

# 将第 0 行第 1 列的值设置为 1.0
nd[0, 1] = 1.0

# 将第 2 行第 2 列的值设置为 3.0
nd[2, 2] = 3.0

# 输出结果如下：
array([[0., 1., 0.],
       [0., 0., 0.],
       [0., 0., 3.]])
```

- Java
```text
// 4. Modifications to array
// 4. 数组修改
nd = Nd4j.zeros(3, 3);
// Set value at row 0, column 1 to value 1.0
// 将第 0 行第 1 列的值设置为 1.0
nd.putScalar(0, 1, 1.0f);
// Set value at row 2, column 2 to value 3.0
// 将第 2 行第 2 列的值设置为 3.0
nd.putScalar(2, 2, 3.0f);

# 输出结果如下：
[[         0,    1.0000,         0], 
 [         0,         0,         0], 
 [         0,         0,    3.0000]]
```

#### 5. 获取单个值
- Python
```text
import numpy as np

# 5. 获取第 0 行第 1 列的值
val = nd[0, 1]
# 输出结果如下：
1.0

```

- Java
```text
// 5. 获取单个值
float val = nd.getFloat(0, 1);
System.out.println("\nValue at (0,1):     " + val);

# 输出结果如下：
Value at (0,1):     1.0

```

#### 6. 创建一个新的副本和独立数组
- Python
```text
import numpy as np

# 6. 创建一个新的副本并独立操作
original_array = np.zeros((3, 3))
# 输出结果如下：
array([[0., 0., 0.],
       [0., 0., 0.],
       [0., 0., 0.]])

# 创建副本并对副本进行操作
new_copy = original_array.copy()
# 对副本进行加法操作
new_copy += 100  
# 输出结果如下：
array([[100., 100., 100.],
       [100., 100., 100.],
       [100., 100., 100.]])

# 打印原始数组，验证原始数组未被修改
array([[0., 0., 0.],
       [0., 0., 0.],
       [0., 0., 0.]])
```

- Java
```text
// 6. 创建一个新的副本和独立数组
nd = Nd4j.zeros(3, 3);
System.out.println("originalArray:\n" + nd);

# 输出结果如下：
originalArray:
[[         0,         0,         0], 
 [         0,         0,         0], 
 [         0,         0,         0]]

INDArray newCopy = nd.dup();
newCopy.addi(100);
System.out.println("After .addi(100):\n" + newCopy);

# 输出结果如下：
After .addi(100):
[[  100.0000,  100.0000,  100.0000], 
 [  100.0000,  100.0000,  100.0000], 
 [  100.0000,  100.0000,  100.0000]]

System.out.println("originalArray:\n" + nd);
# 输出结果如下：
originalArray:
[[         0,         0,         0], 
 [         0,         0,         0], 
 [         0,         0,         0]]
```


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
