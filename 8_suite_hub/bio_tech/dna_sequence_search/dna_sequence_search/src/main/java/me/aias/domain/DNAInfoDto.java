package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文本对象
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Data
public class DNAInfoDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 类别
     */
    private String label;
    /**
     * dna 序列
     */
    private String sequence;
    /**
     * 特征向量
     */
    List<Float> feature;
}