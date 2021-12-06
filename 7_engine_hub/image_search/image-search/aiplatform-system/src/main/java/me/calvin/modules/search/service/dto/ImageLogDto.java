package me.calvin.modules.search.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @description /
* @author Calvin
* @date 2021-02-17
**/
@Data
public class ImageLogDto implements Serializable {

    /** 主键ID */
    private Long logId;

    /** 文件存储ID */
    private Long storageId;

    /** 图片ZIP包名称 */
    private String zipName;

    /** 上传图片清单 */
    private String imageList;

    /** 创建时间 */
    private Timestamp createTime;

    /** 创建人 */
    private String createBy;

    /** 修改时间 */
    private Timestamp updateTime;

    /** 修改人 */
    private String updateBy;
}