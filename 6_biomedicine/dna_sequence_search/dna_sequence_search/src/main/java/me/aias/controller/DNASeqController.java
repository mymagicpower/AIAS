package me.aias.controller;

import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.sentence.VectorizerModel;
import me.aias.common.utils.DataUtils;
import me.aias.common.utils.FeatureUtils;
import me.aias.common.utils.FileUtils;
import me.aias.domain.*;
import me.aias.service.DNAService;
import me.aias.service.LocalStorageService;
import me.aias.service.SearchService;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本管理
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "DNA数据管理")
@RequestMapping("/api/text")
public class DNASeqController {
    @Autowired
    private VectorizerModel vectorizerModel;

    @Autowired
    private DNAService textService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocalStorageService localStorageService;

    @ApiOperation(value = "提取DNA特征值")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id) {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String input = localStorage.getPath();
        File file = new File(input);
        List<String> lines = FileUtils.readFile(file);
        List<Row> rawData = DataUtils.getRawData(lines);
        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.StringType, false, Metadata.empty()),
                new StructField("sequence", DataTypes.StringType, false, Metadata.empty()),
                new StructField("kmers", new ArrayType(DataTypes.StringType, false), false, Metadata.empty())
        });

        //获取spark
        SparkSession sparkSession = SparkSession.builder().master("local[*]").appName("CountVectorizerModel").getOrCreate();
        Dataset<Row> data = sparkSession.createDataFrame(rawData, schema);
        data.show(5);
        vectorizerModel.train(data);
        //显示训练后的词表
        log.info("Vocabulary：" + Arrays.toString(vectorizerModel.vocabulary()));

        //生成特征向量
        Dataset<Row> result = vectorizerModel.transform(data);
        result.show(5);
        List<Row> rowList = result.collectAsList();

        List<DNAInfoDto> list = new ArrayList<>();
        DNAInfoDto textInfoDto;
        // 解析DNA信息
        ConcurrentHashMap<Long, DNAInfoDto> map = textService.getMap();
        long size = map.size();
        int dimension = 0;
        for (int i = 0; i < rowList.size(); i++) {
            textInfoDto = new DNAInfoDto();
            String label = rowList.get(i).getString(0);
            String sequence = rowList.get(i).getString(1);
            log.info("label: " + label);
            log.info("sequence: " + sequence);
            textInfoDto.setId(size++);
            textInfoDto.setLabel(label);
            textInfoDto.setSequence(sequence);
            // 获取稀疏向量
            SparseVector sv = (SparseVector) rowList.get(0).getAs(3);
            // 获取稠密向量
            DenseVector dv = sv.toDense();
            // 获取向量数组并归一化 L2
            List<Float> feature = FeatureUtils.normalizer(dv.toArray());
            dimension = feature.size();
            textInfoDto.setFeature(feature);
            list.add(textInfoDto);
        }

        // 将向量插入向量引擎
        try {
            R<Boolean> response = searchService.hasCollection();
            if (!response.getData()) {
                searchService.initSearchEngine(dimension);
            }
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            for (DNAInfoDto textInfo : list) {
                vectorIds.add(textInfo.getId());
                vectors.add(textInfo.getFeature());
            }
            searchService.insert(vectorIds, vectors);
            textService.addTexts(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }


        return new ResponseEntity<>(ResultBean.success(), HttpStatus.OK);
    }
}