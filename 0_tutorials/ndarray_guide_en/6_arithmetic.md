
### NDArray 算术函数

#### 1. 加减乘除
算术函数包含简单的加减乘除: add()，subtract()，multiply() 和 divide()。
需要注意的是数组必须具有相同的形状或符合数组广播规则。

- Python
```text
import numpy as np 
 
a = np.arange(9, dtype = np.float_).reshape(3,3)  
print ('第一个数组：')
print (a)
print ('\n')
print ('第二个数组：')
b = np.array([10,10,10])  
print (b)
print ('\n')
print ('两个数组相加：')
print (np.add(a,b))
print ('\n')
print ('两个数组相减：')
print (np.subtract(a,b))
print ('\n')
print ('两个数组相乘：')
print (np.multiply(a,b))
print ('\n')
print ('两个数组相除：')
print (np.divide(a,b))

# 输出结果如下：
第一个数组：
[[0. 1. 2.]
 [3. 4. 5.]
 [6. 7. 8.]]

第二个数组：
[10 10 10]

两个数组相加：
[[10. 11. 12.]
 [13. 14. 15.]
 [16. 17. 18.]]

两个数组相减：
[[-10.  -9.  -8.]
 [ -7.  -6.  -5.]
 [ -4.  -3.  -2.]]

两个数组相乘：
[[ 0. 10. 20.]
 [30. 40. 50.]
 [60. 70. 80.]]

两个数组相除：
[[0.  0.1 0.2]
 [0.3 0.4 0.5]
 [0.6 0.7 0.8]]
```

- Java
```text
NDArray a = manager.arange(0, 9, 1, DataType.FLOAT32).reshape(3, 3);
System.out.println("第一个数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("第二个数组：");
NDArray b = manager.create(new int[]{10, 10, 10});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("两个数组相加：");
NDArray c = a.add(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("两个数组相减：");
c = a.sub(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("两个数组相乘：");
c = a.mul(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("两个数组相除：");
c = a.div(b);
System.out.println(c.toDebugString(100, 10, 100, 100));


# 输出结果如下：
第一个数组：
ND: (3, 3) cpu() float32
[[0., 1., 2.],
 [3., 4., 5.],
 [6., 7., 8.],
]

第二个数组：
ND: (3) cpu() int32
[10, 10, 10]

两个数组相加：
ND: (3, 3) cpu() float32
[[10., 11., 12.],
 [13., 14., 15.],
 [16., 17., 18.],
]

两个数组相减：
ND: (3, 3) cpu() float32
[[-10.,  -9.,  -8.],
 [ -7.,  -6.,  -5.],
 [ -4.,  -3.,  -2.],
]

两个数组相乘：
ND: (3, 3) cpu() float32
[[ 0., 10., 20.],
 [30., 40., 50.],
 [60., 70., 80.],
]

两个数组相除：
ND: (3, 3) cpu() float32
[[0. , 0.1, 0.2],
 [0.3, 0.4, 0.5],
 [0.6, 0.7, 0.8],
]
```

#### 2. 幂函数
numpy.power() 函数将第一个输入数组中的元素作为底数，计算它与第二个输入数组中相应元素的幂。

- Python
```text
import numpy as np 
 
a = np.array([10,100,1000])  
print ('我们的数组是；')
print (a)
print ('\n') 
print ('调用 power 函数：')
print (np.power(a,2))
print ('\n')
print ('第二个数组：')
b = np.array([1,2,3])  
print (b)
print ('\n')
print ('再次调用 power 函数：')
print (np.power(a,b))

# 输出结果如下：
我们的数组是；
[  10  100 1000]

调用 power 函数：
[    100   10000 1000000]

第二个数组：
[1 2 3]

再次调用 power 函数：
[        10      10000 1000000000]
```

- Java
```text
a = manager.create(new int[]{10,100,1000});
System.out.println("我们的数组是: ");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("调用 power 函数：");
b = a.pow(2);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("第二个数组：");
b = manager.create(new int[]{1,2,3});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("再次调用 power 函数：");
c = a.pow(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

# 输出结果如下：
我们的数组是: 
ND: (3) cpu() int32
[  10,  100, 1000]

调用 power 函数：
ND: (3) cpu() int32
[    100,   10000, 1000000]

第二个数组：
ND: (3) cpu() int32
[ 1,  2,  3]

再次调用 power 函数：
ND: (3) cpu() int32
[ 1.00000000e+01,  1.00000000e+04,  1.00000000e+09]
```

#### 3. 余数
numpy.mod() 计算输入数组中相应元素的相除后的余数。 函数 numpy.remainder() 也产生相同的结果。

- Python
```text
import numpy as np
 
a = np.array([10,20,30]) 
b = np.array([3,5,7])  
print ('第一个数组：')
print (a)
print ('\n')
print ('第二个数组：')
print (b)
print ('\n')
print ('调用 mod() 函数：')
print (np.mod(a,b))

# 输出结果如下：
第一个数组：
[10 20 30]

第二个数组：
[3 5 7]

调用 mod() 函数：
[1 0 2]
```

- Java
```text
a = manager.create(new int[]{10, 20, 30});
System.out.println("第一个数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("第二个数组：");
b = manager.create(new int[]{3, 5, 7});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("调用 mod() 函数：");
c = a.mod(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

# 输出结果如下：
第一个数组：
ND: (3) cpu() int32
[10, 20, 30]

第二个数组：
ND: (3) cpu() int32
[ 3,  5,  7]

调用 mod() 函数：
ND: (3) cpu() int32
[ 1,  0,  2]
```


### Code download link:  
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No6ArithmeticExample.java)    

