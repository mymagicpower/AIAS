
### NDArray Statistical Functions

#### 1. Minimum Element, Maximum Element
- Python
  NumPy provides many statistical functions for finding the minimum element, maximum element, percentiles, standard deviation, and variance from an array. The functions are described below:
  numpy.amin() and numpy.amax()
  numpy.amin() calculates the minimum value of the elements along the specified axis of an array.
  numpy.amax() calculates the maximum value of the elements along the specified axis of an array.

```text
import numpy as np

a = np.array([[3,7,5],[8,4,3],[2,4,9]])
print ('Our array is:')
print (a)
print ('\n')
print ('Applying amin() function:')
print (np.amin(a,1))
print ('\n')
print ('Applying amin() function again:')
print (np.amin(a,0))
print ('\n')
print ('Applying amax() function:')
print (np.amax(a))
print ('\n')
print ('Applying amax() function again:')
print (np.amax(a, axis =  0))
Output:
Our array is:
[[3 7 5]
[8 4 3]
[2 4 9]]

Applying amin() function:
[3 3 2]

Applying amin() function again:
[2 4 3]

Applying amax() function:
9

Applying amax() function again:
[8 7 9]
```

- Java
```text

NDArray a = manager.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying min() function:");
NDArray b = a.min(new int[]{1});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying min() function again:");
b = a.min(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying max() function:");
b = a.max();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying max() function again:");
b = a.max(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));


Output:
Our array is:
ND: (3, 3) cpu() int32
[[ 3,  7,  5],
[ 8,  4,  3],
[ 2,  4,  9],
]

Applying min() function:
ND: (3) cpu() int32
[ 3,  3,  2]

Applying min() function again:
ND: (3) cpu() int32
[ 2,  4,  3]

Applying max() function:
ND: () cpu() int32
9

Applying max() function again:
ND: (3) cpu() int32
[ 8,  7,  9]
```

#### 2. Arithmetic Mean
- Python
  numpy.mean() function calculates the arithmetic mean of the elements in array a.

```text
import numpy as np

a = np.array([[1,2,3],[3,4,5],[4,5,6]])
print ('Our array is:')
print (a)
print ('\\n')
print ('Applying mean() function:')
print (np.mean(a))
print ('\\n')
print ('Applying mean() function along axis 0:')
print (np.mean(a, axis =  0))
print ('\\n')
print ('Applying mean() function along axis 1:')
print (np.mean(a, axis =  1))

Output:
Our array is:
[[1 2 3]
[3 4 5]
[4 5 6]]

Applying mean() function:
3.6666666666666665

Applying mean() function along axis 0:
[2.66666667 3.66666667 4.66666667]

Applying mean() function along axis 1:
[2. 4. 5.]
```

- Java
```text
a = manager.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
System.out.println("Our array is:");
System.out.println(a.toDebugString(100, 10, 100, 100));
System.out.println("Applying mean() function:");
b = a.mean();
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying mean() function along axis 0:");
b = a.mean(new int[]{0});
System.out.println(b.toDebugString(100, 10, 100, 100));
System.out.println("Applying mean() function along axis 1:");
b = a.mean(new int[]{1});
System.out.println(b.toDebugString(100, 10, 100, 100));

Output:
Our array is:
ND: (3, 3) cpu() float32
[[1., 2., 3.],
[3., 4., 5.],
[4., 5., 6.],
]

Applying mean() function:
ND: () cpu() float32
3.6667

Applying mean() function along axis 0:
ND: (3) cpu() float32
[2.6667, 3.6667, 4.6667]

Applying mean() function along axis 1:
ND: (3) cpu() float32
[2., 4., 5.]
```

#### 3. Standard Deviation
Standard deviation is a measure of the degree of dispersion of a set of data from its average value.
Standard deviation is the square root of the variance.
The formula for standard deviation is as follows:

```text
std = sqrt(mean((x - x.mean())**2))
```
- Python
```text
import numpy as np 
 
print (np.std([1,2,3,4]))

Output:
1.1180339887498949

```

- Java
```text
a = manager.create(new float[]{1,2,3,4});
b = a.sub(a.mean()).pow(2).mean().sqrt();
System.out.println(b.toDebugString(100, 10, 100, 100));

Output:
1.118
```

#### 4. Variance
The variance in statistics (sample variance) is the average of the squared differences from the mean of all the values.
In other words, standard deviation is the square root of variance.
The formula for variance is as follows:

```text
mean((x - x.mean())** 2)
```
- Python
```text
import numpy as np
 
print (np.var([1,2,3,4]))

# Output:
1.25
```

- Java
```text
a = manager.create(new float[]{1,2,3,4});
b = a.sub(a.mean()).pow(2).mean();
System.out.println(b.toDebugString(100, 10, 100, 100));
        
# Output:
1.25
```

### Code download link:  
[Github](https://github.com/mymagicpower/AIAS/blob/main/0_tutorials/ndarray_lessons/src/main/java/me/aias/example/No7StatisticExample.java)    

