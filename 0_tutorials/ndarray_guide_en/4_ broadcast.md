
### NDArray 广播(Broadcast)
广播(Broadcast)是 numpy 对不同形状(shape)的数组进行数值计算的方式， 对数组的算术运算通常在相应的元素上进行。
如果两个数组 a 和 b 形状相同，即满足 a.shape == b.shape，
那么 a*b 的结果就是 a 与 b 数组对应位相乘。这要求维数相同，且各维度的长度相同。


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
NDArray x = manager.create(new int[]{1, 2, 3, 4});
NDArray y = manager.create(new int[]{10, 20, 30, 40});
NDArray z = x.mul(y);
System.out.println(z.toDebugString(100, 10, 100, 100));

# 输出结果如下：
ND: (3) cpu() int32
[ 10,  40,  90, 160]
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
x = manager.create(new int[][]{{0, 0, 0}, {10, 10, 10}, {20, 20, 20}, {30, 30, 30}});
y = manager.create(new int[]{1, 2, 3});
z = x.add(y);
System.out.println(z.toDebugString(100, 10, 100, 100));

# 输出结果如下：
[[ 1,  2,  3],
 [11, 12, 13],
 [21, 22, 23],
 [31, 32, 33],
]
```


### Code download link: 
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No4BroadcastExample.java)    
