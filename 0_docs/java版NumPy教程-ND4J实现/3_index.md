<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 索引切片 - 通过索引或切片来访问 INDArray 对象的内容
INDArray 对象的内容可以通过索引或切片来访问和修改，与 Python 中 list 的切片操作一样。
INDArray 数组可以基于 0 - n 的下标进行索引，切片对象可以通过内置的 slice 函数，并设置 start, stop 及 step 参数进行，从原数组中切割出一个新数组。

#### 1. 冒号分隔切片参数 start:stop:step 来进行切片
- Python
```text
import numpy as np
 
a = np.arange(10)
s = slice(2,7,2)   # 从索引 2 开始到索引 7 停止，间隔为2
print (a[s])

# 输出结果如下：
[2  4  6]

# 以上实例中，我们首先通过 arange() 函数创建 ndarray 对象。 然后，分别设置起始，终止和步长的参数为 2，7 和 2。
# 我们也可以通过冒号分隔切片参数 start:stop:step 来进行切片操作：
a = np.arange(10)  
b = a[2:7:2]   # 从索引 2 开始到索引 7 停止，间隔为 2
print(b)

# 输出结果如下：
[2  4  6]
```

- Java
```text
// 1. 使用索引或切片访问和修改ndarray对象的内容。
INDArray a = Nd4j.arange(10);
INDArray b = a.get(NDArrayIndex.interval(2, 2, 7)); // Slice with step 2
// 使用步长为2的切片
System.out.println("1. Slice a[2:7:2]:\n" + b);

# 输出结果如下：
[    2.0000,    4.0000,    6.0000]
```

#### 2. 冒号 :的使用
冒号 : 的解释：如果只放置一个参数，如 [2]，将返回与该索引相对应的单个元素。如果为 [2:]，表示从该索引开始以后的所有项都将被提取。如果使用了两个参数，如 [2:7]，那么则提取两个索引(不包括停止索引)之间的项。

- Python
```text
import numpy as np
# 实例 2.1
a = np.arange(10)  # [0 1 2 3 4 5 6 7 8 9]
b = a[5] 
print(b)

# 输出结果如下：
5

# 实例 2.2
a = np.arange(10)
print(a[2:])

# 输出结果如下：
[2  3  4  5  6  7  8  9]

# 实例 2.3
a = np.arange(10)  # [0 1 2 3 4 5 6 7 8 9]
print(a[2:5])

# 输出结果如下：
[2  3  4]
```

- Java
```text
// 示例 2.1
a = Nd4j.arange(10);
b = a.get(NDArrayIndex.point(5)); 
// 访问a[5]
System.out.println("2.1 Element a[5]:\n" + b);

# 输出结果如下：
5.0000

// 示例 2.2
a = Nd4j.arange(10);
b = a.get(NDArrayIndex.interval(2, a.length())); // Slice a[2:]

# 输出结果如下：
[    2.0000,    3.0000,    4.0000,    5.0000,    6.0000,    7.0000,    8.0000,    9.0000]

# 实例 2.3
a = Nd4j.arange(10);
b = a.get(NDArrayIndex.interval(2, 5)); 
System.out.println("2.3 Slice a[2:5]:\n" + b);

# 输出结果如下：
[    2.0000,    3.0000,    4.0000]
```

#### 3. 多维数组索引
- Python
```text
import numpy as np
# 实例 3.1
a = np.array([[1,2,3],[3,4,5],[4,5,6]])
print(a)
# 从某个索引处开始切割
print('从数组索引 a[1:] 处开始切割')
print(a[1:])

# 输出结果如下：
[[1 2 3]
 [3 4 5]
 [4 5 6]]
从数组索引 a[1:] 处开始切割
[[3 4 5]
 [4 5 6]]

# 实例 3.2 省略号 …(或者冒号:)
# 切片还可以包括省略号 …(或者冒号:)，来使选择元组的长度与数组的维度相同。 如果在行位置使用省略号，它将返回包含行中元素的 ndarray。
a = np.array([[1,2,3],[3,4,5],[4,5,6]])  
print (a[...,1])   # 第2列元素
print (a[1,...])   # 第2行元素
print (a[...,1:])  # 第2列及剩下的所有元素 

# 输出结果如下：
[2 4 5]
[3 4 5]
[[2 3]
 [4 5]
 [5 6]]

```
- Java
```text
# 3.1 多维数组索引。
a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println("3.1 Original 2D array:\n" + a);
b = a.get(NDArrayIndex.interval(1, a.rows()), NDArrayIndex.all()); // Slice a[1:] or Slice a[1:,:]
// 切片 a[1:] 或 a[1,:]
System.out.println("3.1 Slice a[1:]:\n " + b);
//  输出结果会缺最后一行，或者最后一列，只是打印输出问题，数据本身是正确的。
INDArray flattened = Nd4j.toFlattened(b);
System.out.println("3.1 Slice a[1:]: toFlattened\n " + flattened);

# 输出结果如下：
3.1 Original 2D array:
[[         1,         2,         3], 
 [         3,         4,         5], 
 [         4,         5,         6]]
3.1 Slice a[1:]:
 [[         3,         4,         5], 
 []]
3.1 Slice a[1:]: toFlattened
 [         3,         4,         5,         4,         5,         6]


// 3.2 多维数组索引，省略号 ... （或冒号 :）。
a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println("3.2 Original 2D array:\n" + a);
System.out.println("Elements from the 2nd column: ");
b = a.getColumn(1); // Slice a[:,1]
System.out.println(b);
System.out.println("Elements from the 2nd row: ");

b = a.getRow(1); // Slice a[1,:]
System.out.println(b);
System.out.println("Elements from the 2nd column and all remaining elements: ");
b = a.get(NDArrayIndex.all(), NDArrayIndex.interval(1, a.columns())); // Slice a[:,1:]
System.out.println(b);

//  输出结果会缺最后一行，或者最后一列，只是打印输出问题，数据本身是正确的。
flattened = Nd4j.toFlattened(b);
System.out.println("toFlattened\n " + flattened);

# 输出结果如下：
3.2 Original 2D array:
[[         1,         2,         3], 
 [         3,         4,         5], 
 [         4,         5,         6]]
Elements from the 2nd column: 
[         2,         4,         5]
Elements from the 2nd row: 
[         3,         4,         5]
Elements from the 2nd column and all remaining elements: 
[[         2,         3], 
 [         4,         5], 
 []]
toFlattened
 [         2,         3,         4,         5,         5,         6]

```

#### 4. 布尔索引
我们可以通过一个布尔数组来索引目标数组。
布尔索引通过布尔运算（如：比较运算符）来获取符合指定条件的元素的数组。
以下实例获取大于 5 的元素：

- Python
```text
import numpy as np
x = np.array([[  0,  1,  2],[  3,  4,  5],[  6,  7,  8],[  9,  10,  11]])  
print ('我们的数组是：')
print (x)
print ('\n')
# 现在我们会打印出大于 5 的元素  
print  ('大于 5 的元素是：')
print (x[x >  5])

# 输出结果如下：
我们的数组是：
[[ 0  1  2]
 [ 3  4  5]
 [ 6  7  8]
 [ 9 10 11]]

大于 5 的元素是：
[ 6  7  8  9 10 11]
```

- Java
```text
// 4. 布尔索引。
INDArray x = Nd4j.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
System.out.println("Our array is: ");
// 我们的数组是：
System.out.println(x);
System.out.println("Elements greater than 5 are: ");
// 大于5的元素是：
INDArray mask = x.gt(5);
INDArray y = Nd4j.toFlattened(x.mul(mask)); 
// 使用带掩码的逐元素乘法
System.out.println(y);

# 输出结果如下：
我们的数组是：
[[         0,         1,         2], 
 [         3,         4,         5], 
 [         6,         7,         8], 
 [         9,        10,        11]]

大于 5 的元素是：
[         0,         0,         0,         0,         0,         0,         6,         7,         8,         9,        10,        11]
```

#### 5. 索引赋值
我们可以通过索引来获取符合指定条件的元素的数组，并进行赋值：

- Python
```text
import numpy as np

# 第一部分: 将第3列（索引2）设置为零
a = np.array([[1, 2, 3], [3, 4, 5], [4, 5, 6]])
zeros_column = np.zeros(3)  # 创建一个包含3个零的数组
a[:, 2] = zeros_column  # 将第3列设置为零
print("\nAfter setting operation:")
print(a)
[[ 1,  2,  0],
 [ 3,  4,  0],
 [ 4,  5,  0],
]

# 第二部分: 将第2和第3列（索引1,2）设置为零
a = np.array([[1, 2, 3], [3, 4, 5], [4, 5, 6]])
zeros_columns = np.zeros((3, 2))  # 创建一个3x2的零矩阵
a[:, 1:3] = zeros_columns  # 将第2和第3列设置为零
print("\nAfter setting operation:")
print(a)
[[ 1,  0,  0],
 [ 3,  0,  0],
 [ 4,  0,  0],
]
```

- Java
```text
// 5. 设置值
// 5.1 将第三列（索引2）设置为零：
a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
INDArray zerosColumn = Nd4j.zeros(3, 1);
a.put(new INDArrayIndex[]{NDArrayIndex.all(), NDArrayIndex.point(2)}, zerosColumn);
System.out.println("\n\n\nAfter setting operation:\n" + a);

// 5.2 将第二、三列（索引1,2）设置为零：
a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
INDArray zerosColumns = Nd4j.zeros(3, 2);
a.put(new INDArrayIndex[]{NDArrayIndex.all(), NDArrayIndex.interval(1, 3)}, zerosColumns);
System.out.println("\n\n\nAfter setting operation:\n" + a);


# 输出结果如下：
After setting operation:
[[         1,         2,         0], 
 [         3,         4,         0], 
 [         4,         5,         0]]



After setting operation:
[[         1,         0,         0], 
 [         3,         0,         0], 
 [         4,         0,         0]]
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No3IndexExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No3IndexExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
