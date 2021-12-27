package me.aias.example;

import org.RDKit.ExplicitBitVect;
import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Calvin <179209347@qq.com>
 */
public class FPSExample {

    static {
        try {
            System.load("/Users/calvin/Documents/0_server_sdk/biology_sdks/rdkit_sdk/rdkit_sdk/lib/native/macosx.x86_64/libGraphMolWrap.jnilib");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
//        System.loadLibrary("GraphMolWrap");
    }

    @Test
    public void testFPS() {
        ExplicitBitVect bv;
        bv = new ExplicitBitVect(32);
        String fps;
        fps = RDKFuncs.BitVectToFPSText(bv);
        Assert.assertEquals(fps,"00000000");
        bv.setBit(0);
        bv.setBit(1);
        bv.setBit(17);
        bv.setBit(23);
        bv.setBit(31);

        fps = RDKFuncs.BitVectToFPSText(bv);
        Assert.assertEquals(fps,"03008280");
    }

    @Test
    public void testFPS2() {
        ExplicitBitVect bv;
        bv = new ExplicitBitVect(32);
        String fps="03008280";
        RDKFuncs.UpdateBitVectFromFPSText(bv,fps);
        Assert.assertEquals(bv.getNumOnBits(),5);
        Assert.assertTrue(bv.getBit(0));
        Assert.assertTrue(bv.getBit(1));
        Assert.assertTrue(bv.getBit(17));
        Assert.assertTrue(bv.getBit(23));
        Assert.assertTrue(bv.getBit(31));
    }
}
