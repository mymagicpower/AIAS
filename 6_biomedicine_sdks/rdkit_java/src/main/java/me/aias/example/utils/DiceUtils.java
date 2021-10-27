package me.aias.example.utils;

import org.RDKit.ExplicitBitVect;
import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;
import org.RDKit.RWMol;

/**
 * Dice距离用于度量两个集合的相似性，因为可以把字符串理解为一种集合，因此Dice距离也会用于度量字符串的相似性。
 */
public class DiceUtils {
    /**
     * calculate Dice Distance between two strings
     *
     * @param smi1 the 1st string
     * @param smi2 the 2nd string
     * @return Dice Distance between smi1 and smi2
     * @author
     */
    public static double getDistance(String smi1, String smi2) {
        ROMol m1 = RWMol.MolFromSmiles(smi1);
        ROMol m2 = RWMol.MolFromSmiles(smi2);
        ExplicitBitVect fp1 = RDKFuncs.MACCSFingerprintMol(m1);
        ExplicitBitVect fp2 = RDKFuncs.MACCSFingerprintMol(m2);
        return RDKFuncs.DiceSimilarity(fp1, fp2);
    }
}