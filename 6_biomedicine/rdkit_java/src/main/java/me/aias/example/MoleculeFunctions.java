package me.aias.example;

import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;

/**
 *
 * @author Tim Dudgeon <tdudgeon@informaticsmatters.com>
 */
public class MoleculeFunctions {

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
        RWMol mol = RWMol.MolFromSmiles(args.length == 0 ? "Cc1ccccc1" : args[0]);
        System.out.println("Input: " + mol.MolToSmiles());
        RDKFuncs.Kekulize(mol);
        System.out.println("Kekule: " + mol.MolToSmiles());
        RDKFuncs.setAromaticity(mol);
        System.out.println("Aromatic: " + mol.MolToSmiles());
        RDKFuncs.addHs(mol);
        System.out.println("Hydrogens: " + mol.MolToSmiles());
    }
}
