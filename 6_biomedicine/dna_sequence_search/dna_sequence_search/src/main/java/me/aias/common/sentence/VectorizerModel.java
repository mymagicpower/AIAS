package me.aias.common.sentence;

import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * CountVectorizer是属于常见的特征数值计算类，是一个文本特征提取方法
 *
 * @author Calvin
 * @date 2021-12-19
 */
public final class VectorizerModel {
    private CountVectorizerModel cvModel;
    private int size;

    public void init(int size) {
        this.size = size;
    }

    public void train(Dataset<Row> data) {
        //设定词汇表的最大量为768
        cvModel = new CountVectorizer().setInputCol("kmers")
                .setOutputCol("features")
                .setVocabSize(size)
                .fit(data);
    }

    public Dataset<Row> transform(Dataset<Row> data) {
        return cvModel.transform(data);
    }

    public String[] vocabulary() {
        return cvModel.vocabulary();
    }
}