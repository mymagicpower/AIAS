package me.calvin.modules.search.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图像日志信息对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
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