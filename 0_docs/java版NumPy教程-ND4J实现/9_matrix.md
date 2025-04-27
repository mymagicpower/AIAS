<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 矩阵

#### 1. 转置矩阵
- Python
NumPy 提供了多种排序的方法。 这些排序函数实现不同的排序算法，每个排序算法的特征在于执行速度，最坏情况性能，所需的工作空间和算法的稳定性。 下表显示了三种排序算法的比较。
```text
import numpy as np
 
a = np.arange(12).reshape(3,4)
 
print ('原数组：')
print (a)
print ('\n')
 
print ('转置数组：')
print (a.T)

# 输出结果如下：
原数组：
[[ 0  1  2  3]
 [ 4  5  6  7]
 [ 8  9 10 11]]


转置数组：
[[ 0  4  8]
 [ 1  5  9]
 [ 2  6 10]
 [ 3  7 11]]
```

- Java
```text
// 1. 转置矩阵
INDArray a = Nd4j.arange(12).reshape(3, 4);
System.out.println("Original Array: ");
// 原始数组：
System.out.println(a);
System.out.println("Transposed Array: ");
// 转置后的数组：
INDArray b = a.transpose();
System.out.println(b);

# 输出结果如下：
Original Array: 
[[         0,    1.0000,    2.0000,    3.0000], 
 [    4.0000,    5.0000,    6.0000,    7.0000], 
 [    8.0000,    9.0000,   10.0000,   11.0000]]

转置数组：
Transposed Array: 
[[         0,    4.0000,    8.0000], 
 [    1.0000,    5.0000,    9.0000], 
 [    2.0000,    6.0000,   10.0000], 
 [    3.0000,    7.0000,   11.0000]]
```

#### 2. 创建一个以 0 填充的矩阵 - zeros()
- Python
numpy.matlib.zeros() 函数创建一个以 0 填充的矩阵。

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.zeros((2,2)))

# 输出结果如下：
[[0. 0.]
 [0. 0.]]
```

- Java
```text
// 2. 创建一个填充为0的矩阵 - zeros()
a = Nd4j.zeros(2, 2);
System.out.println("Matrix filled with zeros:");
// 填充为零的矩阵：
System.out.println(a);

# 输出结果如下：
Matrix filled with zeros:
[[         0,         0], 
 [         0,         0]]
```

#### 3. 创建一个以 1 填充的矩阵 - ones()
- Python
numpy.matlib.ones()函数创建一个以 1 填充的矩阵。

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.ones((2,2)))

# 输出结果如下：
[[1. 1.]
 [1. 1.]]
```

- Java
```text
// 3. 创建一个填充为1的矩阵 - ones()
a = Nd4j.ones(2, 2);
System.out.println("Matrix filled with ones:");
// 填充为1的矩阵：
System.out.println(a);
        
# 输出结果如下：
Matrix filled with ones:
[[    1.0000,    1.0000], 
 [    1.0000,    1.0000]]
```

#### 4. 返回一个矩阵，对角线元素为 1，其他位置为零 - eye()
- Python
numpy.matlib.eye() 函数返回一个矩阵，对角线元素为 1，其他位置为零。
numpy.matlib.eye(n, M,k, dtype)
参数说明：
n: 返回矩阵的行数
M: 返回矩阵的列数，默认为 n
k: 对角线的索引
dtype: 数据类型

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.eye(n =  3, M =  4, k =  0, dtype =  float))

# 输出结果如下：
[[1. 0. 0. 0.]
 [0. 1. 0. 0.]
 [0. 0. 1. 0.]]
```

- Java
```text
// 4. 返回一个对角线元素为1，其他元素为0的矩阵 - eye()
int n = 3;
int m = 4;
INDArray matrix = Nd4j.eye(n); // Create an identity matrix of size n x n
// 创建一个n x n的单位矩阵
if (m > n) {
    // Expand the matrix horizontally to match columns
    // 水平扩展矩阵以匹配列数
    matrix = Nd4j.hstack(matrix, Nd4j.zeros(n, m - n));
} else if (m < n) {
    // Slice the matrix to reduce columns
    // 切割矩阵以减少列数
    matrix = matrix.getColumns(0, m - 1);
}
System.out.println("Identity-like matrix:");
// 类单位矩阵：
System.out.println(matrix);
       
# 输出结果如下：
Identity-like matrix:
[[    1.0000,         0,         0,         0], 
 [         0,    1.0000,         0,         0], 
 [         0,         0,    1.0000,         0]]
```

#### 5. 创建一个给定大小的矩阵，数据是随机填充 - rand()
- Python
numpy.matlib.rand() 函数创建一个给定大小的矩阵，数据是随机填充的。
它从给定形状的[0,1)上的均匀分布返回一个随机值矩阵
```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.rand(3,3))

# 输出结果如下：
[[0.23966718 0.16147628 0.14162   ]
 [0.28379085 0.59934741 0.62985825]
 [0.99527238 0.11137883 0.41105367]]
```

- Java
```text
// 5. 创建一个指定大小并填充随机数据的矩阵 - rand()
a = Nd4j.rand(3, 3);
System.out.println("Random matrix:");
// 随机矩阵：
System.out.println(a);

# 输出结果如下：
Random matrix:
[[    0.1088,    0.9984,    0.8379], 
 [    0.7914,    0.0401,    0.1645], 
 [    0.9196,    0.8394,    0.8428]]
```

#### 6. 内积 - dot()
- Python
numpy.dot() 对于两个一维的数组，计算的是这两个数组对应下标元素的乘积和(数学上称之为内积)；对于二维数组，计算的是两个数组的矩阵乘积；对于多维数组，它的通用计算公式如下，即结果数组中的每个元素都是：数组a的最后一维上的所有元素与数组b的倒数第二位上的所有元素的乘积和： dot(a, b)[i,j,k,m] = sum(a[i,j,:] * b[k,:,m])。
```text
import numpy.matlib
import numpy as np
 
a = np.array([[1,2],[3,4]])
b = np.array([[11,12],[13,14]])
print(np.dot(a,b))

# 计算式为：
# [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]

# 输出结果如下：
[[37  40] 
 [85  92]]
```

- Java
```text
// 6. 点积 - dot()
a = Nd4j.create(new int[][]{{1, 2}, {3, 4}});
b = Nd4j.create(new int[][]{{11, 12}, {13, 14}});
INDArray c = a.mmul(b);  // Equivalent to dot product for matrices
// 相当于矩阵的点积
// The calculation is:
// [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
// 计算过程：
// [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
System.out.println("Dot product result:");
// 点积结果：
System.out.println(c);

// 计算式为：
// [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]

# 输出结果如下：
Dot product result:
[[        37,        40], 
 [        85,        92]]
```

#### 7. 矩阵乘积 - matMul()
- Python
numpy.matmul 函数返回两个数组的矩阵乘积。 虽然它返回二维数组的正常乘积，但如果任一参数的维数大于2，则将其视为存在于最后两个索引的矩阵的栈，并进行相应广播。
另一方面，如果任一参数是一维数组，则通过在其维度上附加 1 来将其提升为矩阵，并在乘法之后被去除。
对于二维数组，它就是矩阵乘法：

```text
import numpy.matlib 
import numpy as np 
 
a = [[1,0],[0,1]] 
b = [[4,1],[2,2]] 
print (np.matmul(a,b))

# 输出结果如下：
[[4  1] 
 [2  2]]
```

- Java
```text
// 7. 矩阵乘法 - mmul()
a = Nd4j.create(new int[][]{{1, 0}, {0, 1}});
b = Nd4j.create(new int[][]{{4, 1}, {2, 2}});
c = a.mmul(b);
System.out.println("Matrix multiplication result:");
// 矩阵乘法结果：
System.out.println(c);

# 输出结果如下：
Matrix multiplication result:
[[         4,         1], 
 [         2,         2]]
```



<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
