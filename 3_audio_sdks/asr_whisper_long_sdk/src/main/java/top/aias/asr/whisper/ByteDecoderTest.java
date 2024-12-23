package top.aias.asr.whisper;

import java.nio.charset.StandardCharsets;

public class ByteDecoderTest {
    public static void main(String[] args){
        int[] data = new int[]{232, 191, 145, 229, 185, 190, 229, 185, 180, 44, 228, 184, 141, 228, 189, 134, 230, 136, 145, 231, 148, 168, 230, 155, 184, 231, 181, 166, 229, 165, 179, 229, 133, 146, 229, 163, 147, 231, 162, 142, 44, 228, 185, 159, 229, 136, 184, 232, 170, 170, 232, 166, 170, 230, 156, 139, 228, 184, 141, 232, 166, 129, 231, 181, 166, 229, 165, 179, 229, 133, 146, 229, 163, 147, 231, 162, 142, 233, 140, 162, 44, 232, 128, 140, 230, 148, 185, 233, 128, 129, 229, 163, 147, 231, 162, 142, 230, 155, 184, 227, 128, 130};
        byte[] bytes = intToByte(data);

        String text = new String(bytes, StandardCharsets.UTF_8);

        System.out.println(text);
    }

    public static byte[] intToByte(int[] intarr) {
        byte[] bytes = new byte[intarr.length];
        for (int j = 0; j < intarr.length; j++) {
            bytes[j] = (byte) intarr[j];
        }
        return bytes;
    }
}
