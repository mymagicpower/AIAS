package me.aias.common.utils;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 读取数据生成 Row list
     *
     * @param lines
     * @return
     * @throws IOException
     */
    public static List<Row> getRawData(List<String> lines) {
        List<String> sequences = new ArrayList<>();
        List<List<String>> kmers = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        lines.stream()
                .filter(line -> (line != null && line != "" && !line.contains("sequence")))
                .forEach(
                        line -> {
                            String[] ws = line.split("\t");
                            if (ws.length == 2) {
                                kmers.add(KmersUtils.buildKmers(ws[0], 4));
                                labels.add(ws[1]);
                                sequences.add(ws[0]);
                            }
                        });

        List<Row> rawData = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            Row row = RowFactory.create(labels.get(i), sequences.get(i), kmers.get(i));
            rawData.add(row);
        }
        return rawData;
    }

    /**
     * 生成 Row list
     *
     * @param label
     * @param sequence
     * @return
     */
    public static List<Row> getRawData(String label, String sequence) {
        List<Row> rawData = new ArrayList<>();
        Row row = RowFactory.create(label, sequence, KmersUtils.buildKmers(sequence, 4));
        rawData.add(row);
        return rawData;
    }
}