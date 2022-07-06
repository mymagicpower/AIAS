package me.aias.service;

import java.nio.ByteBuffer;

/**
 * 特征提取服务接口
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface FeatureService {
    ByteBuffer molFeature(String smiles);
}
