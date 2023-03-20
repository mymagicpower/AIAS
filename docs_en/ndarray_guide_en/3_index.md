

### Ndarray Indexing and Slicing
The contents of an ndarray object can be accessed and modified by indexing or slicing, just like the slice operation in Python lists.

The ndarray array can be indexed based on 0 to n, and slicing objects can be created using the built-in slice function, setting the start, stop, and step parameters to slice a new array from the original array.

#### 1. Slicing parameters start:stop:step separated by a colon
- Python
```text
import numpy as np

a = np.arange(10)
s = slice(2,7,2)   #start at index 2, stop at index 7, step 2
print (a[s])

#Output:
[2  4  6]

#In the above example, we first create an ndarray object using the arange() function. Then, we set the start, stop, and step parameters to 2, 7, and 2 respectively.
#We can also use the colon-separated slicing parameters start:stop:step for slicing operations:
a = np.arange(10)
b = a[2:7:2]   #start at index 2, stop at index 7, step 2
print(b)

#Output:
[2  4  6]
```

- Java
```text
NDArray a = manager.arange(10);
NDArray b = a.get("2:7:2");
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
ND: (3) cpu() int32
[ 2,  4,  6]
```

#### 2. Using a colon:
The colon : is used as follows: If only one parameter is put in, like [2], it will return the single element corresponding to that index. If we use [2:], it means that all items after that index will be extracted. If we use two parameters, like [2:7], it will extract the items between those two indices (excluding the stop index).

- Python
```text
import numpy as np
#Example 2.1
a = np.arange(10)  # [0 1 2 3 4 5 6 7 8 9]
b = a[5]
print(b)

#Output:
5

#Example 2.2
a = np.arange(10)
print(a[2:])

#Output:
[2  3  4  5  6  7  8  9]

#Example 2.3
a = np.arange(10)  # [0 1 2 3 4 5 6 7 8 9]
print(a[2:5])

#Output:
[2  3  4]
```

- Java
```text
#Example 2.1
NDArray a = manager.arange(10);
NDArray b = a.get("5");
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
5

#Example 2.2
NDArray a = manager.arange(10);
NDArray b = a.get("2:");
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
[ 2,  3,  4,  5,  6,  7,  8,  9]

#Example 2.3
NDArray a = manager.arange(10);
NDArray b = a.get("2:5");
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
[ 2,  3,  4]
```

#### 3. Multi-dimensional Array Indexing
- Python
```text
import numpy as np
#Example 3.1
a = np.array([[1,2,3],[3,4,5],[4,5,6]])
print(a)
#Slicing from a specific index
print('Slicing from index a[1:]')
print(a[1:])

#Output:
[[1 2 3]
 [3 4 5]
 [4 5 6]]
Slicing from index a[1:]
[[3 4 5]
 [4 5 6]]

#Example 3.2, using the ellipsis …(or colon:)
#Slicing can also include the ellipsis … (or colon :) to make the length of the selection tuple the same as the dimension of the array.
#If the ellipsis is used in the row position, it returns an ndarray containing the elements in the row.
a = np.array([[1,2,3],[3,4,5],[4,5,6]])
print (a[...,1])   #Second column
print (a[1,...])   #Second row
print (a[...,1:])  #Second column and all elements after it

#Output:
[2 4 5]
[3 4 5]
[[2 3]
 [4 5]
 [5 6]]

```
- Java
```text
NDArray a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("The second column:");
NDArray b = a.get("...,1");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("The second row:");
b = a.get("1,...");
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("The second column and all elements after it:");
b = a.get("...,1:");
System.out.println(b.toDebugString(100, 10, 100, 100));

#Output:
The second column:
ND: (3) cpu() int32
[ 2,  4,  5]

The second row:
ND: (3) cpu() int32
[ 3,  4,  5]

The second column and all elements after it:
ND: (3, 2) cpu() int32
[[ 2,  3],
 [ 4,  5],
 [ 5,  6],
]

```

#### 4. Boolean Indexing
We can index the target array using a Boolean array. Boolean indexing obtains an array of elements that meet specific conditions by Boolean operations (such as comparison operators).
The following example obtains elements greater than 5:

- Python
```text
import numpy as np
x = np.array([[  0,  1,  2],[  3,  4,  5],[  6,  7,  8],[  9,  10,  11]])
print ('Our array is:')
print (x)
print ('\\n')
#Now we will print the elements greater than 5
print  ('The elements greater than 5 are:')
print (x[x >  5])

#Output:
Our array is:
[[ 0  1  2]
 [ 3  4  5]
 [ 6  7  8]
 [ 9 10 11]]

The elements greater than 5 are:
[ 6  7  8  9 10 11]

```

- Java
```text
NDArray x = manager.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
System.out.println("Our array is:");
System.out.println(x.toDebugString(100, 10, 100, 100));
System.out.println("The elements greater than 5 are:");
NDArray y = x.get(x.gt(5));
System.out.println(y.toDebugString(100, 10, 100, 100));

#Output:
Our array is:
ND: (4, 3) cpu() int32
[[ 0,  1,  2],
 [ 3,  4,  5],
 [ 6,  7,  8],
 [ 9, 10, 11],
]

The elements greater than 5 are:
ND: (6) cpu() int32
[ 6,  7,  8,  9, 10, 11]

```

### Code download link:
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No3IndexExample.java)    



