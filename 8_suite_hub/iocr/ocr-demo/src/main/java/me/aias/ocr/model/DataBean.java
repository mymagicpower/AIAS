package me.aias.ocr.model;

import lombok.Data;
import java.util.List;

@Data
public class DataBean {
    private String value;
    private List<Point> points;
}
