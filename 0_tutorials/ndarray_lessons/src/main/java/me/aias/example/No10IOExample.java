package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Ndarray IO - npy/npz
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No10IOExample {

    private No10IOExample() {
    }

    public static void main(String[] args) throws IOException {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Save an array to a binary file in NumPy .npy format.
            NDArray a = manager.create(new int[]{1, 2, 3, 4, 5});
            NDList encoded = new NDList(a);
            encoded.encode();
            OutputStream os = Files.newOutputStream(Paths.get("src/test/resources/outfile.npy"));
            encoded.encode(os, true);

            // 2. Load arrays or pickled objects from .npy, .npz or pickled files.
            byte[] data = readFile("outfile.npy");
            NDList decoded = NDList.decode(manager, data);
            NDArray array = decoded.get(0);
            System.out.println(array.toDebugString(100, 10, 100, 100));

            // 3. Save several arrays into a single file in uncompressed .npz format.
            a = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
            NDArray b = manager.arange(0f, 1f, 0.1f);
            encoded = new NDList(a, b);
            encoded.encode();
            os = Files.newOutputStream(Paths.get("src/test/resources/runoob.npz"));
            encoded.encode(os, true);

            // 4. Load data from .npz file.
            data = readFile("runoob.npz");
            decoded = NDList.decode(manager, data);
            a = decoded.get(0);
            b = decoded.get(1);
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println(b.toDebugString(100, 10, 100, 100));
        }
    }

    private static byte[] readFile(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
    }
}
