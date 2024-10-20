<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### Ndarray数组
本节我们将来了解数组的一些基本属性。
在 NumPy中，每一个线性的数组称为是一个轴（axis），也就是维度（dimensions）。比如说，二维数组相当于是两个一维数组，其中第一个一维数组中每个元素又是一个一维数组。所以一维数组就是 NumPy 中的轴（axis），第一个轴相当于是底层数组，第二个轴是底层数组里的数组。而轴的数量就是数组的维数。

#### 1. 数组的维数
- Python
```text
import numpy as np 
 
a = np.arange(24)  
print (a.ndim)        # a 现只有一个维度
# 现在调整其大小
b = a.reshape(2,4,3)  # b 现在拥有三个维度
print (b.ndim)

# 输出结果如下：
1
3
```

- Java
```text
NDArray nd = manager.arange(24); // 现只有一个维度
System.out.println(nd.getShape().dimension());
// 现在调整其大小
nd = nd.reshape(2, 4, 3); // 现在拥有三个维度
System.out.println(nd.getShape().dimension());

# 输出结果如下：
1
3
```

#### 2. 数组的shape
- Python
```text
# ndarray.shape 表示数组的维度，返回一个元组，这个元组的长度就是维度的数目，即 ndim 属性。比如，一个二维数组，其维度表示"行数"和"列数"。
import numpy as np  
a = np.array([[1,2,3],[4,5,6]])  
print (a.shape)

# 输出结果如下：
(2, 3)
```

- Java
```text
nd = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
System.out.println(nd.getShape());

# 输出结果如下：
(2, 3)
```

#### 3. 调整数组形状
- Python
```text
import numpy as np 
 
a = np.array([[1,2,3],[4,5,6]]) 
b = a.reshape(3,2)  
print (b)

# 输出结果如下：
[[1, 2] 
 [3, 4] 
 [5, 6]]
```

- Java
```text
nd = nd.reshape(3, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (3, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
 [ 5,  6],
]
```


#### 4. 创建数组zeros
创建指定大小的数组，数组元素以 0 来填充
- Python
```text
import numpy as np 
# 设置类型为整数
y = np.zeros((5,), dtype = np.int) 
print(y)

# 输出结果如下：
[0 0 0 0 0]
```

- Java
```text
nd = manager.zeros(new Shape(5), DataType.INT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (5) cpu() int32
[ 0,  0,  0,  0,  0]
```

#### 5. 创建数组ones
创建指定大小的数组，数组元素以 0 来填充
- Python
```text
import numpy as np 
# 自定义类型
x = np.ones([2,2], dtype = int)
print(x)

# 输出结果如下：
[[1 1]
 [1 1]]
```

- Java
```text
nd = manager.ones(new Shape(2, 2), DataType.INT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() int32
[[ 1,  1],
 [ 1,  1],
]
```

#### 6. 从数值范围创建数组
numpy 包中的使用 arange 函数创建数值范围并返回 ndarray 对象，函数格式如下：
```text
# 根据 start 与 stop 指定的范围以及 step 设定的步长，生成一个 ndarray。
numpy.arange(start, stop, step, dtype)
```
##### 6.1 生成 0 到 5 的数组
- Python
```text
import numpy as np

x = np.arange(5)  
print (x)

# 输出结果如下：
[0  1  2  3  4]
```

- Java
```text
nd = manager.arange(5);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[ 0,  1,  2,  3,  4]
```

##### 6.2 设置返回类型为 float
- Python
```text
# 设置了 dtype
x = np.arange(5, dtype =  float)  
print (x)


# 输出结果如下：
[0.  1.  2.  3.  4.]
```
- Java
```text
nd = manager.arange(0, 5, 1, DataType.FLOAT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[0., 1., 2., 3., 4.]
```

##### 6.3 设置了起始值、终止值及步长
- Python
```text
x = np.arange(10,20,2)  
print (x)

# 输出结果如下：
[10  12  14  16  18]
```
- Java
```text
nd = manager.arange(10, 20, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[10, 12, 14, 16, 18]
```

##### 6.4 等差数列 linspace
- Python
```text
# numpy.linspace 函数用于创建一个一维数组，数组是一个等差数列构成的，格式如下：
# np.linspace(start, stop, num=50, endpoint=True, retstep=False, dtype=None)
# start	     序列的起始值
# stop	     序列的终止值，如果endpoint为true，该值包含于数列中
# num	     要生成的等步长的样本数量，默认为50
# endpoint   该值为 true 时，数列中包含stop值，反之不包含，默认是True。
# retstep	 如果为 True 时，生成的数组中会显示间距，反之不显示。
# dtype	     ndarray 的数据类型
# 以下实例用到三个参数，设置起始点为 1 ，终止点为 10，数列个数为 10。
a = np.linspace(1,10,10)
print(a)

# 输出结果如下：
[ 1.  2.  3.  4.  5.  6.  7.  8.  9. 10.]
```
- Java
```text
nd = manager.linspace(1,10,10);
System.out.println(nd.toDebugString(100, 10, 100, 100));
# 输出结果如下：
[10, 12, 14, 16, 18]
[ 1.,  2.,  3.,  4.,  5.,  6.,  7.,  8.,  9., 10.]
```

#### 7. 数组操作
创建指定大小的数组，数组元素以 0 来填充

##### 7.1 翻转数组 numpy.transpose
- Python
```text
import numpy as np
 a = np.arange(12).reshape(3,4)
 
print ('原数组：')
print (a )
print ('\n')
 
print ('对换数组：')
print (np.transpose(a))

# 输出结果如下：
原数组：
[[ 0  1  2  3]
 [ 4  5  6  7]
 [ 8  9 10 11]]

对换数组：
[[ 0  4  8]
 [ 1  5  9]
 [ 2  6 10]
 [ 3  7 11]]

```
- Java
```text
nd = manager.arange(12).reshape(3,4);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.transpose();
System.out.println(nd.toDebugString(100, 10, 100, 100));
// 输出结果如下：
原数组：
[[ 0,  1,  2,  3],
 [ 4,  5,  6,  7],
 [ 8,  9, 10, 11],
]

对换数组：
[[ 0,  4,  8],
 [ 1,  5,  9],
 [ 2,  6, 10],
 [ 3,  7, 11],
]
```

##### 7.2 交换数组的两个轴 numpy.swapaxes
- Python
```text
# 创建了三维的 ndarray
a = np.arange(8).reshape(2,2,2)
 
print ('原数组：')
print (a)
print ('\n')
# 现在交换轴 0（深度方向）到轴 2（宽度方向）
 
print ('调用 swapaxes 函数后的数组：')
print (np.swapaxes(a, 2, 0))

# 输出结果如下：
原数组：
[[[0 1]
  [2 3]]

 [[4 5]
  [6 7]]]

调用 swapaxes 函数后的数组：
[[[0 4]
  [2 6]]

 [[1 5]
  [3 7]]]
```
- Java
```text
nd = manager.arange(8).reshape(2, 2, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.swapAxes(2, 0);
System.out.println(nd.toDebugString(100, 10, 100, 100));

// 输出结果如下：
原数组：
[[[ 0,  1],
  [ 2,  3],
 ],
 [[ 4,  5],
  [ 6,  7],
 ],
]

调用 swapaxes 函数后的数组：
[[[ 0,  4],
  [ 2,  6],
 ],
 [[ 1,  5],
  [ 3,  7],
 ],
]

```

##### 7.3 广播 numpy.broadcast_to
- Python
```text
# numpy.broadcast_to 函数将数组广播到新形状。
import numpy as np
 
a = np.arange(4).reshape(1,4)
 
print ('原数组：')
print (a)
print ('\n')
 
print ('调用 broadcast_to 函数之后：')
print (np.broadcast_to(a,(4,4)))

# 输出结果如下：
原数组：
[[0 1 2 3]]


调用 broadcast_to 函数之后：
[[0 1 2 3]
 [0 1 2 3]
 [0 1 2 3]
 [0 1 2 3]]

```
- Java
```text
nd = manager.arange(4).reshape(1, 4);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.broadcast(new Shape(4, 4));
System.out.println(nd.toDebugString(100, 10, 100, 100));

// 输出结果如下：
原数组：
[[ 0,  1,  2,  3],
]

// 调用broadcast函数之后：
[[ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
]

```

##### 7.4 在指定位置插入新的轴来扩展数组形状 numpy.expand_dims
- Python
```text
import numpy as np
 
x = np.array(([1,2],[3,4]))
 
print ('数组 x：')
print (x)
print ('\n')
y = np.expand_dims(x, axis = 0)
 
print ('数组 y：')
print (y)
print ('\n')
 
print ('数组 x 和 y 的形状：')
print (x.shape, y.shape)
print ('\n')
# 在位置 1 插入轴
y = np.expand_dims(x, axis = 1)
 
print ('在位置 1 插入轴之后的数组 y：')
print (y)
print ('\n')
 
print ('x.ndim 和 y.ndim：')
print (x.ndim,y.ndim)
print ('\n')
 
print ('x.shape 和 y.shape：')
print (x.shape, y.shape)

# 输出结果如下：
数组 x：
[[1 2]
 [3 4]]

数组 y：
[[[1 2]
  [3 4]]]

数组 x 和 y 的形状：
(2, 2) (1, 2, 2)

在位置 1 插入轴之后的数组 y：
[[[1 2]]
 [[3 4]]]

x.ndim 和 y.ndim：
2 3

x.shape 和 y.shape：
(2, 2) (2, 1, 2)

```
- Java
```text
NDArray x = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("数组 x：");
System.out.println(x.toDebugString(100, 10, 100, 100));
// 在位置 0 插入轴
NDArray y = x.expandDims(0);
System.out.println("数组 y：");
System.out.println(y.toDebugString(100, 10, 100, 100));
System.out.println("数组 x 和 y 的形状：");
System.out.println(x.getShape() + " " + y.getShape());
// 在位置 1 插入轴
y = x.expandDims(1);
System.out.println("在位置 1 插入轴之后的数组 y：");
System.out.println(y.toDebugString(100, 10, 100, 100));

System.out.println("x.ndim 和 y.ndim：");
System.out.println(x.getShape().dimension() + " " + y.getShape().dimension());

System.out.println("数组 x 和 y 的形状：");
System.out.println(x.getShape() + " " + y.getShape());

// 输出结果如下：
数组 x：
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

数组 y：
ND: (1, 2, 2) cpu() int32
[[[ 1,  2],
  [ 3,  4],
 ],
]

数组 x 和 y 的形状：
(2, 2) (1, 2, 2)
在位置 1 插入轴之后的数组 y：
ND: (2, 1, 2) cpu() int32
[[[ 1,  2],
 ],
 [[ 3,  4],
 ],
]

x.ndim 和 y.ndim：
2 3
数组 x 和 y 的形状：
(2, 2) (2, 1, 2)
```

##### 7.5 从给定数组的形状中删除一维的条目 numpy.squeeze
- Python
```text
import numpy as np
 
x = np.arange(9).reshape(1,3,3)
 
print ('数组 x：')
print (x)
print ('\n')
y = np.squeeze(x)
 
print ('数组 y：')
print (y)
print ('\n')
 
print ('数组 x 和 y 的形状：')
print (x.shape, y.shape)

# 输出结果如下：
数组 x：
[[[0 1 2]
  [3 4 5]
  [6 7 8]]]


数组 y：
[[0 1 2]
 [3 4 5]
 [6 7 8]]


数组 x 和 y 的形状：
(1, 3, 3) (3, 3)
```
- Java
```text
x = manager.arange(9).reshape(1, 3, 3);
System.out.println("数组 x：");
System.out.println(x.toDebugString(100, 10, 100, 100));

y = x.squeeze();
System.out.println("数组 y：");
System.out.println(y.toDebugString(100, 10, 100, 100));

System.out.println("数组 x 和 y 的形状：");
System.out.println(x.getShape() + " " + y.getShape());

// 输出结果如下：
数组 x：
ND: (1, 3, 3) cpu() int32
[[[ 0,  1,  2],
  [ 3,  4,  5],
  [ 6,  7,  8],
 ],
]

数组 y：
ND: (3, 3) cpu() int32
[[ 0,  1,  2],
 [ 3,  4,  5],
 [ 6,  7,  8],
]

数组 x 和 y 的形状：
(1, 3, 3) (3, 3)
```

##### 7.6 连接数组 numpy.concatenate
- Python
```text
import numpy as np
 
a = np.array([[1,2],[3,4]])
 
print ('第一个数组：')
print (a)
print ('\n')
b = np.array([[5,6],[7,8]])
 
print ('第二个数组：')
print (b)
print ('\n')
# 两个数组的维度相同
 
print ('沿轴 0 连接两个数组：')
print (np.concatenate((a,b)))
print ('\n')
 
print ('沿轴 1 连接两个数组：')
print (np.concatenate((a,b),axis = 1))

# 输出结果如下：
第一个数组：
[[1 2]
 [3 4]]


第二个数组：
[[5 6]
 [7 8]]


沿轴 0 连接两个数组：
[[1 2]
 [3 4]
 [5 6]
 [7 8]]


沿轴 1 连接两个数组：
[[1 2 5 6]
 [3 4 7 8]]

```
- Java
```text
NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("第一个数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
System.out.println("第二个数组：");
System.out.println(b.toDebugString(100, 10, 100, 100));

nd = NDArrays.concat(new NDList(a, b));
System.out.println("沿轴 0 连接两个数组：");
System.out.println(nd.toDebugString(100, 10, 100, 100));

nd = NDArrays.concat(new NDList(a, b), 1);
System.out.println("沿轴 1 连接两个数组：");
System.out.println(nd.toDebugString(100, 10, 100, 100));

// 输出结果如下：
第一个数组：
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

第二个数组：
ND: (2, 2) cpu() int32
[[ 5,  6],
 [ 7,  8],
]

沿轴 0 连接两个数组：
ND: (4, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
 [ 5,  6],
 [ 7,  8],
]

沿轴 1 连接两个数组：
ND: (2, 4) cpu() int32
[[ 1,  2,  5,  6],
 [ 3,  4,  7,  8],
]
```

##### 7.7 沿新轴堆叠数组序列 numpy.stack
- Python
```text
import numpy as np
 
a = np.array([[1,2],[3,4]])
 
print ('第一个数组：')
print (a)
print ('\n')
b = np.array([[5,6],[7,8]])
 
print ('第二个数组：')
print (b)
print ('\n')
 
print ('沿轴 0 堆叠两个数组：')
print (np.stack((a,b),0))
print ('\n')
 
print ('沿轴 1 堆叠两个数组：')
print (np.stack((a,b),1))

# 输出结果如下：
第一个数组：
[[1 2]
 [3 4]]


第二个数组：
[[5 6]
 [7 8]]


沿轴 0 堆叠两个数组：
[[[1 2]
  [3 4]]

 [[5 6]
  [7 8]]]


沿轴 1 堆叠两个数组：
[[[1 2]
  [5 6]]

 [[3 4]
  [7 8]]]
```
- Java
```text
a = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("第一个数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));

b = manager.create(new int[][]{{5, 6}, {7, 8}});
System.out.println("第二个数组：");
System.out.println(b.toDebugString(100, 10, 100, 100));

nd = NDArrays.stack(new NDList(a, b));
System.out.println("沿轴 0 堆叠两个数组：");
System.out.println(nd.toDebugString(100, 10, 100, 100));

nd = NDArrays.stack(new NDList(a, b), 1);
System.out.println("沿轴 1 堆叠两个数组：");
System.out.println(nd.toDebugString(100, 10, 100, 100));


// 输出结果如下：
第一个数组：
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

第二个数组：
ND: (2, 2) cpu() int32
[[ 5,  6],
 [ 7,  8],
]

沿轴 0 堆叠两个数组：
ND: (2, 2, 2) cpu() int32
[[[ 1,  2],
  [ 3,  4],
 ],
 [[ 5,  6],
  [ 7,  8],
 ],
]

沿轴 1 堆叠两个数组：
ND: (2, 2, 2) cpu() int32
[[[ 1,  2],
  [ 5,  6],
 ],
 [[ 3,  4],
  [ 7,  8],
 ],
]
```

##### 7.8 沿特定的轴将数组分割为子数组 numpy.split
- Python
```text
import numpy as np
 
a = np.arange(9)
 
print ('第一个数组：')
print (a)
print ('\n')
 
print ('将数组分为三个大小相等的子数组：')
b = np.split(a,3)
print (b)
print ('\n')
 
print ('将数组在一维数组中表明的位置分割：')
b = np.split(a,[4,7])
print (b)

# 输出结果如下：
第一个数组：
[0 1 2 3 4 5 6 7 8]


将数组分为三个大小相等的子数组：
[array([0, 1, 2]), array([3, 4, 5]), array([6, 7, 8])]


将数组在一维数组中表明的位置分割：
[array([0, 1, 2, 3]), array([4, 5, 6]), array([7, 8])]


```

- Java
```text
a = manager.arange(9);
System.out.println("第一个数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDList list = a.split(3);
System.out.println("将数组分为三个大小相等的子数组：");
System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

list = a.split(new long[]{4, 7});
System.out.println("将数组在一维数组中表明的位置分割：");
System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

// 输出结果如下：
第一个数组：
ND: (9) cpu() int32
[ 0,  1,  2,  3,  4,  5,  6,  7,  8]

将数组分为三个大小相等的子数组：
ND: (3) cpu() int32
[ 0,  1,  2]

ND: (3) cpu() int32
[ 3,  4,  5]

ND: (3) cpu() int32
[ 6,  7,  8]

将数组在一维数组中表明的位置分割：
ND: (4) cpu() int32
[ 0,  1,  2,  3]

ND: (3) cpu() int32
[ 4,  5,  6]

ND: (2) cpu() int32
[ 7,  8]
```


### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No2ArrayExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No2ArrayExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
