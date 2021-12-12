package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Calvin
 * @description /
 * @date 2021-02-17
 **/
@Data
public class ImageInfoDto implements Serializable {

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
    private String groupId;

    /**
     * 检测目标json
     */
    private String detectObjs;

    /**
     * 图片相对路径
     */
    private String imgUrl;

    private String fullPath;

    /**
     * 1: 本地url，0: 远程图片url
     */
    private Integer type;

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

    private List<SimpleFaceObject> faceObjects;
}