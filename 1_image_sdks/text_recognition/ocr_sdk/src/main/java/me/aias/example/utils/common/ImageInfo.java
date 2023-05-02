package me.aias.example.utils.common;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.ndarray.NDArray;

public class ImageInfo {
    private String name;
    private Double prob;
    private Image image;
    private NDArray box;

    public ImageInfo(Image image, NDArray box) {
        this.image = image;
        this.box = box;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public NDArray getBox() {
        return box;
    }

    public void setBox(NDArray box) {
        this.box = box;
    }
}
