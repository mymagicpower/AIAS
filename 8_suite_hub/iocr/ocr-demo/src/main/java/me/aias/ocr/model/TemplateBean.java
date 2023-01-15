package me.aias.ocr.model;

import lombok.Data;

import java.util.List;

@Data
public class TemplateBean {
    private String uid;
    private String name;
    private String imageName;
    List<LabelBean> labelData;
}