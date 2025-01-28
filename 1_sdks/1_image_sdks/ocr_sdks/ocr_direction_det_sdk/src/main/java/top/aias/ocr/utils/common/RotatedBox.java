package top.aias.ocr.utils.common;

import ai.djl.ndarray.NDArray;

/**
 * 旋转检测框
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class RotatedBox implements Comparable<RotatedBox> {
    private NDArray box;
    private String text;
    private double prob;

    public RotatedBox(NDArray box, String text, double prob) {
        this.box = box;
        this.text = text;
        this.prob = prob;
    }

    /**
     * 将左上角 Y 坐标升序排序
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(RotatedBox o) {
        NDArray lowBox = this.getBox();
        NDArray highBox = o.getBox();
        float lowY = lowBox.toFloatArray()[1];
        float highY = highBox.toFloatArray()[1];
        return (lowY < highY) ? -1 : 1;
    }

    public NDArray getBox() {
        return box;
    }

    public void setBox(NDArray box) {
        this.box = box;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }
}
