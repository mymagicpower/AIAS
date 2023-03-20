
### NDArray Broadcasting
Broadcasting is the way numpy performs arithmetic operations on arrays with different shapes. The arithmetic operation on arrays is usually performed on the corresponding elements. If two arrays a and b have the same shape, i.e., a.shape == b.shape, then the result of a*b is the element-wise multiplication of the a and b arrays. This requires the dimensions to be the same, and the length of each dimension to be the same.

#### 1. Two arrays a and b have the same shape
- Python
```text
import numpy as np 
 
a = np.array([1,2,3,4]) 
b = np.array([10,20,30,40]) 
c = a * b 
print (c)

# Output:
[ 10  40  90 160]
```

- Java
```text
NDArray x = manager.create(new int[]{1, 2, 3, 4});
NDArray y = manager.create(new int[]{10, 20, 30, 40});
NDArray z = x.mul(y);
System.out.println(z.toDebugString(100, 10, 100, 100));

# Output:
ND: (3) cpu() int32
[ 10,  40,  90, 160]
```

#### 2. Broadcasting is triggered automatically when the shapes of the two arrays in the operation are different
- Python
```text
import numpy as np 
 
a = np.array([[ 0, 0, 0],
           [10,10,10],
           [20,20,20],
           [30,30,30]])
b = np.array([1,2,3])
print(a + b)

# Output:
[[ 1  2  3]
 [11 12 13]
 [21 22 23]
 [31 32 33]]
```

- Java
```text
x = manager.create(new int[][]{{0, 0, 0}, {10, 10, 10}, {20, 20, 20}, {30, 30, 30}});
y = manager.create(new int[]{1, 2, 3});
z = x.add(y);
System.out.println(z.toDebugString(100, 10, 100, 100));

# Output:
[[ 1,  2,  3],
 [11, 12, 13],
 [21, 22, 23],
 [31, 32, 33],
]
```


### Code download link: 
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No4BroadcastExample.java)    
