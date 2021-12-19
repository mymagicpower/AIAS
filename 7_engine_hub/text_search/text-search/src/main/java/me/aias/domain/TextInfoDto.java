package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 文本对象
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Data
public class TextInfoDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String text;
    /**
     * 特征向量
     */
    List<Float> feature;
}