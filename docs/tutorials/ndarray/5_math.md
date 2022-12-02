<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  


### NDArray 数学函数
NDArray包含大量的各种数学运算的函数，包括三角函数，算术运算的函数，复数处理函数等。

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
NDArray a = manager.create(new int[]{0, 30, 45, 60, 90});
System.out.println("不同角度的正弦值：");
// 通过乘 pi/180 转化为弧度
NDArray b = a.mul(Math.PI / 180).sin();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("数组中角度的余弦值：");
 b = a.mul(Math.PI / 180).cos();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("数组中角度的正切值：");
 b = a.mul(Math.PI / 180).tan();
System.out.println(b.toDebugString(100, 10, 100, 100));


# 输出结果如下：
不同角度的正弦值：
ND: (5) cpu() float64
[0.    , 0.5   , 0.7071, 0.866 , 1.    ]

数组中角度的余弦值：
ND: (5) cpu() float64
[ 1.00000000e+00,  8.66025404e-01,  7.07106781e-01,  5.00000000e-01,  6.12323400e-17]

数组中角度的正切值：
ND: (5) cpu() float64
[ 0.00000000e+00,  5.77350269e-01,  1.00000000e+00,  1.73205081e+00,  1.63312394e+16]
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
a = manager.create(new int[]{0, 30, 45, 60, 90});
System.out.println("含有正弦值的数组：");
NDArray sin = a.mul(Math.PI / 180).sin();
System.out.println(sin.toDebugString(100, 10, 100, 100));
System.out.println("计算角度的反正弦，返回值以弧度为单位：");
NDArray inv = sin.asin();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("通过转化为角度制来检查结果：");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("arccos 和 arctan 函数行为类似：");
NDArray cos = a.mul(Math.PI / 180).cos();
System.out.println(cos.toDebugString(100, 10, 100, 100));
System.out.println("反余弦：");
inv = cos.acos();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("角度制单位：");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("tan 函数：");
NDArray tan = a.mul(Math.PI / 180).tan();
System.out.println(tan.toDebugString(100, 10, 100, 100));
System.out.println("反正切：");
inv = tan.atan();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("角度制单位：");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));


# 输出结果如下：
含有正弦值的数组：
ND: (5) cpu() float64
[0.    , 0.5   , 0.7071, 0.866 , 1.    ]

计算角度的反正弦，返回值以弧度为单位：
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]

通过转化为角度制来检查结果：
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]

arccos 和 arctan 函数行为类似：
ND: (5) cpu() float64
[ 1.00000000e+00,  8.66025404e-01,  7.07106781e-01,  5.00000000e-01,  6.12323400e-17]

反余弦：
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]

角度制单位：
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]

tan 函数：
ND: (5) cpu() float64
[ 0.00000000e+00,  5.77350269e-01,  1.00000000e+00,  1.73205081e+00,  1.63312394e+16]

反正切：
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]

角度制单位：
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]
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
a = manager.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
System.out.println("原数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("舍入后：");
b = a.round();
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
原数组：
ND: (5) cpu() float64
[  1.   ,   5.55 , 123.   ,   0.567,  25.532]

舍入后：
ND: (5) cpu() float64
[  1.,   6., 123.,   1.,  26.]
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
a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("提供的数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("修改后的数组：");
b = a.floor();
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
提供的数组：
ND: (5) cpu() float64
[-1.7,  1.5, -0.2,  0.6, 10. ]

修改后的数组：
ND: (5) cpu() float64
[-2.,  1., -1.,  0., 10.]
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
a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("提供的数组：");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("修改后的数组：");
b = a.ceil();
System.out.println(b.toDebugString(100, 10, 100, 100));

# 输出结果如下：
提供的数组：
ND: (5) cpu() float64
[-1.7,  1.5, -0.2,  0.6, 10. ]

修改后的数组：
ND: (5) cpu() float64
[-1.,  2., -0.,  1., 10.]
```


### 代码下载地址：    
[Github链接](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No5MathExample.java)    

[Gitee链接](https://gitee.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No5MathExample.java)   


<div align="center">
  <a href="http://aias.top/AIAS/guides/tutorials/ndarray/index.html">点击返回目录</a>
</div>  
