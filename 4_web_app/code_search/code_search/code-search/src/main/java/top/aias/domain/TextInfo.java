package top.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文本对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 **/
@Data
public class TextInfo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 文件id
     */
    private int storageId;
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