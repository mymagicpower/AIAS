package top.aias.ocr.utils.common;
/**
 * 文本方向
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class DirectionInfo {
    private String name;
    private Double prob;

    public DirectionInfo(String name, Double prob) {
        this.name = name;
        this.prob = prob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProb() {
        return prob;
    }

    public void setProb(Double prob) {
        this.prob = prob;
    }
}
