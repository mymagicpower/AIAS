package me.aias.example;

import me.aias.example.utils.SvgUtils;
import org.RDKit.ExplicitBitVect;
import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Calvin <179209347@qq.com>
 */
public class SimpleSmilesExample {
    private static final Logger logger = LoggerFactory.getLogger(SimpleSmilesExample.class);

    static {
        try {
            //For mac
            System.load("/path/to/macosx.x86_64/libGraphMolWrap.jnilib");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
//        System.loadLibrary("GraphMolWrap");
    }

    public static void main(String[] args) {
        String smi1 = "c1ccccc1";
        String smi2 = "c1ccccn1";

        logger.info("smi1: {}", smi1);
        logger.info("smi2: {}", smi2);
        
        //读写分子
        RWMol m1 = RWMol.MolFromSmiles(smi1);
        RWMol m2 = RWMol.MolFromSmiles(smi2);
        ExplicitBitVect fp1 = RDKFuncs.RDKFingerprintMol(m1);
        ExplicitBitVect fp2 = RDKFuncs.RDKFingerprintMol(m2);

        //生成图片
        SvgUtils.convertToPng(m1.ToSVG(),"build/output/m1.png");
        SvgUtils.convertToPng(m2.ToSVG(),"build/output/m2.png");
        
        //计算分子相似性
        double dis = RDKFuncs.AllBitSimilarity(fp1, fp2);
        logger.info("AllBitSimilarity: {}", dis);

        dis = RDKFuncs.CosineSimilarity(fp1, fp2);
        logger.info("CosineSimilarity: {}", dis);

        fp1 = RDKFuncs.MACCSFingerprintMol(m1);
        fp2 = RDKFuncs.MACCSFingerprintMol(m2);
        dis = RDKFuncs.DiceSimilarity(fp1, fp2);
        //Dice距离用于度量两个集合的相似性，因为可以把字符串理解为一种集合，因此Dice距离也会用于度量字符串的相似性。
        logger.info("DiceSimilarity: {}", dis);


    }

}
