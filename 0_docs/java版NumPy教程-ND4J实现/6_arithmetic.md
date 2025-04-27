<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 算术函数

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
// 1. 加法、减法、乘法和除法
INDArray a = Nd4j.arange(0, 9).reshape(3, 3);
System.out.println("First array: ");
// 第一个数组：
System.out.println(a);

INDArray b = Nd4j.create(new double[]{10, 10, 10});
System.out.println("Second array: ");
// 第二个数组：
System.out.println(b);

System.out.println("Addition of two arrays: ");
// 两个数组的加法：
INDArray c = a.add(b);
System.out.println(c);

System.out.println("Subtraction of two arrays: ");
// 两个数组的减法：
c = a.sub(b);
System.out.println(c);

System.out.println("Multiplication of two arrays: ");
// 两个数组的乘法：
c = a.mul(b);
System.out.println(c);

System.out.println("Division of two arrays: ");
// 两个数组的除法：
c = a.div(b);
System.out.println(c);


# 输出结果如下：
第一个数组：
First array: 
[[         0,    1.0000,    2.0000], 
 [    3.0000,    4.0000,    5.0000], 
 [    6.0000,    7.0000,    8.0000]]

第二个数组：
Second array: 
[   10.0000,   10.0000,   10.0000]

两个数组相加：
Addition of two arrays: 
[[   10.0000,   11.0000,   12.0000], 
 [   13.0000,   14.0000,   15.0000], 
 [   16.0000,   17.0000,   18.0000]]

两个数组相减：
Subtraction of two arrays: 
[[  -10.0000,   -9.0000,   -8.0000], 
 [   -7.0000,   -6.0000,   -5.0000], 
 [   -4.0000,   -3.0000,   -2.0000]]

两个数组相乘：
Multiplication of two arrays: 
[[         0,   10.0000,   20.0000], 
 [   30.0000,   40.0000,   50.0000], 
 [   60.0000,   70.0000,   80.0000]]

两个数组相除：
Division of two arrays: 
[[         0,    0.1000,    0.2000], 
 [    0.3000,    0.4000,    0.5000], 
 [    0.6000,    0.7000,    0.8000]]
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
// 2. 幂运算函数
a = Nd4j.create(new double[]{10, 20, 30});
System.out.println("First array: ");
// 第一个数组：
System.out.println(a);

b = Nd4j.create(new double[]{3, 5, 7});
System.out.println("Second array: ");
// 第二个数组：
System.out.println(b);

System.out.println("Calculating the remainder - mod: ");
// 计算余数 - 取模：
c = a.sub(Transforms.floor(a.div(b)).mul(b));
System.out.println(c);

# 输出结果如下：
我们的数组是: 
First array: 
[   10.0000,   20.0000,   30.0000]

第二个数组：
Second array: 
[    3.0000,    5.0000,    7.0000]

计算余数 - 取模：
Calculating the remainder - mod: 
[    1.0000,         0,    2.0000]
```




<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
