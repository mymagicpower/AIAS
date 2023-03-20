
### NDArray Arithmetic Functions

#### 1. Addition, subtraction, multiplication and division
Arithmetic functions include simple addition, subtraction, multiplication and division: add(), subtract(), multiply() and divide(). Note that the arrays must have the same shape or comply with the array broadcast rule.

- Python
```text
import numpy as np

a = np.arange(9, dtype = np.float_).reshape(3,3)
print ('First array:')
print (a)
print ('\n')
print ('Second array:')
b = np.array([10,10,10])
print (b)
print ('\n')
print ('Add the two arrays:')
print (np.add(a,b))
print ('\n')
print ('Subtract the two arrays:')
print (np.subtract(a,b))
print ('\n')
print ('Multiply the two arrays:')
print (np.multiply(a,b))
print ('\n')
print ('Divide the two arrays:')
print (np.divide(a,b))

#Output:
First array:
[[0. 1. 2.]
 [3. 4. 5.]
 [6. 7. 8.]]

Second array:
[10 10 10]

Add the two arrays:
[[10. 11. 12.]
 [13. 14. 15.]
 [16. 17. 18.]]

Subtract the two arrays:
[[-10.  -9.  -8.]
 [ -7.  -6.  -5.]
 [ -4.  -3.  -2.]]

Multiply the two arrays:
[[ 0. 10. 20.]
 [30. 40. 50.]
 [60. 70. 80.]]

Divide the two arrays:
[[0.  0.1 0.2]
 [0.3 0.4 0.5]
 [0.6 0.7 0.8]]
```

- Java
```text
NDArray a = manager.arange(0, 9, 1, DataType.FLOAT32).reshape(3, 3);
System.out.println("First array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Second array:");
NDArray b = manager.create(new int[]{10, 10, 10});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Add the two arrays:");
NDArray c = a.add(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("Subtract the two arrays:");
c = a.sub(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("Multiply the two arrays:");
c = a.mul(b);
System.out.println(c.toDebugString(100, 10, 100, 100));
System.out.println("Divide the two arrays:");
c = a.div(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

#Output:
First array:
ND: (3, 3) cpu() float32
[[0., 1., 2.],
 [3., 4., 5.],
 [6., 7., 8.],
]

Second array:
ND: (3) cpu() int32
[10, 10, 10]

Add the two arrays:
ND: (3, 3) cpu() float32
[[10., 11., 12.],
 [13., 14., 15.],
 [16., 17., 18.],
]

Subtract the two arrays:
ND: (3, 3) cpu() float32
[[-10.,  -9.,  -8.],
 [ -7.,  -6.,  -5.],
 [ -4.,  -3.,  -2.],
]

Multiply the two arrays:
ND: (3, 3) cpu() float32
[[ 0., 10., 20.],
 [30., 40., 50.],
 [60., 70., 80.],
]

Divide the two arrays:
ND: (3, 3) cpu() float32
[[0. , 0.1, 0.2],
 [0.3, 0.4, 0.5],
 [0.6, 0.7, 0.8],
]
```

#### 2. Power function
The numpy.power() function takes the elements in the first input array as the base and calculates their power with the corresponding elements in the second input array.

- Python
```text
import numpy as np

a = np.array([10,100,1000])
print ('Our array is:')
print (a)
print ('\n')
print ('Calling power function:')
print (np.power(a,2))
print ('\n')
print ('Second array:')
b = np.array([1,2,3])
print (b)
print ('\n')
print ('Calling power function again:')
print (np.power(a,b))

#Output:
Our array is:
[  10  100 1000]

Calling power function:
[    100   10000 1000000]

Second array:
[1 2 3]

Calling power function again:
[        10      10000 1000000000]

```

- Java
```text
a = manager.create(new int[]{10,100,1000});
System.out.println("Our array is: ");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Calling power function:");
b = a.pow(2);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Second array:");
b = manager.create(new int[]{1,2,3});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Calling power function again:");
c = a.pow(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

#Output:
Our array is:
ND: (3) cpu() int32
[  10,  100, 1000]

Calling power function:
ND: (3) cpu() int32
[    100,   10000, 1000000]

Second array:
ND: (3) cpu() int32
[ 1,  2,  3]

Calling power function again:
ND: (3) cpu() int32
[ 1.00000000e+01,  1.00000000e+04,  1.00000000e+09]

```

#### 3. Remainder
numpy.mod() calculates the remainder of the input arrays after division. The numpy.remainder() function produces the same result.

- Python
```text
import numpy as np

a = np.array([10,20,30])
b = np.array([3,5,7])
print ('First array:')
print (a)
print ('\n')
print ('Second array:')
print (b)
print ('\n')
print ('Calling mod() function:')
print (np.mod(a,b))

#Output:
First array:
[10 20 30]

Second array:
[3 5 7]

Calling mod() function:
[1 0 2]

```

- Java
```text
a = manager.create(new int[]{10, 20, 30});
System.out.println("The first array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("The second array:");
b = manager.create(new int[]{3, 5, 7});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Calling the mod() function:");
c = a.mod(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

# The output is as follows:
The first array:
ND: (3) cpu() int32
[10, 20, 30]

The second array:
ND: (3) cpu() int32
[ 3,  5,  7]

Calling the mod() function:
ND: (3) cpu() int32
[ 1,  0,  2]
```


### Code download link:  
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No6ArithmeticExample.java)    

