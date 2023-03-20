
### NDarray Objects

One of the most important features of NumPy is its N-dimensional array object, ndarray, which is a collection of same type data indexed by 0-based array of elements.
The ndarray object is used to store same type elements in a multi-dimensional array.
Each element in ndarray has an area of the same storage size in memory.

NDManager is a class in DJL that helps manage the memory usage of NDArray. By creating an NDManager, memory can be cleaned up in a timely manner. When the tasks inside this block are completed, the internal NDArrays will all be cleared. This design ensures that we can make more efficient use of memory when using NDArray on a large scale.

 
```text
try (NDManager manager = NDManager.newBaseManager()) {
  ...
}
```

#### 1. Creating Data Objects - Vector
- Python
```text
import numpy as np 
a = np.array([1,2,3])  
print (a)

# The output is as follows:
[1 2 3]
```

- Java
```text
int[] vector = new int[]{1, 2, 3};
NDArray nd = manager.create(vector);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# The output is as follows:
ND: (3) cpu() int32
[ 1,  2,  3]
```

#### 2. Creating Data Objects - Matrix
- Python
```text
# More than one dimension
import numpy as np 
a = np.array([[1,  2],  [3,  4]])  
print (a)

# The output is as follows:
[[1  2] 
 [3  4]]
```

- Java
```text
# 2.1 Creating Data Objects - Matrix
int[][] mat = new int[][]{{1, 2}, {3, 4}};
NDArray nd = manager.create(vector);
nd = manager.create(mat);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# The output is as follows:
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

# 2.2 Creating Data Objects - Matrix
int[] arr = new int[]{1, 2, 3, 4};
nd = manager.create(arr).reshape(2, 2);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# The output is as follows:
ND: (2, 2) cpu() int32
[[ 1,  2],
 [ 3,  4],
]

# 2.3 Creating Data Objects - Specifying Matrix Dimensions
nd = manager.create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
System.out.println(nd.toDebugString(100, 10, 100, 100));

# The output is as follows:
ND: (1, 1, 3) cpu() float32
[[[0.485, 0.456, 0.406],
 ],
]
```

#### 3. Data Type Conversion
- Java
```text
nd = manager.create(new int[]{1, 2, 3, 4}).toType(DataType.FLOAT32, false);
System.out.println(nd.toDebugString(100, 10, 100, 100));

# The output is as follows:
ND: (4) cpu() float32
[1., 2., 3., 4.]
```

### Code download link: 
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No1BasicExample.java)    


