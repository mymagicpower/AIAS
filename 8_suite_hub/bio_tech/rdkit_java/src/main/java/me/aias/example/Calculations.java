package me.aias.example;

import org.RDKit.RWMol;
import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;

/**
 *
 * @author Tim Dudgeon <tdudgeon@informaticsmatters.com>
 */
public class Calculations {

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
        RWMol mol = RWMol.MolFromSmiles(args.length == 0 ? "CN1CCN2C(C1)C1=C(CC3=C2C=CC=C3)C=CC=C1" : args[0]);
        calculate(mol);
    }

    public static void calculate(ROMol rdkitMol) {
        double logp = RDKFuncs.calcMolLogP(rdkitMol);
        double mw = RDKFuncs.calcExactMW(rdkitMol);
        double fsp3 = RDKFuncs.calcFractionCSP3(rdkitMol);
        long hba = RDKFuncs.calcNumHBA(rdkitMol);
        long hbd = RDKFuncs.calcNumHBD(rdkitMol);
        long lhba = RDKFuncs.calcLipinskiHBA(rdkitMol);
        long lhbd = RDKFuncs.calcLipinskiHBD(rdkitMol);
        String mf = RDKFuncs.calcMolFormula(rdkitMol);
        double mr = RDKFuncs.calcMolMR(rdkitMol);
        long numHetero = RDKFuncs.calcNumHeteroatoms(rdkitMol);
        long numRings = RDKFuncs.calcNumRings(rdkitMol);
        long numAromRings = RDKFuncs.calcNumAromaticRings(rdkitMol);
        long numRotBonds = RDKFuncs.calcNumRotatableBonds(rdkitMol);
        double tpsa = RDKFuncs.calcTPSA(rdkitMol);

        StringBuilder b = new StringBuilder("Calculated ");
        b.append("\nLogP = ").append(logp)
                .append("\nMW = ").append(mw)
                .append("\nFormula = ").append(mf)
                .append("\nMolar refractivity = ").append(mr)
                .append("\nFrac C SP3 = ").append(fsp3)
                .append("\n# Acceptors = ").append(hba)
                .append("\n# Donors = ").append(hbd)
                .append("\n# Lipinski Acceptors = ").append(lhba)
                .append("\n# Lipinski Donors = ").append(lhbd)
                .append("\n# heteratoms = ").append(numHetero)
                .append("\n# rings = ").append(numRings)
                .append("\n# aromatic rings = ").append(numAromRings)
                .append("\n# rot bonds = ").append(numRotBonds)
                .append("\nTPSA = ").append(tpsa);

        System.out.println(b.toString());
    }

}
