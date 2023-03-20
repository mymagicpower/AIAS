
### NDArray Matrix

#### 1. Transposed Matrix
- Python
  NumPy provides various sorting methods. These sorting functions implement different sorting algorithms, each characterized by execution speed, worst-case performance, required workspace, and algorithm stability. The following table shows a comparison of three sorting algorithms.

```text
import numpy as np

a = np.arange(12).reshape(3,4)

print ('Original Array:')
print (a)
print ('\n')

print ('Transposed Array:')
print (a.T)

# Output:
Original Array:
[[ 0  1  2  3]
 [ 4  5  6  7]
 [ 8  9 10 11]]

Transposed Array:
[[ 0  4  8]
 [ 1  5  9]
 [ 2  6 10]
 [ 3  7 11]]

```

- Java
```text
NDArray a = manager.arange(12).reshape(3, 4);
System.out.println("Original Array:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Transposed Array:");
NDArray b = a.transpose();
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Original Array:
ND: (3, 4) cpu() int32
[[ 0,  1,  2,  3],
 [ 4,  5,  6,  7],
 [ 8,  9, 10, 11],
]

Transposed Array:
ND: (4, 3) cpu() int32
[[ 0,  4,  8],
 [ 1,  5,  9],
 [ 2,  6, 10],
 [ 3,  7, 11],
]

```

#### 2. Create a Matrix Filled with Zeros - zeros()
- Python
  The numpy.matlib.zeros() function creates a matrix filled with 0s.

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.zeros((2,2)))

# Output:
[[0. 0.]
 [0. 0.]]
```

- Java
```text
a = manager.zeros(new Shape(2, 2));
System.out.println(a.toDebugString(100, 10, 100, 100));

# Output:
ND: (2, 2) cpu() float32
[[0., 0.],
 [0., 0.],
]
```

#### 3. Create a Matrix Filled with Ones - ones()
- Python
  The numpy.matlib.ones() function creates a matrix filled with 1s.

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.ones((2,2)))

# Output:
[[1. 1.]
 [1. 1.]]
```

- Java
```text
a = manager.ones(new Shape(2, 2));
System.out.println(a.toDebugString(100, 10, 100, 100));  
        
# Output:
ND: (2, 2) cpu() float32
[[1., 1.],
 [1., 1.],
]
```

#### 4. Return a Matrix with Diagonal Elements as 1 and 0s elsewhere - eye()
- Python
  The numpy.matlib.eye() function returns a matrix with diagonal elements as 1 and 0s elsewhere.
  numpy.matlib.eye(n, M, k, dtype)
  Parameter Description:
  n: The number of rows of the resulting matrix.
  M: The number of columns of the resulting matrix. If None, defaults to n.
  k: Diagonal offset. Defaults to 0. See below for details.
  dtype: Data-type of the output. Defaults to float.

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.eye(n =  3, M =  4, k =  0, dtype =  float))

# Output:
[[1. 0. 0. 0.]
 [0. 1. 0. 0.]
 [0. 0. 1. 0.]]
```

- Java
```text
a = manager.eye(3,4,0, DataType.INT32);
System.out.println(a.toDebugString(100, 10, 100, 100));
       
# Output:
ND: (3, 4) cpu() int32
[[ 1,  0,  0,  0],
 [ 0,  1,  0,  0],
 [ 0,  0,  1,  0],
]
```

#### 5. Create a Matrix of Given Size, Filled with Random Values - rand()
- Python
  The numpy.matlib.rand() function creates a matrix of given size, filled with random values.
  It returns a matrix of random values from a uniform distribution over [0,1) of the given shape.

```text
import numpy.matlib 
import numpy as np 
 
print (np.matlib.rand(3,3))

# Output:
[[0.23966718 0.16147628 0.14162   ]
 [0.28379085 0.59934741 0.62985825]
 [0.99527238 0.11137883 0.41105367]]
```

- Java
```text
a = manager.randomUniform(0,1,new Shape(3,3));
System.out.println(a.toDebugString(100, 10, 100, 100));
     
# Output:
ND: (3, 3) cpu() float32
[[0.356 , 0.9904, 0.1063],
 [0.8469, 0.5733, 0.1028],
 [0.7271, 0.0218, 0.8271],
]
```

#### 6.  Inner Product - dot()
- Python
  For 1-D arrays, it returns the dot product of the two arrays. For 2-D arrays, it returns the matrix multiplication of the two arrays. For n-D arrays, it is a sum product over the last axis of a and the second-to-last of b.

```text
import numpy.matlib
import numpy as np
 
a = np.array([[1,2],[3,4]])
b = np.array([[11,12],[13,14]])
print(np.dot(a,b))

# Calculation：
# [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]

# Output：
[[37  40] 
 [85  92]]
```

- Java
```text
NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
NDArray b = manager.create(new int[][]{{11, 12}, {13, 14}});
NDArray c = a.dot(b);
// Calculation：
// [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
System.out.println(c.toDebugString(100, 10, 100, 100));

# Output：
ND: (2, 2) cpu() int32
[[37, 40],
 [85, 92],
]
```

#### 7. Matrix Multiplication - matMul()
- Python
  The numpy.matmul() function returns the matrix product of two arrays. While it returns a normal product for 2-D arrays, if dimensions of any of the argument arrays is > 2, it is treated as a stack of matrices residing in the last two indexes and is broadcast accordingly.
  On the other hand, if either argument is 1-D array, it is promoted to a matrix by appending a 1 to its dimensions. After matrix multiplication, the appended 1 is removed.


```text
import numpy.matlib 
import numpy as np 
 
a = [[1,0],[0,1]] 
b = [[4,1],[2,2]] 
print (np.matmul(a,b))

# Output：
[[4  1] 
 [2  2]]
```

- Java
```text
a = manager.create(new int[][]{{1, 0}, {0, 1}});
b = manager.create(new int[][]{{4, 1}, {2, 2}});
c = a.matMul(b);
System.out.println(c.toDebugString(100, 10, 100, 100));

# Output：
ND: (2, 2) cpu() int32
[[ 4,  1],
 [ 2,  2],
]
```

### Code download link:
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No9MatrixExample.java)    
