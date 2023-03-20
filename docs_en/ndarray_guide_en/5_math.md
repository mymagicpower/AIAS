
### NDArray Mathematical Functions
NDArray contains a large number of mathematical functions, including trigonometric functions, arithmetic functions, complex number processing functions, etc.

#### 1. Trigonometric Functions
NDArray provides standard trigonometric functions: sin (), cos (), tan ().
- Python
```text
import numpy as np

a = np.array([0,30,45,60,90])
print ('Sine value of different angles:')
# Convert to radians by multiplying with pi/180
print (np.sin(a*np.pi/180))
print ('\n')
print ('Cosine value of different angles:')
print (np.cos(a*np.pi/180))
print ('\n')
print ('Tangent value of different angles:')
print (np.tan(a*np.pi/180))

# The output is as follows:
Sine value of different angles:
[0.         0.5        0.70710678 0.8660254  1.        ]

Cosine value of different angles:
[1.00000000e+00 8.66025404e-01 7.07106781e-01 5.00000000e-01
 6.12323400e-17]

Tangent value of different angles:
[0.00000000e+00 5.77350269e-01 1.00000000e+00 1.73205081e+00
 1.63312394e+16]

```

- Java
```text
NDArray a = manager.create(new int[]{0, 30, 45, 60, 90});
System.out.println("Sine value of different angles:");
// Convert to radians by multiplying with pi/180
NDArray b = a.mul(Math.PI / 180).sin();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Cosine value of different angles:");
 b = a.mul(Math.PI / 180).cos();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Tangent value of different angles:");
 b = a.mul(Math.PI / 180).tan();
System.out.println(b.toDebugString(100, 10, 100, 100));

# The output is as follows:
Sine value of different angles:
ND: (5) cpu() float64
[0.    , 0.5   , 0.7071, 0.866 , 1.    ]

Cosine value of different angles:
ND: (5) cpu() float64
[ 1.00000000e+00,  8.66025404e-01,  7.07106781e-01,  5.00000000e-01,  6.12323400e-17]

Tangent value of different angles:
ND: (5) cpu() float64
[ 0.00000000e+00,  5.77350269e-01,  1.00000000e+00,  1.73205081e+00,  1.63312394e+16]
```

#### 2. Inverse Trigonometric Functions
The arcsin, arccos, and arctan functions return the inverse trigonometric functions of sin, cos, and tan of a given angle.
These functions' results can be converted to degrees using the toDegrees() function.

- Python
```text
import numpy as np

a = np.array([0,30,45,60,90])
print ('Array containing sine values:')
sin = np.sin(a*np.pi/180)
print (sin)
print ('\n')
print ('Calculating inverse sine, result in radians:')
inv = np.arcsin(sin)
print (inv)
print ('\n')
print ('Result in degrees:')
print (np.degrees(inv))
print ('\n')
print ('arccos and arctan behave similarly:')
cos = np.cos(a*np.pi/180)
print (cos)
print ('\n')
print ('Inverse cosine:')
inv = np.arccos(cos)
print (inv)
print ('\n')
print ('Result in degrees:')
print (np.degrees(inv))
print ('\n')
print ('tan function:')
tan = np.tan(a*np.pi/180)
print (tan)
print ('\n')
print ('Inverse tangent:')
inv = np.arctan(tan)
print (inv)
print ('\n')
print ('Result in degrees:')
print (np.degrees(inv))

# The output is as follows:
Array containing sine values:
[0.         0.5        0.70710678 0.8660254  1.        ]

Calculating inverse sine, result in radians:
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

Result in degrees:
[ 0. 30. 45. 60. 90.]

arccos and arctan behave similarly:
[1.00000000e+00 8.66025404e-01 7.07106781e-01 5.00000000e-01
 6.12323400e-17]

Inverse cosine:
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

Result in degrees:
[ 0. 30. 45. 60. 90.]

tan function:
[0.00000000e+00 5.77350269e-01 1.00000000e+00 1.73205081e+00
 1.63312394e+16]

Inverse tangent:
[0.         0.52359878 0.78539816 1.04719755 1.57079633]

Result in degrees:
[ 0. 30. 45. 60. 90.]
```

- Java
```text

-Java
```text
a = manager.create(new int[]{0, 30, 45, 60, 90});
System.out.println("Array with sine values:");
NDArray sin = a.mul(Math.PI / 180).sin();
System.out.println(sin.toDebugString(100, 10, 100, 100));
System.out.println("Calculate the arcsine, returns the value in radians:");
NDArray inv = sin.asin();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("Check the results by converting to degrees:");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Arccos and arctan functions behave similarly:");
NDArray cos = a.mul(Math.PI / 180).cos();
System.out.println(cos.toDebugString(100, 10, 100, 100));
System.out.println("Inverse cosine:");
inv = cos.acos();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("Degree unit:");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Tan function:");
NDArray tan = a.mul(Math.PI / 180).tan();
System.out.println(tan.toDebugString(100, 10, 100, 100));
System.out.println("Inverse tangent:");
inv = tan.atan();
System.out.println(inv.toDebugString(100, 10, 100, 100));
System.out.println("Degree unit:");
b = inv.toDegrees();
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
Array with sine values:
ND: (5) cpu() float64
[0.    , 0.5   , 0.7071, 0.866 , 1.    ]
Calculate the arcsine, returns the value in radians:
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]
Check the results by converting to degrees:
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]
Arccos and arctan functions behave similarly:
ND: (5) cpu() float64
[ 1.00000000e+00,  8.66025404e-01,  7.07106781e-01,  5.00000000e-01,  6.12323400e-17]
Inverse cosine:
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]
Degree unit:
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]
Tan function:
ND: (5) cpu() float64
[ 0.00000000e+00,  5.77350269e-01,  1.00000000e+00,  1.73205081e+00,  1.63312394e+16]
Inverse tangent:
ND: (5) cpu() float64
[0.    , 0.5236, 0.7854, 1.0472, 1.5708]
Degree unit:
ND: (5) cpu() float64
[ 0., 30., 45., 60., 90.]
```

#### 3.Rounding Functions
#### 3.1 Round
numpy.around() returns the rounded value of a specified number.

- Python
```text
import numpy as np

a = np.array([1.0,5.55,  123,  0.567,  25.532])
print  ('Original array:')
print (a)
print ('\\n')
print ('Array after rounding:')
print (np.around(a))

# The output is as follows:
Original array:
[  1.      5.55  123.      0.567  25.532]

Array after rounding:
[  1.   6. 123.   1.  26.]
```

- Java
```text
a = manager.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
System.out.println("Original array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Array after rounding:");
b = a.round();
System.out.println(b.toDebugString(100, 10, 100, 100));

# The output is as follows:
Original array:
ND: (5) cpu() float64
[  1.   ,   5.55 , 123.   ,   0.567,  25.532]

Array after rounding:
ND: (5) cpu() float64
[  1.,   6., 123.,   1.,  26.]
```

#### 3.2 Floor
numpy.floor() returns the largest integer less than or equal to a specified number.

- Python
```text
import numpy as np

a = np.array([-1.7,  1.5,  -0.2,  0.6,  10])
print ('Provided array:')
print (a)
print ('\\n')
print ('Modified array:')
print (np.floor(a))

# The output is as follows:
Provided array:
[-1.7  1.5 -0.2  0.6 10. ]

Modified array:
[-2.  1. -1.  0. 10.]

```

- Java
```text
a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("Provided array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Modified array:");
b = a.floor();
System.out.println(b.toDebugString(100, 10, 100, 100));

# The output is as follows:
Provided array:
ND: (5) cpu() float64
[-1.7,  1.5, -0.2,  0.6, 10. ]

Modified array:
ND: (5) cpu() float64
[-2.,  1., -1.,  0., 10.]
```

#### 3.3 Ceil
numpy.ceil() returns the smallest integer greater than or equal to a specified number.

- Python
```text
import numpy as np

a = np.array([-1.7,  1.5,  -0.2,  0.6,  10])
print  ('Provided array:')
print (a)
print ('\\n')
print ('Modified array:')
print (np.ceil(a))

# The output is as follows:
Provided array:
[-1.7  1.5 -0.2  0.6 10. ]

Modified array:
[-1.  2. -0.  1. 10.]
```

- Java
```text
a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
System.out.println("Provided array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Modified array:");
b = a.ceil();
System.out.println(b.toDebugString(100, 10, 100, 100));

# The output is as follows:
Provided array:
ND: (5) cpu() float64
[-1.7,  1.5, -0.2,  0.6, 10. ]

Modified array:
ND: (5) cpu() float64
[-1.,  2., -0.,  1., 10.]
```


### Code download link:  
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No5MathExample.java)    


