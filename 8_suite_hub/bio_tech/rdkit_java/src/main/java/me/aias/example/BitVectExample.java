package me.aias.example;

import me.aias.example.utils.BytesUtils;
import org.RDKit.ExplicitBitVect;
import org.RDKit.RDKFuncs;

import java.util.Arrays;

/**
 * @author Tim Dudgeon <tdudgeon@informaticsmatters.com>
 */
public class BitVectExample {

    static {
        try {
            System.load("/Users/calvin/Documents/0_server_sdk/biology_sdks/rdkit_sdk/rdkit_sdk/lib/native/macosx.x86_64/libGraphMolWrap.jnilib");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
//        System.loadLibrary("GraphMolWrap");
    }

    public static void main(String[] args) {
        ExplicitBitVect bv;
        bv = new ExplicitBitVect(16);
        String fps;
        bv.setBit(0);
        bv.setBit(1);
        bv.setBit(2);
        bv.setBit(3);
        bv.setBit(4);
        bv.setBit(5);
        bv.setBit(6);

        fps = RDKFuncs.BitVectToFPSText(bv);
        System.out.println("Bits Num: " + bv.getNumBits());
        System.out.println("BitVectToFPSText: " + fps);
        System.out.println("Length: " + fps.length());
        byte[] bytes = BytesUtils.hexStringToBytes(fps);
//        ByteBuffer byteBuffer = BytesUtils.bytesToByteBuffer(bytes);
        System.out.println("Bytes Array: " + Arrays.toString(bytes));
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            String bitStr = BytesUtils.byteToBit(b);
            sb.append(bitStr);
        }
        System.out.println("Bits Array: " + sb.toString());
    }

}
