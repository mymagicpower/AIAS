
package top.aias.sd.utils;

import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;

import java.io.IOException;
import java.util.Arrays;

public class TestArray {
    private static final Engine engine = Engine.getEngine("PyTorch");
    private static final NDManager manager =
            NDManager.newBaseManager(engine.defaultDevice(), engine.getEngineName());

    private TestArray() {
    }


    public static void main(String[] args) throws IOException, TranslateException, ModelException {

        NDArray a = manager.create(new float[]{1});
        NDArray b = manager.create(new float[]{2});
        NDArray c = manager.create(new float[]{1});

        System.out.println(a.equals(b));
        System.out.println(a.equals(c));

        int[][] arr1 = new int[][]{
                {0, 0, 1, 0},
                {1, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 1}
        };

//        label1(arr1, -1);
        Pair<int[][], Integer> pair = labelImage(arr1);
        int[][] res = pair.getKey();
        System.out.println("Label num: " + pair.getValue());
        for (int i = 0; i < res.length; i++) {
            System.out.println(Arrays.toString(res[i]));
        }

    }



    public static Pair<int[][], Integer> labelImage(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        int[][] labels = new int[height][width];
        int label_numbers = 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (image[i][j] == 0) {
                    continue;
                }
                if (j > 0 && image[i][j - 1] == image[i][j]) {
                    labels[i][j] = labels[i][j - 1];
                } else if (i > 0 && j > 0 && image[i - 1][j - 1] == image[i][j]) {
                    labels[i][j] = labels[i - 1][j - 1];
                } else if (i > 0 && image[i - 1][j] == image[i][j]) {
                    labels[i][j] = labels[i - 1][j];
                } else if (i > 0 && j < width - 1 && image[i - 1][j + 1] == image[i][j]) {
                    labels[i][j] = labels[i - 1][j + 1];
                } else {
                    labels[i][j] = label_numbers;
                    label_numbers++;
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (image[i][j] == 0) {
                    labels[i][j] = 0;
                }
            }
        }

        Pair<int[][], Integer> pair = new Pair<>(labels, label_numbers);
        return pair;
    }


}