package me.aias.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.aias.common.utils.BytesUtils;
import me.aias.service.FeatureService;
import org.RDKit.ExplicitBitVect;
import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * 特征提取服务
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Value("${search.dimension}")
    int dimension;

    public ByteBuffer molFeature(String smiles) {
        RWMol m1 = RWMol.MolFromSmiles(smiles);
        ExplicitBitVect bv = RDKFuncs.getMorganFingerprintAsBitVect(m1,2,dimension);
        String fps = RDKFuncs.BitVectToFPSText(bv);
        byte[] bytes = BytesUtils.hexStringToBytes(fps);

        return BytesUtils.bytesToByteBuffer(bytes);
    }
}
