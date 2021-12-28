package me.aias.common.rdkit;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * RDKit 初始化
 *
 * @author Calvin
 * @date 2021-12-19
 */
public final class RDKitInstance {
    static {
        try {
            Path path = Paths.get("lib/native/macosx.x86_64/libGraphMolWrap.jnilib");
            System.load(path.toAbsolutePath().toString());
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
//        System.loadLibrary("GraphMolWrap");
    }
}