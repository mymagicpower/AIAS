<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### NDArray 排序、条件刷选函数

#### 1. 数组的排序 - numpy.sort()
- Python
NumPy 提供了多种排序的方法。 这些排序函数实现不同的排序算法，每个排序算法的特征在于执行速度，最坏情况性能，所需的工作空间和算法的稳定性。 下表显示了三种排序算法的比较。
```text
import numpy as np  
 
a = np.array([[3,7],[9,1]])  
print ('我们的数组是：')
print (a)
print ('\n')
print ('调用 sort() 函数：')
print (np.sort(a))
print ('\n')
print ('按列排序：')
print (np.sort(a, axis =  0))
print ('\n')

# 输出结果如下：
我们的数组是：
[[3 7]
 [9 1]]

调用 sort() 函数：
[[3 7]
 [1 9]]

按列排序：
[[3 1]
 [9 7]]
```

- Java
```text
NDArray a = manager.create(new int[][]{{3, 7}, {9, 1}});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 sort() 函数：");
NDArray b = a.sort();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("按列排序：");
b = a.sort(0);
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
我们的数组是：
ND: (2, 2) cpu() int32
[[ 3,  7],
 [ 9,  1],
]

调用 sort() 函数：
ND: (2, 2) cpu() int32
[[ 3,  7],
 [ 1,  9],
]

按列排序：
ND: (2, 2) cpu() int32
[[ 3,  1],
 [ 9,  7],
]
```

#### 2. 数组值从小到大的索引值 - numpy.argsort()
- Python
numpy.argsort() 函数返回的是数组值从小到大的索引值。

```text
import numpy as np 
 
x = np.array([3,  1,  2])  
print ('我们的数组是：')
print (x)
print ('\n')
print ('对 x 调用 argsort() 函数：')
y = np.argsort(x)  
print (y)


# 输出结果如下：
我们的数组是：
[3 1 2]

对 x 调用 argsort() 函数：
[1 2 0]
```

- Java
```text
a = manager.create(new int[]{3, 1, 2});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 argsort() 函数：");
b = a.argSort();
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
我们的数组是：
ND: (3) cpu() int32
[ 3,  1,  2]

调用 argsort() 函数：
ND: (3) cpu() int64
[ 1,  2,  0]
```

#### 3. 返回最大和最小元素的索引 - numpy.argmax() 和 numpy.argmin()
- Python
numpy.argmax() 和 numpy.argmin()函数分别沿给定轴返回最大和最小元素的索引。

```text
import numpy as np 
 
a = np.array([[30,40,70],[80,20,10],[50,90,60]])  
print  ('我们的数组是：') 
print (a) 
print ('\n') 
print ('调用 argmax() 函数：') 
print (np.argmax(a)) 
print ('\n') 
print ('展开数组：') 
print (a.flatten()) 
print ('\n') 
print ('沿轴 0 的最大值索引：') 
maxindex = np.argmax(a, axis =  0)  
print (maxindex) 
print ('\n') 
print ('沿轴 1 的最大值索引：') 
maxindex = np.argmax(a, axis =  1)  
print (maxindex) 
print ('\n') 
print ('调用 argmin() 函数：') 
minindex = np.argmin(a)  
print (minindex) 
print ('\n') 
print ('沿轴 0 的最小值索引：') 
minindex = np.argmin(a, axis =  0)  
print (minindex) 
print ('\n') 
print ('沿轴 1 的最小值索引：') 
minindex = np.argmin(a, axis =  1)  
print (minindex)

# 输出结果如下：
我们的数组是：
[[30 40 70]
 [80 20 10]
 [50 90 60]]

调用 argmax() 函数：
7

展开数组：
[30 40 70 80 20 10 50 90 60]

沿轴 0 的最大值索引：
[1 2 0]

沿轴 1 的最大值索引：
[2 0 1]

调用 argmin() 函数：
5

沿轴 0 的最小值索引：
[0 1 1]

沿轴 1 的最小值索引：
[0 2 0]
```

- Java
```text
a = manager.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 argmax() 函数：");
b = a.argMax();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("展开数组：");
b = a.flatten();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 0 的最大值索引：");
b = a.argMax(0);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 1 的最大值索引：");
b = a.argMax(1);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("调用 argmin() 函数：");
b = a.argMin();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 0 的最大值索引：");
b = a.argMin(0);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("沿轴 1 的最大值索引：");
b = a.argMin(1);
System.out.println(b.toDebugString(100, 10, 100, 100));          
        
# 输出结果如下：
我们的数组是：
ND: (3, 3) cpu() int32
[[30, 40, 70],
 [80, 20, 10],
 [50, 90, 60],
]

调用 argmax() 函数：
ND: () cpu() int64
7

展开数组：
ND: (9) cpu() int32
[30, 40, 70, 80, 20, 10, 50, 90, 60]

沿轴 0 的最大值索引：
ND: (3) cpu() int64
[ 1,  2,  0]

沿轴 1 的最大值索引：
ND: (3) cpu() int64
[ 2,  0,  1]

调用 argmin() 函数：
ND: () cpu() int64
5

沿轴 0 的最大值索引：
ND: (3) cpu() int64
[ 0,  1,  1]

沿轴 1 的最大值索引：
ND: (3) cpu() int64
[ 0,  2,  0]
```

#### 4. 数组中非零元素的索引 - numpy.nonzero()
- Python
numpy.nonzero() 函数返回输入数组中非零元素的索引。

```text
import numpy as np 
 
a = np.array([[30,40,0],[0,20,10],[50,0,60]])  
print ('我们的数组是：')
print (a)
print ('\n')
print ('调用 nonzero() 函数：')
print (np.nonzero (a))

# 输出结果如下：
我们的数组是：
[[30 40  0]
 [ 0 20 10]
 [50  0 60]]

调用 nonzero() 函数：
(array([0, 0, 1, 1, 2, 2]), array([0, 1, 1, 2, 0, 2]))
```

- Java
```text
a = manager.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 nonzero() 函数：");
b = a.nonzero();
System.out.println(b.toDebugString(100, 10, 100, 100));
       
# 输出结果如下：
调用 nonzero() 函数：
ND: (6, 2) cpu() int64
[[ 0,  0],
 [ 0,  1],
 [ 1,  1],
 [ 1,  2],
 [ 2,  0],
 [ 2,  2],
]
```

#### 5. 数组中满足给定条件的元素的索引 - numpy.where()
- Python
numpy.where() 函数返回输入数组中满足给定条件的元素的索引。

```text
import numpy as np 
 
x = np.arange(9.).reshape(3,  3)  
print ('我们的数组是：')
print (x)
print ( '大于 3 的元素的索引：')
y = np.where(x >  3)  
print (y)
print ('使用这些索引来获取满足条件的元素：')
print (x[y])

# 输出结果如下：
我们的数组是：
[[0. 1. 2.]
 [3. 4. 5.]
 [6. 7. 8.]]
大于 3 的元素的索引：
(array([1, 1, 2, 2, 2]), array([1, 2, 0, 1, 2]))
使用这些索引来获取满足条件的元素：
[4. 5. 6. 7. 8.]
```

- Java
```text
a = manager.arange(9f).reshape(3, 3);
System.out.println("我们的数组是：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("大于 3 的元素的索引：");
b = a.gt(3);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("使用这些索引来获取满足条件的元素：");
b = a.get(b);
System.out.println(b.toDebugString(100, 10, 100, 100));
     
# 输出结果如下：
我们的数组是：
ND: (3, 3) cpu() float32
[[0., 1., 2.],
 [3., 4., 5.],
 [6., 7., 8.],
]

大于 3 的元素的索引：
ND: (3, 3) cpu() boolean
[[false, false, false],
 [false,  true,  true],
 [ true,  true,  true],
]

使用这些索引来获取满足条件的元素：
ND: (5) cpu() float32
[4., 5., 6., 7., 8.]
```

### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No8SortConditionExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No8SortConditionExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
