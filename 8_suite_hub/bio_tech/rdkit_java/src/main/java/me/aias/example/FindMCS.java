package me.aias.example;

import org.RDKit.AtomComparator;
import org.RDKit.BondComparator;
import org.RDKit.MCSResult;
import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;
import org.RDKit.ROMol_Vect;
import org.RDKit.RWMol;

/**
 *
 * @author Tim Dudgeon <tdudgeon@informaticsmatters.com>
 */
public class FindMCS {

    static {
        try {
            System.load("/Users/calvin/Documents/0_server_sdk/biology_sdks/rdkit_sdk/rdkit_sdk/lib/native/macosx.x86_64/libGraphMolWrap.jnilib");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
//        System.loadLibrary("GraphMolWrap");
    }

    static String[] exampleMols = new String[]{
        "CN1CCN2C(C1)C1=C(CC3=C2C=CC=C3)C=CC=C1",
        "CN(C)CCCC1CC2N(O1)C1=C(CC3=C2C=CC=C3)C=CC=C1",
        "CN(C)CC1CC2N(O1)C1=C(CC3=C2C=CC=C3)C=CC=C1",
        "CN(C)CC1CC2N(O1)C1=CC(Cl)=CC=C1CC1=C2C=CC=C1",
        "CN(C)CC1(C)CC2N(O1)C1=C(CC3=C2C=CC=C3)C=CC=C1"
    };

    public static ROMol_Vect createMoleculeVectorFromSmiles(String[] smiles) {
        ROMol_Vect rovect = new ROMol_Vect();
        for (String smi : smiles) {
            RWMol mol = RWMol.MolFromSmiles(smi);
            RDKFuncs.setAromaticity(mol);
            if (mol != null) {
                rovect.add(mol);
            } else {
                throw new IllegalArgumentException("Could not read molecule " + smi);
            }
        }
        return rovect;
    }

    /**
     * Determine MCS with default options
     *
     * @param mols
     * @return
     */
    public static MCSResult findMCS(ROMol_Vect mols) {
        return RDKFuncs.findMCS(mols);
    }

    /**
     * Determine MCS with all available options
     * @see http://rdkit.org/docs/api/rdkit.Chem.MCS-module.html#FindMCS for all the options
     * 
     */
    public static MCSResult findMCS(ROMol_Vect mols,
            boolean maximizeBonds,
            double threshold,
            long timeout,
            boolean verbose,
            boolean matchValences,
            boolean ringMatchesRingOnly,
            boolean completeRingsOnly,
            boolean matchChiralTag,
            AtomComparator atomComp,
            BondComparator bondComp) {
        return RDKFuncs.findMCS(mols, maximizeBonds, threshold, timeout, verbose, matchValences, ringMatchesRingOnly, completeRingsOnly, matchChiralTag, atomComp, bondComp);
    }

    public static void main(String[] args) {
        ROMol_Vect input = createMoleculeVectorFromSmiles(args.length == 0 ? exampleMols : args);
        MCSResult mcs = findMCS(input);
        System.out.println("MCS from " + input.size() + " mols is " + mcs.getSmartsString());
    }

}
