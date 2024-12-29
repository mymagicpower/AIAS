
### NDArray Sorting and Conditional Filtering Functions

#### 1. Sorting NDArrays - numpy.sort()
- Python
  NumPy provides various methods for sorting. These sorting functions implement different sorting algorithms, and the characteristics of each sorting algorithm are defined by execution speed, worst-case performance, required workspace, and algorithm stability. The table below shows a comparison of three sorting algorithms.

```text
import numpy as np

a = np.array([[3,7],[9,1]])
print ('Our array is:')
print (a)
print ('\n')
print ('Applying sort() function:')
print (np.sort(a))
print ('\n')
print ('Sort along axis 0:')
print (np.sort(a, axis =  0))
print ('\n')

# Output:
Our array is:
[[3 7]
 [9 1]]

Applying sort() function:
[[3 7]
 [1 9]]

Sort along axis 0:
[[3 1]
 [9 7]]
```

- Java
```text
NDArray a = manager.create(new int[][]{{3, 7}, {9, 1}});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying sort() function:");
NDArray b = a.sort();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Sort along axis 0:");
b = a.sort(0);
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Our array is:
ND: (2, 2) cpu() int32
[[ 3,  7],
 [ 9,  1],
]

Applying sort() function:
ND: (2, 2) cpu() int32
[[ 3,  7],
 [ 1,  9],
]

Sort along axis 0:
ND: (2, 2) cpu() int32
[[ 3,  1],
 [ 9,  7],
]
```

#### 2. Indexing NDArray elements in ascending order - numpy.argsort()
- Python
  The numpy.argsort() function returns the indices that would sort an array in ascending order.

```text
import numpy as np

x = np.array([3,  1,  2])
print ('Our array is:')
print (x)
print ('\n')
print ('Applying argsort() function:')
y = np.argsort(x)
print (y)

# Output:
Our array is:
[3 1 2]

Applying argsort() function:
[1 2 0]

```

- Java
```text
a = manager.create(new int[]{3, 1, 2});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying argsort() function:");
b = a.argSort();
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Our array is:
ND: (3) cpu() int32
[ 3,  1,  2]

Applying argsort() function:
ND: (3) cpu() int64
[ 1,  2,  0]
```

#### 3. Indexing the maximum and minimum elements in an NDArray - numpy.argmax() and numpy.argmin()
- Python
  The numpy.argmax() and numpy.argmin() functions return the indices of the maximum and minimum elements, respectively, along the given axis.

```text
import numpy as np

a = np.array([[30,40,70],[80,20,10],[50,90,60]])
print  ('Our array is:')
print (a)
print ('\n')
print ('Applying argmax() function:')
print (np.argmax(a))
print ('\n')
print ('Flattened array:')
print (a.flatten())
print ('\n')
print ('Index of max element along axis 0:')
maxindex = np.argmax(a, axis =  0)
print (maxindex)
print ('\n')
print ('Index of max element along axis 1:')
maxindex = np.argmax(a, axis =  1)
print (maxindex)
print ('\n')
print ('Applying argmin() function:')
minindex = np.argmin(a)
print (minindex)
print ('\n')
print ('Index of min element along axis 0:')
minindex = np.argmin(a, axis =  0)
print (minindex)
print ('\n')
print ('Index of min element along axis 1:')
minindex = np.argmin(a, axis =  1)
print (minindex)

# Output:
Our array is:
[[30 40 70]
 [80 20 10]
 [50 90 60]]

Applying argmax() function:
7

Flattened array:
[30 40 70 80 20 10 50 90 60]

Index of max element along axis 0:
[1 2 0]

Index of max element along axis 1:
[2 0 1]

Applying argmin() function:
5

Index of min element along axis 0:
[0 1 1]

Index of min element along axis 1:
[0 2 0]

```

- Java
```text
a = manager.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying argmax() function:");
b = a.argMax();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Flattened array:");
b = a.flatten();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Index of max element along axis 0:");
b = a.argMax(0);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Index of max element along axis 1:");
b = a.argMax(1);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying argmin() function:");
b = a.argMin();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Index of min element along axis 0:");
b = a.argMin(0);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Index of min element along axis 1:");
b = a.argMin(1);
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Our array is:
ND: (3, 3) cpu() int32
[[30, 40, 70],
 [80, 20, 10],
 [50, 90, 60],
]

Applying argmax() function:
ND: () cpu() int64
7

Flattened array:
ND: (9) cpu() int32
[30, 40, 70, 80, 20, 10, 50, 90, 60]

Index of max element along axis 0:
ND: (3) cpu() int64
[ 1,  2,  0]

Index of max element along axis 1:
ND: (3) cpu() int64
[ 2,  0,  1]

Applying argmin() function:
ND: () cpu() int64
5

Index of min element along axis 0:
ND: (3) cpu() int64
[ 0,  1,  1]

Index of min element along axis 1:
ND: (3) cpu() int64
[ 0,  2,  0]

```

#### 4. Indexing the non-zero elements in an NDArray - numpy.nonzero()
- Python
  The numpy.nonzero() function returns the indices of the non-zero elements in an input array.

```text
import numpy as np

a = np.array([[30,40,0],[0,20,10],[50,0,60]])
print ('Our array is:')
print (a)
print ('\n')
print ('Applying nonzero() function:')
print (np.nonzero (a))

# Output:
Our array is:
[[30 40  0]
 [ 0 20 10]
 [50  0 60]]

Applying nonzero() function:
(array([0, 0, 1, 1, 2, 2]), array([0, 1, 1, 2, 0, 2]))

```

- Java
```text
a = manager.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying nonzero() function:");
b = a.nonzero();
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Applying nonzero() function:
ND: (6, 2) cpu() int64
[[ 0,  0],
 [ 0,  1],
 [ 1,  1],
 [ 1,  2],
 [ 2,  0],
 [ 2,  2],
]

```

#### 5. Indexing the elements in an NDArray satisfying a given condition - numpy.where()
- Python
  The numpy.where() function returns the indices of the elements in an input array that satisfy a given condition.

```text
import numpy as np

x = np.arange(9.).reshape(3,  3)
print ('Our array is:')
print (x)
print ( 'Indices of elements > 3:')
y = np.where(x >  3)
print (y)
print ('Elements satisfying the condition:')
print (x[y])

# Output:
Our array is:
[[0. 1. 2.]
 [3. 4. 5.]
 [6. 7. 8.]]
Indices of elements > 3:
(array([1, 1, 2, 2, 2]), array([1, 2, 0, 1, 2]))
Elements satisfying the condition:
[4. 5. 6. 7. 8.]

```

- Java
```text
a = manager.arange(9f).reshape(3, 3);
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Indices of elements > 3:");
b = a.gt(3);
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Elements satisfying the condition:");
b = a.get(b);
System.out.println(b.toDebugString(100, 10, 100, 100));

# Output:
Indices of elements > 3:
ND: (3, 3) cpu() boolean
[[false, false, false],
 [false,  true,  true],
 [ true,  true,  true],
]

Our array is:
ND: (3, 3) cpu() float32
[[0., 1., 2.],
 [3., 4., 5.],
 [6., 7., 8.],
]

Elements satisfying the condition:
ND: (5) cpu() float32
[4., 5., 6., 7., 8.]
```

### Code download link: 
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No8SortConditionExample.java)    


