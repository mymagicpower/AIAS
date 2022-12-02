<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### NDArray 矩阵

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
NDArray a = manager.arange(12).reshape(3, 4);
System.out.println("原数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("转置数组：");
NDArray b = a.transpose();
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
原数组：
ND: (3, 4) cpu() int32
[[ 0,  1,  2,  3],
 [ 4,  5,  6,  7],
 [ 8,  9, 10, 11],
]

转置数组：
ND: (4, 3) cpu() int32
[[ 0,  4,  8],
 [ 1,  5,  9],
 [ 2,  6, 10],
 [ 3,  7, 11],
]
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
a = manager.zeros(new Shape(2, 2));
System.out.println(a.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() float32
[[0., 0.],
 [0., 0.],
]
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
a = manager.ones(new Shape(2, 2));
System.out.println(a.toDebugString(100, 10, 100, 100));  
        
# 输出结果如下：
ND: (2, 2) cpu() float32
[[1., 1.],
 [1., 1.],
]
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
a = manager.eye(3,4,0, DataType.INT32);
System.out.println(a.toDebugString(100, 10, 100, 100));
       
# 输出结果如下：
ND: (3, 4) cpu() int32
[[ 1,  0,  0,  0],
 [ 0,  1,  0,  0],
 [ 0,  0,  1,  0],
]
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
a = manager.randomUniform(0,1,new Shape(3,3));
System.out.println(a.toDebugString(100, 10, 100, 100));
     
# 输出结果如下：
ND: (3, 3) cpu() float32
[[0.356 , 0.9904, 0.1063],
 [0.8469, 0.5733, 0.1028],
 [0.7271, 0.0218, 0.8271],
]
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
NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
NDArray b = manager.create(new int[][]{{11, 12}, {13, 14}});
NDArray c = a.dot(b);
// 计算式为：
// [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
System.out.println(c.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() int32
[[37, 40],
 [85, 92],
]
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
a = manager.create(new int[][]{{1, 0}, {0, 1}});
b = manager.create(new int[][]{{4, 1}, {2, 2}});
c = a.matMul(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() int32
[[ 4,  1],
 [ 2,  2],
]
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No9MatrixExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No9MatrixExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
