<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### Ndarray 索引切片 - 通过索引或切片来访问ndarray对象的内容
ndarray对象的内容可以通过索引或切片来访问和修改，与 Python 中 list 的切片操作一样。
ndarray 数组可以基于 0 - n 的下标进行索引，切片对象可以通过内置的 slice 函数，并设置 start, stop 及 step 参数进行，从原数组中切割出一个新数组。

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
NDArray a = manager.arange(10);
NDArray b = a.get("2:7:2");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (3) cpu() int32
[ 2,  4,  6]
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
# 实例 2.1
a = manager.arange(10);
b = a.get("5");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
5

# 实例 2.2
a = manager.arange(10);
b = a.get("2:");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[ 2,  3,  4,  5,  6,  7,  8,  9]

# 实例 2.3
a = manager.arange(10);
b = a.get("2:5");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[ 2,  3,  4]
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
a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("第2列元素：");
b = a.get("...,1");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("第2行元素：");
b = a.get("1,...");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("第2列及剩下的所有元素：");
b = a.get("...,1:");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
第2列元素：
ND: (3) cpu() int32
[ 2,  4,  5]

第2行元素：
ND: (3) cpu() int32
[ 3,  4,  5]

第2列及剩下的所有元素：
ND: (3, 2) cpu() int32
[[ 2,  3],
 [ 4,  5],
 [ 5,  6],
]

或者：
b = a.get(":,1");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("第2行元素：");
b = a.get("1,:");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("第2列及剩下的所有元素：");
b = a.get(":,1:");
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
第2列元素：
ND: (3) cpu() int32
[ 2,  4,  5]

第2行元素：
ND: (3) cpu() int32
[ 3,  4,  5]

第2列及剩下的所有元素：
ND: (3, 2) cpu() int32
[[ 2,  3],
 [ 4,  5],
 [ 5,  6],
]

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
NDArray x = manager.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
System.out.println("我们的数组是：");
System.out.println(x.toDebugString(100, 10, 100, 100));
System.out.println("大于 5 的元素是：");
NDArray y = x.get(x.gt(5));
System.out.println(y.toDebugString(100, 10, 100, 100));

# 输出结果如下：
我们的数组是：
ND: (4, 3) cpu() int32
[[ 0,  1,  2],
 [ 3,  4,  5],
 [ 6,  7,  8],
 [ 9, 10, 11],
]

大于 5 的元素是：
ND: (6) cpu() int32
[ 6,  7,  8,  9, 10, 11]
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No3IndexExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No3IndexExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
