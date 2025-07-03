package me.calvin.modules.search.service.dto;

import ai.djl.modality.cv.Image;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 图片信息对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
public class ImageInfoDto implements Serializable {
    /**
     * 图片id
     */
    private Long imageId;

    /**
     * 图片uuid
     */
    private String uuid;
    /**
     * 原图片名字
     */
    private String preName;

    /**
     * 图片分组id
     */
    private Long groupId;

    /**
     * 图片相对路径
     */
    private String imgUri;

    /**
     * 图片全路径
     */
    private String fullPath;

    /**
     * 图片
     */
    private Image image;

    /**
     * 图片特征
     */
    private List<Float> feature;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private String updateBy;
}