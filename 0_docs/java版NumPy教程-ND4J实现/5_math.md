<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### INDArray 数学函数
INDArray 包含大量的各种数学运算的函数，包括三角函数，算术运算的函数，复数处理函数等。

#### 1.三角函数
NDArray 提供了标准的三角函数：sin()、cos()、tan()。
- Python
```text
import numpy as np
 
a = np.array([0,30,45,60,90])
print ('不同角度的正弦值：')
# 通过乘 pi/180 转化为弧度  
print (np.sin(a*np.pi/180))
print ('\n')
print ('数组中角度的余弦值：')
print (np.cos(a*np.pi/180))
print ('\n')
print ('数组中角度的正切值：')
print (np.tan(a*np.pi/180))

# 输出结果如下：
不同角度的正弦值：
[0.         0.5        0.70710678 0.8660254  1.        ]

数组中角度的余弦值：
[1.00000000e+00 8.66025404e-01 7.07106781e-01 5.00000000e-01
 6.12323400e-17]

数组中角度的正切值：
[0.00000000e+00 5.77350269e-01 1.00000000e+00 1.73205081e+00
 1.63312394e+16]
```

- Java
```text
// 1. 三角函数
INDArray a = Nd4j.create(new double[]{0, 30, 45, 60, 90});
System.out.println("Sine values for different angles: ");
// 不同角度的正弦值：
INDArray b = Transforms.sin(a.mul(Math.PI / 180));
System.out.println(b);

System.out.println("Cosine values for angles in the array: ");
// 数组中角度的余弦值：
b = Transforms.cos(a.mul(Math.PI / 180));
System.out.println(b);

System.out.println("Tangent values for angles in the array: ");
// 数组中角度的正切值：
b = Transforms.tan(a.mul(Math.PI / 180));
System.out.println(b);


# 输出结果如下：
不同角度的正弦值：
[         0,    0.5000,    0.7071,    0.8660,    1.0000]

数组中角度的余弦值：
Cosine values for angles in the array: 
[    1.0000,    0.8660,    0.7071,    0.5000,6.1232e-17]

数组中角度的正切值：
Tangent values for angles in the array: 
[         0,    0.5774,    1.0000,    1.7321, 1.6331e16]
```

#### 2. 反三角函数
arcsin，arccos，和 arctan 函数返回给定角度的 sin，cos 和 tan 的反三角函数。
这些函数的结果可以通过toDegrees() 函数将弧度转换为角度。

- Python
```text
import numpy as np
 
a = np.array([0,30,45,60,90])  
print ('含有正弦值的数组：')
sin = np.sin(a*np.pi/180)  
print (sin)
print ('\n')
print ('计算角度的反正弦，返回值以弧度为单位：')
inv = np.arcsin(sin)  
print (inv)
print ('\n')
print ('通过转化为角度制来检查结果：')
print (np.degrees(inv))
print ('\n')
print ('arccos 和 arctan 函数行为类似：')
cos = np.cos(a*np.pi/180)  
print (cos)
print ('\n')
print ('反余弦：')
inv = np.arccos(cos)  
print (inv)
print ('\n')
print ('角度制单位：')
print (np.degrees(inv))
print ('\n')
print ('tan 函数：')
tan = np.tan(a*np.pi/180)  
print (tan)
print ('\n')
print ('反正切：')
inv = np.arctan(tan)  
print (inv)
print ('\n')
print ('角度制单位：')
print (np.degrees(inv))

# 输出结果如下：
含有正弦值的数组：
[0.         0.5        0.70710678 0.8660254  1.        ]

计算角度的反正弦，返回值以弧度为单位：
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

通过转化为角度制来检查结果：
[ 0. 30. 45. 60. 90.]

arccos 和 arctan 函数行为类似：
[1.00000000e+00 8.66025404e-01 7.07106781e-01 5.00000000e-01
 6.12323400e-17]

反余弦：
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

角度制单位：
[ 0. 30. 45. 60. 90.]

tan 函数：
[0.00000000e+00 5.77350269e-01 1.00000000e+00 1.73205081e+00
 1.63312394e+16]

反正切：
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

角度制单位：
[ 0. 30. 45. 60. 90.]
```

- Java
```text
// 2. 反三角函数
System.out.println("Array containing sine values: ");
// 包含正弦值的数组：
INDArray sin = Transforms.sin(a.mul(Math.PI / 180));
System.out.println(sin);

System.out.println("Calculating inverse sine of angles, returns values in radians: ");
// 计算角度的反正弦，返回弧度值：
INDArray inv = Transforms.asin(sin);
System.out.println(inv);

System.out.println("Checking the result by converting to degrees: ");
// 通过转换为度来检查结果：
b = inv.mul(180 / Math.PI);
System.out.println(b);

System.out.println("arccos and arctan functions behave similarly: ");
// 反余弦和反正切函数行为相似：
INDArray cos = Transforms.cos(a.mul(Math.PI / 180));
System.out.println(cos);

System.out.println("Inverse cosine: ");
// 反余弦：
inv = Transforms.acos(cos);
System.out.println(inv);

System.out.println("Units in degrees: ");
// 单位为度：
b = inv.mul(180 / Math.PI);
System.out.println(b);

System.out.println("tan function: ");
// 正切函数：
INDArray tan = Transforms.tan(a.mul(Math.PI / 180));
System.out.println(tan);

System.out.println("Inverse tangent: ");
// 反正切：
inv = Transforms.atan(tan);
System.out.println(inv);

System.out.println("Units in degrees: ");
// 单位为度：
b = inv.mul(180 / Math.PI);
System.out.println(b);


# 输出结果如下：
含有正弦值的数组：
Array containing sine values: 
[         0,    0.5000,    0.7071,    0.8660,    1.0000]

计算角度的反正弦，返回值以弧度为单位：
Calculating inverse sine of angles, returns values in radians: 
[         0,    0.5236,    0.7854,    1.0472,    1.5708]

通过转化为角度制来检查结果：
Checking the result by converting to degrees: 
[         0,   30.0000,   45.0000,   60.0000,   90.0000]

arccos 和 arctan 函数行为类似：
arccos and arctan functions behave similarly: 
[    1.0000,    0.8660,    0.7071,    0.5000,6.1232e-17]

反余弦：
Inverse cosine: 
[         0,    0.5236,    0.7854,    1.0472,    1.5708]

角度制单位：
Units in degrees: 
[         0,   30.0000,   45.0000,   60.0000,   90.0000]

tan 函数：
tan function: 
[         0,    0.5774,    1.0000,    1.7321, 1.6331e16]

反正切：
Inverse tangent: 
[         0,    0.5236,    0.7854,    1.0472,    1.5708]

角度制单位：
Units in degrees: 
[         0,   30.0000,   45.0000,   60.0000,   90.0000]
```

#### 3. 舍入函数
#### 3.1 四舍五入
numpy.around() 函数返回指定数字的四舍五入值。
- Python
```text
import numpy as np
 
a = np.array([1.0,5.55,  123,  0.567,  25.532])  
print  ('原数组：')
print (a)
print ('\n')
print ('舍入后：')
print (np.around(a))

# 输出结果如下：
原数组：
[  1.      5.55  123.      0.567  25.532]

舍入后：
[  1.   6. 123.   1.  26.]
```

- Java
```text
// 3.1 四舍五入
a = Nd4j.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
System.out.println("Original array: ");
// 原始数组：
System.out.println(a);

System.out.println("After rounding off: ");
// 四舍五入后的数组：
b = Transforms.round(a);
System.out.println(b);

# 输出结果如下：
原数组：
Original array: 
[    1.0000,    5.5500,  123.0000,    0.5670,   25.5320]

舍入后：
After rounding off: 
[    1.0000,    6.0000,  123.0000,    1.0000,   26.0000]
```

#### 3.2 向下取整
numpy.floor() 返回小于或者等于指定表达式的最大整数，即向下取整。
- Python
```text
import numpy as np
 
a = np.array([-1.7,  1.5,  -0.2,  0.6,  10])
print ('提供的数组：')
print (a)
print ('\n')
print ('修改后的数组：')
print (np.floor(a))

# 输出结果如下：
提供的数组：
[-1.7  1.5 -0.2  0.6 10. ]

修改后的数组：
[-2.  1. -1.  0. 10.]
```

- Java
```text
// 3.2 向下取整函数
a = Nd4j.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("Array provided: ");
// 提供的数组：
System.out.println(a);

System.out.println("Modified array (floor): ");
// 修改后的数组（向下取整）：
b = Transforms.floor(a);
System.out.println(b);

# 输出结果如下：
提供的数组：
Array provided: 
[   -1.7000,    1.5000,   -0.2000,    0.6000,   10.0000]

修改后的数组：
Modified array (floor): 
[   -2.0000,    1.0000,   -1.0000,         0,   10.0000]
```

#### 3.3 向上取整
numpy.ceil() 返回大于或者等于指定表达式的最小整数，即向上取整。
- Python
```text
import numpy as np
 
a = np.array([-1.7,  1.5,  -0.2,  0.6,  10])  
print  ('提供的数组：')
print (a)
print ('\n')
print ('修改后的数组：')
print (np.ceil(a))

# 输出结果如下：
提供的数组：
[-1.7  1.5 -0.2  0.6 10. ]

修改后的数组：
[-1.  2. -0.  1. 10.]
```

- Java
```text
// 3.3 向上取整函数
a = Nd4j.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("Array provided: ");
// 提供的数组：
System.out.println(a);

System.out.println("Modified array (ceiling): ");
// 修改后的数组（向上取整）：
b = Transforms.ceil(a);
System.out.println(b);

# 输出结果如下：
提供的数组：
Array provided: 
[   -1.7000,    1.5000,   -0.2000,    0.6000,   10.0000]

修改后的数组：
Modified array (ceiling): 
[   -1.0000,    2.0000,         0,    1.0000,   10.0000]
```



<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
