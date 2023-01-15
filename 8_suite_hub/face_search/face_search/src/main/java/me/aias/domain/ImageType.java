package me.aias.domain;

import lombok.Getter;

@Getter
public enum ImageType {
    FILE_PNG("png", "PNG"),
    FILE_JPG("jpg", "JPG"),
    FILE_JPEG("jpeg", "JPEG");
    public String key;
    public String desc;

    ImageType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
