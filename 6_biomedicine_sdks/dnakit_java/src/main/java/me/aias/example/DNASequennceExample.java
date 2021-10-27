package me.aias.example;

import me.aias.example.utils.DataUtils;
import me.aias.example.utils.FileUtils;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DNASequennceExample {
    public static void main(String[] args) throws IOException {
        //获取spark
        SparkSession spark = SparkSession.builder().master("local[*]").appName("CountVectorizerModel").getOrCreate();
        Path humanPath = Paths.get("src/test/resources/human_data.txt");
        Path chimpPath = Paths.get("src/test/resources/chimp_data.txt");
        Path dogPath = Paths.get("src/test/resources/dog_data.txt");

        FileUtils.downloadHumanData(humanPath);
        FileUtils.downloadChimpData(chimpPath);
        FileUtils.downloadDogData(dogPath);

        //获取数据 DataFrames
        List<Row> humanData = DataUtils.getRawData(humanPath);
        List<Row> chimpData = DataUtils.getRawData(chimpPath);
        List<Row> dogData = DataUtils.getRawData(dogPath);

        List<Row> rawData = new ArrayList<>();
        rawData.addAll(humanData);
//        rawData.addAll(chimpData);
//        rawData.addAll(dogData);

        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.StringType, false, Metadata.empty()),
                new StructField("sequence", new ArrayType(DataTypes.StringType, false), false, Metadata.empty())
        });

        Dataset<Row> data = spark.createDataFrame(rawData, schema);
        data.show(5);


        //dim 768
        //设定词汇表的最大量为768
        CountVectorizerModel cvModel = new CountVectorizer().setInputCol("sequence")
                .setOutputCol("features")
                .setVocabSize(768)
                .fit(data);
        
        String[] vocabulary = cvModel.vocabulary();
        
        cvModel.transform(data).show(5);


        spark.stop();
    }
}