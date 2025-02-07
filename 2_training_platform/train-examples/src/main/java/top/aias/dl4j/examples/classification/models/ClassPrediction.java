package top.aias.dl4j.examples.classification.models;

import lombok.Data;
/**
 * 分类预测
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
@Data
public class ClassPrediction {

    private String label;
    private double percentage;

    public ClassPrediction(String label, double percentage) {
        this.label = label;
        this.percentage = percentage;
    }

    public String toString() {
        return String.format("%s: %.2f ", this.label, this.percentage);
    }
}