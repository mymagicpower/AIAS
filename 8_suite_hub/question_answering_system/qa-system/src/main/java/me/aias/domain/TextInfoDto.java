package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 文本对象
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Data
public class TextInfoDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private String answer;
    /**
     * 特征向量
     */
    List<Float> feature;
}