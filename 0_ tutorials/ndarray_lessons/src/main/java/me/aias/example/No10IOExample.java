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
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No10IOExample {

    private No10IOExample() {
    }

    public static void main(String[] args) throws IOException {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 将数组保存到以 .npy 为扩展名的文件
            NDArray a = manager.create(new int[]{1, 2, 3, 4, 5});
            NDList encoded = new NDList(a);
            encoded.encode();
            OutputStream os = Files.newOutputStream(Paths.get("src/test/resources/outfile.npy"));
            encoded.encode(os, true);

            // 2. 读取 .npy 文件
            byte[] data = readFile("outfile.npy");
            NDList decoded = NDList.decode(manager, data);
            NDArray array = decoded.get(0);
            System.out.println(array.toDebugString(100, 10, 100, 100));

            // 3. 将多个数组保存到以 npz 为扩展名的文件
            a = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
            NDArray b = manager.arange(0f, 1f, 0.1f);
            encoded = new NDList(a, b);
            encoded.encode();
            os = Files.newOutputStream(Paths.get("src/test/resources/runoob.npz"));
            encoded.encode(os, true);

            // 4. 读取 .npz 文件
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
