package me.aias.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.aias.common.sentence.VectorizerModel;
import me.aias.common.utils.FeatureUtils;
import me.aias.service.FeatureService;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 特征提取服务
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private VectorizerModel vectorizerModel;

    public List<Float> dnaFeature(Dataset<Row> data) {
        float[] embeddings = null;
        Dataset<Row> dataset = vectorizerModel.transform(data);
        dataset = dataset.select("features");
        List<Row> list = dataset.collectAsList();
        // 获取稀疏向量
        SparseVector sv = (SparseVector) list.get(0).getAs(0);
        // 获取稠密向量
        DenseVector dv = sv.toDense();
        List<Float> feature = FeatureUtils.normalizer(dv.toArray());
        return feature;
    }
}
