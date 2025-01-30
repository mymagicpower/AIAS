package top.aias.training.training.models;

import lombok.Data;
/**
 * 分类预测
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 * @website www.aias.top
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