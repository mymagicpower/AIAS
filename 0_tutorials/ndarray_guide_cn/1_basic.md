<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### Ndarray 对象
NumPy 最重要的一个特点是其 N 维数组对象 ndarray，它是一系列同类型数据的集合，以 0 下标为开始进行集合中元素的索引。
ndarray 对象是用于存放同类型元素的多维数组。
ndarray 中的每个元素在内存中都有相同存储大小的区域。
 
NDManager是DJL中的一个class可以帮助管理NDArray的内存使用。通过创建NDManager，可以及时的对内存进行清理。当这个block里的任务运行完成时，内部产生的NDArray都会被清理掉。这个设计保证了我们在大规模使用NDArray的过程中，可以更高效的利用内存。
```text
try (NDManager manager = NDManager.newBaseManager()) {
  ...
}
```

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
int[] vector = new int[]{1, 2, 3};
NDArray nd = manager.create(vector);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (3) cpu() int32
[ 1,  2,  3]
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
# 2.1 创建数据对象 - 矩阵
int[][] mat = new int[][]{{1, 2}, {3, 4}};
NDArray nd = manager.create(vector);
nd = manager.create(mat);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

# 2.2 创建数据对象 - 矩阵
int[] arr = new int[]{1, 2, 3, 4};
nd = manager.create(arr).reshape(2, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

# 2.3 创建数据对象 - 指定矩阵维度
nd = manager.create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (1, 1, 3) cpu() float32
[[[0.485, 0.456, 0.406],
 ],
]
```

#### 3. 数据类型转换
- Java
```text
nd = manager.create(new int[]{1, 2, 3, 4}).toType(DataType.FLOAT32, false);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (4) cpu() float32
[1., 2., 3., 4.]
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No1BasicExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No1BasicExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
