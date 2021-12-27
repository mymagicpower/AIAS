package me.aias.example.utils;

import ai.djl.util.Utils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 读取数据生成 Row list
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static List<Row> getRawData(Path path) throws IOException {
        List<List<String>> sequences = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> lines = Utils.readLines(path);
        lines.stream()
                .filter(line -> (line != null && line != "" && !line.contains("sequence")))
                .forEach(
                        line -> {
                            String[] ws = line.split("\t");
                            if (ws.length == 2) {
                                sequences.add(KmersUtils.buildKmers(ws[0], 4));
                                labels.add(ws[1]);
                            }
                        });

        List<Row> rawData = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            Row row = RowFactory.create(labels.get(i), sequences.get(i));
            rawData.add(row);
        }
        return rawData;
    }
}