package top.aias.domain;

import lombok.Getter;
/**
 * 图片类型
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
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
