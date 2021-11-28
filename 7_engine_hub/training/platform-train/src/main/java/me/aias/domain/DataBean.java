package me.aias.domain;

import java.util.List;

public class DataBean {

    private String value;
    private List<Point> points;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }
}
