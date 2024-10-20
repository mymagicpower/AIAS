

### NDarray Array
In this section, we will learn about some basic properties of arrays.
In NumPy, each linear array is called an axis, which is a dimension. For example, a two-dimensional array is equivalent to two one-dimensional arrays, and each element in the first one-dimensional array is another one-dimensional array. Therefore, a one-dimensional array is the axis in NumPy, where the first axis is the underlying array and the second axis is the array in the underlying array. The number of axes is the dimension of the array.

#### 1. Array dimension
- Python
```text
import numpy as np 
 
a = np.arange(24)  
print (a.ndim)        # a now has only one dimension
# Now reshape it
b = a.reshape(2,4,3)  # b now has three dimensions
print (b.ndim)

# Output:
1
3
```

- Java
```text
NDArray nd = manager.arange(24); //now has only one dimension
System.out.println(nd.getShape().dimension());
// Now reshape it
nd = nd.reshape(2, 4, 3); // now has three dimensions
System.out.println(nd.getShape().dimension());

# Output：
1
3
```

#### 2. Array shape
- Python
```text
# ndarray.shape represents the dimensions of the array, returning a tuple whose length is the number of dimensions, that is, the ndim attribute. For example, a two-dimensional array has dimensions of "number of rows" and "number of columns".
import numpy as np  
a = np.array([[1,2,3],[4,5,6]])  
print (a.shape)

# Output：
(2, 3)
```

- Java
```text
nd = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
System.out.println(nd.getShape());

# Output：
(2, 3)
```

#### 3. Reshape the array
- Python
```text
import numpy as np 
 
a = np.array([[1,2,3],[4,5,6]]) 
b = a.reshape(3,2)  
print (b)

# Output：
[[1, 2] 
 [3, 4] 
 [5, 6]]
```

- Java
```text
nd = nd.reshape(3, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output：
ND: (3, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
 [ 5,  6],
]
```


#### 4. Create an array of zeros
Create an array of specified size, and fill the array elements with 0.

- Python
```text
import numpy as np 
# Set the type to integer
y = np.zeros((5,), dtype = np.int) 
print(y)

# Output：
[0 0 0 0 0]
```

- Java
```text
nd = manager.zeros(new Shape(5), DataType.INT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output：
ND: (5) cpu() int32
[ 0,  0,  0,  0,  0]
```

#### 5. Create an array of ones
Create an array of specified size, and fill the array elements with 1

- Python
```text
import numpy as np 
# Custom type
x = np.ones([2,2], dtype = int)
print(x)

# Output:
[[1 1]
 [1 1]]
```

- Java
```text
nd = manager.ones(new Shape(2, 2), DataType.INT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output:
ND: (2, 2) cpu() int32
[[ 1,  1],
 [ 1,  1],
]
```

#### 6. Create an array from a numerical range
The numpy package uses the arange function to create a numerical range and return an ndarray object. The function format is as follows:
```text
# Generate an ndarray based on the range specified by start and stop and the step set by step.
numpy.arange(start, stop, step, dtype)
```
##### 6.1 Generate an array from 0 to 5
- Python
```text
import numpy as np

x = np.arange(5)  
print (x)

# Output:
[0  1  2  3  4]
```

- Java
```text
nd = manager.arange(5);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output:
[ 0,  1,  2,  3,  4]
```

##### 6.2 Set the return type to float
- Python
```text
# Set dtype
x = np.arange(5, dtype =  float)  
print (x)


# Output:
[0.  1.  2.  3.  4.]
```
- Java
```text
nd = manager.arange(0, 5, 1, DataType.FLOAT32);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output:
[0., 1., 2., 3., 4.]
```

##### 6.3 Set the start value, end value, and step size
- Python
```text
x = np.arange(10,20,2)  
print (x)

# Output:
[10  12  14  16  18]
```
- Java
```text
nd = manager.arange(10, 20, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# Output:
[10, 12, 14, 16, 18]
```

##### 6.4 Arithmetic progression linspace
- Python
```text
# The np.linspace function in numpy is used to create a one-dimensional array consisting of an arithmetic progression. The format is as follows:
# np.linspace(start, stop, num=50, endpoint=True, retstep=False, dtype=None)
# start:    is the starting value of the sequence
# stop:     is the end value of the sequence. If endpoint is true, this value is included in the sequence
# num:      is the number of equally spaced samples to be generated. Default is 50
# endpoint: includes the stop value in the sequence if it is true, otherwise it does not. The default is True.
# retstep:  displays the step in the generated array if it is True, otherwise it does not show.
# dtype:    is the data type of the ndarray
#The following instance uses three parameters to set the starting point to 1, the end point to 10, and the number of samples to 10.
a = np.linspace(1,10,10)
print(a)

# Output:
[ 1.  2.  3.  4.  5.  6.  7.  8.  9. 10.]
```
- Java
```text
nd = manager.linspace(1,10,10);
System.out.println(nd.toDebugString(100, 10, 100, 100));
# Output:
[10, 12, 14, 16, 18]
[ 1.,  2.,  3.,  4.,  5.,  6.,  7.,  8.,  9., 10.]
```

#### 7. Array operations
Create an array of specified size, and fill the array elements with 0.

##### 7.1Transpose the array numpy.transpose
- Python
```text
import numpy as np
 a = np.arange(12).reshape(3,4)
 
print ('Original array:')
print (a )
print ('\n')
 
print ('Array after transpose:')
print (np.transpose(a))

#Output:
Original array:
[[ 0  1  2  3]
 [ 4  5  6  7]
 [ 8  9 10 11]]

Array after transpose:
[[ 0  4  8]
 [ 1  5  9]
 [ 2  6 10]
 [ 3  7 11]]

```
- Java
```text
nd = manager.arange(12).reshape(3,4);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.transpose();
System.out.println(nd.toDebugString(100, 10, 100, 100));

//Output:
Original array:
[[ 0,  1,  2,  3],
 [ 4,  5,  6,  7],
 [ 8,  9, 10, 11],
]

Array after transpose:
[[ 0,  4,  8],
 [ 1,  5,  9],
 [ 2,  6, 10],
 [ 3,  7, 11],
]
```

##### 7.2 Swapping two axes of an array numpy.swapaxes
- Python
```text
# Created a three-dimensional ndarray
a = np.arange(8).reshape(2,2,2)
 
print ('Original array:')
print (a)
print ('\n')
# Now swap axis 0 (depth direction) to axis 2 (width direction)
 
print ('Array after swapaxes:')
print (np.swapaxes(a, 2, 0))

#Output:
Original array:
[[[0 1]
  [2 3]]

 [[4 5]
  [6 7]]]

Array after swapaxes:
[[[0 4]
  [2 6]]

 [[1 5]
  [3 7]]]
```
- Java
```text
nd = manager.arange(8).reshape(2, 2, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.swapAxes(2, 0);
System.out.println(nd.toDebugString(100, 10, 100, 100));

//Output:
Original array:
[[[ 0,  1],
  [ 2,  3],
 ],
 [[ 4,  5],
  [ 6,  7],
 ],
]

Array after swapaxes:
[[[ 0,  4],
  [ 2,  6],
 ],
 [[ 1,  5],
  [ 3,  7],
 ],
]

```

##### 7.3 Broadcasting numpy.broadcast_to
- Python
```text
# The numpy.broadcast_to function broadcasts the array to a new shape.
import numpy as np
 
a = np.arange(4).reshape(1,4)
 
print ('Original array:')
print (a)
print ('\n')
 
print ('Array after broadcast_to:')
print (np.broadcast_to(a,(4,4)))

#Output:
Original array:
[[0 1 2 3]]


Array after broadcast_to:
[[0 1 2 3]
 [0 1 2 3]
 [0 1 2 3]
 [0 1 2 3]]

```
- Java
```text
nd = manager.arange(4).reshape(1, 4);
System.out.println(nd.toDebugString(100, 10, 100, 100));
nd = nd.broadcast(new Shape(4, 4));
System.out.println(nd.toDebugString(100, 10, 100, 100));

//Output:
Original array:
[[ 0,  1,  2,  3],
]

Array after broadcast:
[[ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
 [ 0,  1,  2,  3],
]

```

##### 7.4 Inserting a new axis at a specified position to expand the array shape numpy.expand_dims
- Python
```text
import numpy as np
 
x = np.array(([1,2],[3,4]))
 
print ('Array x:')
print (x)
print ('\n')
y = np.expand_dims(x, axis = 0)
 
print ('Array y:')
print (y)
print ('\n')
 
print ('Shapes of arrays x and y:')
print (x.shape, y.shape)
print ('\n')
# Inserting axis at position 1
y = np.expand_dims(x, axis = 1)
 
print ('Array y after inserting axis at position 1:')
print (y)
print ('\n')
 
print ('x.ndim and y.ndim：')
print (x.ndim,y.ndim)
print ('\n')
 
print ('x.shape and y.shape：')
print (x.shape, y.shape)

#Output:
Array x:
[[1 2]
 [3 4]]

Array y:
[[[1 2]
  [3 4]]]

Shapes of arrays x and y:
(2, 2) (1, 2, 2)

Array y after inserting axis at position 1:
[[[1 2]]
 [[3 4]]]

x.ndim and y.ndim：
2 3

x.shape and y.shape：
(2, 2) (2, 1, 2)

```
- Java
```text
NDArray x = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("Array x:");
System.out.println(x.toDebugString(100, 10, 100, 100));
// Insert axis at position 0
NDArray y = x.expandDims(0);
System.out.println("Array y:");
System.out.println(y.toDebugString(100, 10, 100, 100));
System.out.println("Shapes of Array x and y:");
System.out.println(x.getShape() + " " + y.getShape());
// Insert axis at position 1
y = x.expandDims(1);
System.out.println("Array y after inserting axis at position 1:");
System.out.println(y.toDebugString(100, 10, 100, 100));

System.out.println("x.ndim and y.ndim:");
System.out.println(x.getShape().dimension() + " " + y.getShape().dimension());

System.out.println("Shapes of Array x and y:");
System.out.println(x.getShape() + " " + y.getShape());

// Output:
Array x:
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

Array y:
ND: (1, 2, 2) cpu() int32
[[[ 1,  2],
  [ 3,  4],
 ],
]

Shapes of Array x and y:
(2, 2) (1, 2, 2)
Array y after inserting axis at position 1:
ND: (2, 1, 2) cpu() int32
[[[ 1,  2],
 ],
 [[ 3,  4],
 ],
]

x.ndim and y.ndim:
2 3
Shapes of Array x and y:
(2, 2) (2, 1, 2)
```

##### 7.5 Remove Single-dimensional Entries from the Shape of an Array Using numpy.squeeze
- Python
```text
import numpy as np
 
x = np.arange(9).reshape(1,3,3)

print ('Array x:')
print (x)
print ('\n')
y = np.squeeze(x)
 
print ('Array y:')
print (y)
print ('\n')
 
print ('Shapes of Array x and y:')
print (x.shape, y.shape)

# Output:
Array x:
[[[0 1 2]
  [3 4 5]
  [6 7 8]]]


Array y:
[[0 1 2]
 [3 4 5]
 [6 7 8]]

Shapes of Array x and y:
(1, 3, 3) (3, 3)
```
- Java
```text
NDArray a = manager.arange(9);
System.out.println("Array a:");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDArray b = a.reshape(1, 3, 3);
System.out.println("Array x:");
System.out.println(b.toDebugString(100, 10, 100, 100));

NDArray c = b.squeeze();
System.out.println("Array y:");
System.out.println(c.toDebugString(100, 10, 100, 100));

System.out.println("Shapes of Array x and y:");
System.out.println(b.getShape() + " " + c.getShape());

// Output:
Array a:
ND: (9) cpu() int32
[ 0,  1,  2,  3,  4,  5,  6,  7,  8]

Array x:
ND: (1, 3, 3) cpu() int32
[[[ 0,  1,  2],
  [ 3,  4,  5],
  [ 6,  7,  8],
 ],
]

Array y:
ND: (3, 3) cpu() int32
[[ 0,  1,  2],
 [ 3,  4,  5],
 [ 6,  7,  8],
]

Shapes of Array x and y:
(1, 3, 3) (3, 3)
```

##### 7.6 Joining Arrays Using numpy.concatenate
- Python
```text
import numpy as np

a = np.array([[1,2],[3,4]])

print ('First array:')
print (a)
print ('\n')
b = np.array([[5,6],[7,8]])

print ('Second array:')
print (b)
print ('\n')
# Both arrays have the same dimension

print ('Joining the two arrays along axis 0:')
print (np.concatenate((a,b)))
print ('\n')

print ('Joining the two arrays along axis 1:')
print (np.concatenate((a,b),axis = 1))

# Output:
First array:
[[1 2]
 [3 4]]

Second array:
[[5 6]
 [7 8]]

Joining the two arrays along axis 0:
[[1 2]
 [3 4]
 [5 6]
 [7 8]]

Joining the two arrays along axis 1:
[[1 2 5 6]
 [3 4 7 8]]

```
- Java
```text
NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("First array:");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
System.out.println("Second array:");
System.out.println(b.toDebugString(100, 10, 100, 100));

NDArray nd = NDArrays.concat(new NDList(a, b));
System.out.println("Joining the two arrays along axis 0:");
System.out.println(nd.toDebugString(100, 10, 100, 100));

nd = NDArrays.concat(new NDList(a, b), 1);
System.out.println("Joining the two arrays along axis 1:");
System.out.println(nd.toDebugString(100, 10, 100, 100));

// Output:
First array:
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

Second array:
ND: (2, 2) cpu() int32
[[ 5,  6],
 [ 7,  8],
]

Joining the two arrays along axis 0:
ND: (4, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
 [ 5,  6],
 [ 7,  8],
]

Joining the two arrays along axis 1:
ND: (2, 4) cpu() int32
[[ 1,  2,  5,  6],
 [ 3,  4,  7,  8],
]
```

##### 7.7 Stacking Arrays Horizontally or Vertically Using numpy.stack
- Python
```text
import numpy as np

a = np.array([[1,2],[3,4]])

print ('First array:')
print (a)
print ('\n')
b = np.array([[5,6],[7,8]])

print ('Second array:')
print (b)
print ('\n')

print ('Stacking the two arrays along axis 0:')
print (np.stack((a,b),0))
print ('\n')

print ('Stacking the two arrays along axis 1:')
print (np.stack((a,b),1))

# Output:
First array:
[[1 2]
 [3 4]]

Second array:
[[5 6]
 [7 8]]

Stacking the two arrays along axis 0:
[[[1 2]
  [3 4]]

 [[5 6]
  [7 8]]]

Stacking the two arrays along axis 1:
[[[1 2]
  [5 6]]

 [[3 4]
  [7 8]]]
```
- Java
```text
NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
System.out.println("First array:");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
System.out.println("Second array:");
System.out.println(b.toDebugString(100, 10, 100, 100));

NDArray nd = NDArrays.stack(new NDList(a, b));
System.out.println("Stacking the two arrays along axis 0:");
System.out.println(nd.toDebugString(100, 10, 100, 100));

nd = NDArrays.stack(new NDList(a, b), 1);
System.out.println("Stacking the two arrays along axis 1:");
System.out.println(nd.toDebugString(100, 10, 100, 100));

// Output:
First array:
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

Second array:
ND: (2, 2) cpu() int32
[[ 5,  6],
 [ 7,  8],
]

Stacking the two arrays along axis 0:
ND: (2, 2, 2) cpu() int32
[[[ 1,  2],
  [ 3,  4],
 ],
 [[ 5,  6],
  [ 7,  8],
 ],
]

Stacking the two arrays along axis 1:
ND: (2, 2, 2) cpu() int32
[[[ 1,  2],
  [ 5,  6],
 ],
 [[ 3,  4],
  [ 7,  8],
 ],
]
```

##### 7.8 Splitting Arrays into Subarrays Along a Specified Axis Using numpy.split
- Python
```text
import numpy as np

a = np.arange(9)

print ('First array:')
print (a)
print ('\n')

print ('Split the array into three equally sized subarrays:')
b = np.split(a,3)
print (b)
print ('\n')

print ('Split the array at positions indicated in 1-D array:')
b = np.split(a,[4,7])
print (b)

# Output:
First array:
[0 1 2 3 4 5 6 7 8]

Split the array into three equally sized subarrays:
[array([0, 1, 2]), array([3, 4, 5]), array([6, 7, 8])]

Split the array at positions indicated in 1-D array:
[array([0, 1, 2, 3]), array([4, 5, 6]), array([7, 8])]


```

- Java
```text
NDArray a = manager.arange(9);
System.out.println("First array:");
System.out.println(a.toDebugString(100, 10, 100, 100));

NDList list = a.split(3);
System.out.println("Split the array into three equally sized subarrays:");
System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

list = a.split(new long[]{4, 7});
System.out.println("Split the array at positions indicated in 1-D array:");
System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

// Output:
First array:
ND: (9) cpu() int32
[ 0,  1,  2,  3,  4,  5,  6,  7,  8]

Split the array into three equally sized subarrays:
ND: (3) cpu() int32
[ 0,  1,  2]

ND: (3) cpu() int32
[ 3,  4,  5]

ND: (3) cpu() int32
[ 6,  7,  8]

Split the array at positions indicated in 1-D array:
ND: (4) cpu() int32
[ 0,  1,  2,  3]

ND: (3) cpu() int32
[ 4,  5,  6]

ND: (2) cpu() int32
[ 7,  8]
```


### Code download link: 
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No2ArrayExample.java)    

