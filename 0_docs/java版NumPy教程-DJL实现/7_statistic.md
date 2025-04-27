<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### NDArray 统计函数

#### 1. 最小元素，最大元素
- Python
NumPy 提供了很多统计函数，用于从数组中查找最小元素，最大元素，百分位标准差和方差等。 函数说明如下：
numpy.amin() 和 numpy.amax()
numpy.amin() 用于计算数组中的元素沿指定轴的最小值。
numpy.amax() 用于计算数组中的元素沿指定轴的最大值。
```text
import numpy as np 
 
a = np.array([[3,7,5],[8,4,3],[2,4,9]])  
print ('我们的数组是：')
print (a)
print ('\n')
print ('调用 amin() 函数：')
print (np.amin(a,1))
print ('\n')
print ('再次调用 amin() 函数：')
print (np.amin(a,0))
print ('\n')
print ('调用 amax() 函数：')
print (np.amax(a))
print ('\n')
print ('再次调用 amax() 函数：')
print (np.amax(a, axis =  0))

# 输出结果如下：
我们的数组是：
[[3 7 5]
 [8 4 3]
 [2 4 9]]

调用 amin() 函数：
[3 3 2]

再次调用 amin() 函数：
[2 4 3]

调用 amax() 函数：
9

再次调用 amax() 函数：
[8 7 9]
```

- Java
```text
NDArray a = manager.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 min() 函数：");
NDArray b = a.min(new int[]{1});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("再次调用 min() 函数：");
b = a.min(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("调用 max() 函数：");
b = a.max();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("再次调用 max() 函数：");
b = a.max(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));


# 输出结果如下：
我们的数组是：
ND: (3, 3) cpu() int32
[[ 3,  7,  5],
 [ 8,  4,  3],
 [ 2,  4,  9],
]

调用 min() 函数：
ND: (3) cpu() int32
[ 3,  3,  2]

再次调用 min() 函数：
ND: (3) cpu() int32
[ 2,  4,  3]

调用 max() 函数：
ND: () cpu() int32
9

再次调用 max() 函数：
ND: (3) cpu() int32
[ 8,  7,  9]
```

#### 2. 算术平均值
- Python
numpy.mean() 函数用于计算数组 a 中元素的算术平均值

```text
import numpy as np 
 
a = np.array([[1,2,3],[3,4,5],[4,5,6]])  
print ('我们的数组是：')
print (a)
print ('\n')
print ('调用 mean() 函数：')
print (np.mean(a))
print ('\n')
print ('沿轴 0 调用 mean() 函数：')
print (np.mean(a, axis =  0))
print ('\n')
print ('沿轴 1 调用 mean() 函数：')
print (np.mean(a, axis =  1))

# 输出结果如下：
我们的数组是：
[[1 2 3]
 [3 4 5]
 [4 5 6]]

调用 mean() 函数：
3.6666666666666665

沿轴 0 调用 mean() 函数：
[2.66666667 3.66666667 4.66666667]

沿轴 1 调用 mean() 函数：
[2. 4. 5.]
```

- Java
```text
a = manager.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 mean() 函数：");
b = a.mean();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 0 调用 mean() 函数：");
b = a.mean(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 1 调用 mean() 函数：");
b = a.mean(new int[]{1});
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
我们的数组是：
ND: (3, 3) cpu() float32
[[1., 2., 3.],
 [3., 4., 5.],
 [4., 5., 6.],
]

调用 mean() 函数：
ND: () cpu() float32
3.6667

沿轴 0 调用 mean() 函数：
ND: (3) cpu() float32
[2.6667, 3.6667, 4.6667]

沿轴 1 调用 mean() 函数：
ND: (3) cpu() float32
[2., 4., 5.]
```

#### 3. 标准差
标准差是一组数据平均值分散程度的一种度量。
标准差是方差的算术平方根。
标准差公式如下：
```text
std = sqrt(mean((x - x.mean())**2))
```
- Python
```text
import numpy as np 
 
print (np.std([1,2,3,4]))

# 输出结果如下：
1.1180339887498949
```

- Java
```text
a = manager.create(new float[]{1,2,3,4});
b = a.sub(a.mean()).pow(2).mean().sqrt();
System.out.println(b.toDebugString(100, 10, 100, 100));
        
# 输出结果如下：
1.118
```

#### 4. 方差
统计中的方差（样本方差）是每个样本值与全体样本值的平均数之差的平方值的平均数。
换句话说，标准差是方差的平方根。
方差公式如下：
```text
mean((x - x.mean())** 2)
```
- Python
```text
import numpy as np
 
print (np.var([1,2,3,4]))

# 输出结果如下：
1.25
```

- Java
```text
a = manager.create(new float[]{1,2,3,4});
b = a.sub(a.mean()).pow(2).mean();
System.out.println(b.toDebugString(100, 10, 100, 100));
        
# 输出结果如下：
1.25
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No7StatisticExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No7StatisticExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
