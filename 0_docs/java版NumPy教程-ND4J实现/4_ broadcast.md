<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 广播(Broadcast)
广播(Broadcast)是 numpy 对不同形状(shape)的数组进行数值计算的方式， 对数组的算术运算通常在相应的元素上进行。
如果两个数组 a 和 b 形状相同，即满足 a.shape == b.shape，
那么 a * b 的结果就是 a 与 b 数组对应位相乘。这要求维数相同，且各维度的长度相同。


#### 1.两个数组 a 和 b 形状相同
- Python
```text
import numpy as np 
 
a = np.array([1,2,3,4]) 
b = np.array([10,20,30,40]) 
c = a * b 
print (c)

# 输出结果如下：
[ 10  40  90 160]
```

- Java
```text
// 1. 两个数组a和b具有相同的形状。
// 注意 Nd4j.create(new int[]{1, 2, 3, 4})，此时，int数组是shape，不是数据。
INDArray x = Nd4j.create(new int[]{1, 2, 3, 4}, new long[]{1, 4}, DataType.INT32);
INDArray y = Nd4j.create(new int[]{10, 20, 30, 40}, new long[]{1, 4}, DataType.INT32);
INDArray z = x.mul(y);
System.out.println("Result of element-wise multiplication:");
// 元素级乘法的结果：
System.out.println(z);
        
# 输出结果如下：
Result of element-wise multiplication:
[[        10,        40,        90,       160]]
```

#### 2. 当运算中的 2 个数组的形状不同时，将自动触发广播机制
- Python
```text
import numpy as np 
 
a = np.array([[ 0, 0, 0],
           [10,10,10],
           [20,20,20],
           [30,30,30]])
b = np.array([1,2,3])
print(a + b)

# 输出结果如下：
[[ 1  2  3]
 [11 12 13]
 [21 22 23]
 [31 32 33]]
```

- Java
```text
// 2. 当操作中两个数组的形状不同时，会自动触发广播机制。
x = Nd4j.create(new int[][]{
        {0, 0, 0},
        {10, 10, 10},
        {20, 20, 20},
        {30, 30, 30}
});
y = Nd4j.create(new int[]{1, 2, 3}, new long[]{1, 3}, DataType.INT32);
z = x.addRowVector(y); // Use addRowVector to add a 1D array to each row.
// 使用 addRowVector 将一个1D数组加到每一行。
System.out.println("Result of broadcasting addition:");
// 广播加法的结果：
System.out.println(z);

# 输出结果如下：
Result of broadcasting addition:
[[         1,         2,         3], 
 [        11,        12,        13], 
 [        21,        22,        23], 
 [        31,        32,        33]]
```


### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No4BroadcastExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No4BroadcastExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
