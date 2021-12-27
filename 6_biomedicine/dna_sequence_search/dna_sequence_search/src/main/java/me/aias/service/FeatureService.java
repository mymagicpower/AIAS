package me.aias.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

/**
 * 特征提取服务接口
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface FeatureService {
    List<Float> dnaFeature(Dataset<Row> data);
}
