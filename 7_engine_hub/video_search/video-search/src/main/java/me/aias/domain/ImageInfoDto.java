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
     * uuid
     */
    private String uuid;
    /**
     * 原图片名字
     * original image name
     */
    private String preName;

    /**
     * 图片分组id
     * image group id
     */
    private String groupId;

    /**
     * 检测目标json
     * detected object json
     */
    private String detectObjs;

    /**
     * 图片相对路径
     * image relative path
     */
    private String imgUrl;

    private String fullPath;

    /**
     * 1: 本地url，0: 远程图片url
     * 1: local url, 0: remote url
     */
    private Integer type;

    /**
     * 创建时间
     * created time
     */
    private Timestamp createTime;

    /**
     * 创建人
     * created by
     */
    private String createBy;

    /**
     * 修改时间
     * updated time
     */
    private Timestamp updateTime;

    /**
     * 修改人
     * updated by
     */
    private String updateBy;

    private List<SimpleFaceObject> faceObjects;
}