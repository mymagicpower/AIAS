package me.aias.example.utils.common;

import ai.djl.ndarray.NDArray;

public class RotatedBox {
    private NDArray box;
    private String text;

    public RotatedBox(NDArray box, String text) {
        this.box = box;
        this.text = text;
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
}
