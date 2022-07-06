package me.aias.example;

import org.RDKit.ROMol;
import org.RDKit.RDKFuncs;
import org.RDKit.SDMolSupplier;

/**
 *
 * @author Tim Dudgeon <tdudgeon@informaticsmatters.com>
 */
public class SdfReader {

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
        if (args.length == 0) {
            System.out.println("Usage: SdfReader <filename>");
            return;
        }
        String filename = args[0];

        long t0 = System.currentTimeMillis();
        SDMolSupplier sdf = new SDMolSupplier(filename, true, false);

        int count = 0;
        int errors = 0;
        long totRings = 0;
        double sum = 0;
        while (!sdf.atEnd()) {
            count++;
            ROMol mol = sdf.next();
            if (mol == null) {
                errors++;
            } else {
                //double tpsa = RDKFuncs.calcTPSA(mol);
                //double mr = RDKFuncs.calcMolMR(mol);
                //double logp = RDKFuncs.calcMolLogP(mol);
                //long val = RDKFuncs.calcNumAromaticRings(mol);
                //double val = RDKFuncs.calcLabuteASA(mol);
                ROMol mur = RDKFuncs.MurckoDecompose(mol);
                long val = mur.getAtoms().size();
                //totRings += rings;
                //System.out.println("LogP = " + logp);
                sum += val;
            }
        }

        long t1 = System.currentTimeMillis();
        System.out.println("Processed " + count + " mols in " + (t1 - t0) + "ms. " + errors + " errors");
        System.out.println("Sum: " + sum);
    }

}
