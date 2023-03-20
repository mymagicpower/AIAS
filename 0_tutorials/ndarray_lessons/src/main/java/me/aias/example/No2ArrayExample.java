package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * NDarray Operations
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 *
 */
public final class No2ArrayExample {

    private No2ArrayExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Number of dimensions of the array, initially with only one dimension.
            NDArray nd = manager.arange(24);
            System.out.println(nd.getShape().dimension());
            // Resize it to have three dimensions.
            nd = nd.reshape(2, 4, 3);
            System.out.println(nd.getShape().dimension());

            // 2. Shape of the array.
            nd = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
            System.out.println(nd.getShape());

            // 3. Reshape the array.
            nd = nd.reshape(3, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 4. Create an array filled with zeros.
            nd = manager.zeros(new Shape(5), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 5. Create an array filled with ones.
            nd = manager.ones(new Shape(2, 2), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6. Create an array from a range of values.
            // 6.1 Create an array from 0 to 5.
            nd = manager.arange(5);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.2 Set the return type to float.
            nd = manager.arange(0, 5, 1, DataType.FLOAT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.3 Set the start, stop, and step values.
            nd = manager.arange(10, 20, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.4 Create an array of evenly spaced values using linspace.
            nd = manager.linspace(1, 10, 10);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7. Array operations.
            // 7.1 Create an array from 0 to 5.
            nd = manager.arange(12).reshape(3, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.transpose();
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.2 Swap the two axes of the array.
            nd = manager.arange(8).reshape(2, 2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.swapAxes(2, 0);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.3 Broadcasting - broadcast the array to a new shape.
            nd = manager.arange(4).reshape(1, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.broadcast(new Shape(4, 4));
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.4 Insert a new axis at a specified position to expand the shape of the array.
            NDArray x = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("Array x: ");
            System.out.println(x.toDebugString(100, 10, 100, 100));
            // Insert axis at position 0.
            NDArray y = x.expandDims(0);
            System.out.println("Array y:");
            System.out.println(y.toDebugString(100, 10, 100, 100));
            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());
            // Insert axis at position 1.
            y = x.expandDims(1);
            System.out.println("Array y after inserting axis at position 1:\n ");
            System.out.println(y.toDebugString(100, 10, 100, 100));

            System.out.println("x.ndim and y.ndim：");
            System.out.println(x.getShape().dimension() + " " + y.getShape().dimension());

            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.5 Remove a single-dimensional entry from the shape of a given array using squeeze.
            x = manager.arange(9).reshape(1, 3, 3);
            System.out.println("Array x：");
            System.out.println(x.toDebugString(100, 10, 100, 100));

            y = x.squeeze();
            System.out.println("Array y: ");
            System.out.println(y.toDebugString(100, 10, 100, 100));

            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.6 Concatenate arrays using concatenate.
            NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("Second array: ");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            nd = NDArrays.concat(new NDList(a, b));
            System.out.println("Concatenate the two arrays along axis 0: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            nd = NDArrays.concat(new NDList(a, b), 1);
            System.out.println("Concatenate the two arrays along axis 1: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.7 Stack arrays in sequence along a new axis using stack.
            a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("Second array: ");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            nd = NDArrays.stack(new NDList(a, b));
            System.out.println("Stack the two arrays along axis 0: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            nd = NDArrays.stack(new NDList(a, b), 1);
            System.out.println("Stack the two arrays along axis 1: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.8 Split an array into multiple sub-arrays along a specified axis using numpy.split.
            a = manager.arange(9);
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            NDList list = a.split(3);
            System.out.println("Split the array into three equally sized sub-arrays: ");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

            list = a.split(new long[]{4, 7});
            System.out.println("Split the array at the positions indicated in a one-dimensional array: ");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

        }

    }
}
