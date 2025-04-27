<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 排序、条件刷选函数

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
// 1. 数组排序 - numpy.sort()
INDArray a = Nd4j.create(new int[][]{{3, 7}, {9, 1}});
System.out.println("Our array is: \n" + a);
// 我们的数组是：
System.out.println("Calling sort() function: ");
// 调用sort()函数：
INDArray b = Nd4j.sort(a, 1, true); // Sort along rows (default axis=1)
// 沿行排序（默认轴=1）
System.out.println(b);
System.out.println("Sort along columns: ");
// 沿列排序：
b = Nd4j.sort(a, 0, true);
System.out.println(b);

# 输出结果如下：
我们的数组是：
Our array is: 
[[         3,         7], 
 [         9,         1]]

调用 sort() 函数：
Calling sort() function: 
[[         3,         7], 
 [         1,         9]]

按列排序：
Sort along columns: 
[[         1,         7], 
 [         3,         9]]
```


#### 2. 返回最大和最小元素的索引 - numpy.argmax() 和 numpy.argmin()
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
// 2. 最大值和最小值元素的索引 - numpy.argmax() 和 numpy.argmin()
a = Nd4j.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
// 我们的数组是：
System.out.println("Our array is: \n" + a);
// 调用argmax()函数：
b = Nd4j.argMax(a, 1); // Argmax along rows
System.out.println("Calling argmax() function: \n" + b);
// 沿行找最大值索引
System.out.println("Index of the maximum value along rows: \n" + b);
b = Nd4j.argMax(a, 0); // Argmax along columns
// 沿列找最大值索引
System.out.println("Index of the maximum value along columns: \n" + b);
System.out.println("Calling argmin() function: ");
// 调用argmin()函数：
b = Nd4j.argMin(a, 1); // Argmin along rows
// 沿行找最小值索引
System.out.println("Index of the minimum value along rows: \n" + b);
b = Nd4j.argMin(a, 0); // Argmin along columns
// 沿列找最小值索引
System.out.println("Index of the minimum value along columns: \n" + b);     
        
# 输出结果如下：
我们的数组是：
Our array is: 
[[        30,        40,        70], 
 [        80,        20,        10], 
 [        50,        90,        60]]

调用 argmax() 函数：
Calling argmax() function: 
7

沿轴 0 的最大值索引：
Index of the maximum value along rows: 
[         2,         0,         1]

沿轴 1 的最大值索引：
Index of the maximum value along columns: 
[         1,         2,         0]

调用 argmin() 函数：
5

沿轴 0 的最大值索引：
[         0,         2,         0]

沿轴 1 的最大值索引：
[         0,         1,         1]
```

#### 3. 数组中非零元素的索引 - numpy.nonzero()
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
// 3. 数组中非零元素的索引 - numpy.nonzero()
a = Nd4j.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
// 我们的数组是：
System.out.println("Our array is: \n" + a);
System.out.println("Indexes of non-zero elements: ");
// 获取非零元素的坐标
INDArray[] indexes = Nd4j.where(a.neq(0), null, null);
INDArray pos = Nd4j.hstack(Nd4j.expandDims(indexes[0], 1), Nd4j.expandDims(indexes[1], 1));
// 提取对应的数据
int[][] posArr = pos.toIntMatrix();
double[] values = new double[(int) indexes[0].length()];
for (int i = 0; i < values.length; i++) {
    values[i] = a.getDouble(posArr[i][0], posArr[i][1]);
}
// 打印提取的非零元素
System.out.println("Non-zero elements: ");
// 非零元素：
for (double v : values) {
    System.out.print(v + " ");
}
       
# 输出结果如下：
Non-zero elements: 
30.0 40.0 20.0 10.0 50.0 60.0 
```

#### 4. 数组中满足给定条件的元素的索引 - numpy.where()
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
// 4. 数组中满足给定条件的元素的索引 - numpy.where()
a = Nd4j.arange(9).reshape(3, 3);
System.out.println("Our array is: \n" + a);
// 我们的数组是：
System.out.println("Indexes of elements greater than 3: ");
// 大于3的元素的索引：
INDArray condition = a.gt(3);
System.out.println(condition);
// 使用这些索引获取满足条件的元素：
INDArray filtered = a.mul(condition);
System.out.println(filtered);
     
# 输出结果如下：
我们的数组是：
Our array is: 
[[         0,    1.0000,    2.0000], 
 [    3.0000,    4.0000,    5.0000], 
 [    6.0000,    7.0000,    8.0000]]

大于 3 的元素的索引：
Indexes of elements greater than 3: 
[[     false,     false,     false], 
 [     false,      true,      true], 
 [      true,      true,      true]]

使用这些索引来获取满足条件的元素：
[[         0,         0,         0], 
 [         0,    4.0000,    5.0000], 
 [    6.0000,    7.0000,    8.0000]]
```



<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
