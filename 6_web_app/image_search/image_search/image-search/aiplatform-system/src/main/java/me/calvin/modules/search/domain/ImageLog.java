package me.calvin.modules.search.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图片日志信息
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Entity
@Data
@Table(name = "image_log")
public class ImageLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    @ApiModelProperty(value = "图片上传ID")
    private Long logId;

    @Column(name = "storage_id", nullable = false)
    @NotNull
    @ApiModelProperty(value = "文件存储ID")
    private Long storageId;

    @Column(name = "file_name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @Column(name = "image_list", nullable = false)
    @NotNull
    @ApiModelProperty(value = "上传图片清单")
    private String imageList;

    @Column(name = "create_time", nullable = false)
    @NotNull
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "create_by", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @Column(name = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    @Column(name = "update_by")
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    public void copy(ImageLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}