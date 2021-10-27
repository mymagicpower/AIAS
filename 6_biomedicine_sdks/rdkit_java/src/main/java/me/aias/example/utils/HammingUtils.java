/**
 * 在信息理论中，两个等长字符串之间的汉明距离
 * 是两个字符串对应位置上不同字符的个数，
 * 换句话说，汉明距离就是将一个字符串替换成另外一个字符串所需要替换的字符长度。
 * 例如，1011101和1001001之间的汉明距离是2，
 * toned和roses之间的汉明距离是3.
 * 汉明权重是字符串相对于同样长度的零字符串的汉明距离，
 * 也就是说，它是字符串中非零的元素个数：对于二进制字符串来说，就是 1 的个数，
 * 所以 11101 的汉明重量是 4。
 * 下面的代码展示了在Java中如何计算汉明距离和汉明重量。
 */
package me.aias.example.utils;

public class HammingUtils {
    public static void main(String[] args) {
        String str1 = "abcdefg";
        String str2 = "aacceeg";
        HammingUtils hd = new HammingUtils();
        int distance = hd.getDistance(str1, str2);
        System.out.println("distance is " + distance);
        int weight = hd.getWeight(255);
        System.out.println("weight is " + weight);
    }

    /**
     * calculate Hamming Distance between two strings
     *
     * @param str1 the 1st string
     * @param str2 the 2nd string
     * @return Hamming Distance between str1 and str2
     * @author
     */
    public static int getDistance(String str1, String str2) {
        int distance;
        if (str1.length() != str2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    /**
     * calculate Hamming weight for binary number
     *
     * @param i the binary number
     * @return Hamming weight of the binary number
     * @author
     */
    public static int getWeight(int i) {
        int n;
        for (n = 0; i > 0; n++) {
            i &= (i - 1);
        }
        return n;
    }
}