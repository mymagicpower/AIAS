package me.calvin.modules.search.domain;

import ai.djl.modality.cv.output.DetectedObjects;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 图片信息
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Entity
@Data
@Table(name = "image_info")
public class ImageInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    @ApiModelProperty(value = "图片主键id")
    private Long imageId;

    @Column(name = "uuid", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图片uuid")
    private String uuid;

    @Column(name = "pre_name")
    @ApiModelProperty(value = "preName")
    private String preName;

    @Column(name = "group_id")
    @ApiModelProperty(value = "图片分组id")
    private Long groupId;

    @Column(name = "img_uri")
    @ApiModelProperty(value = "图片相对路径")
    private String imgUri;

    @Column(name = "full_path")
    @ApiModelProperty(value = "fullPath")
    private String fullPath;

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

    public void copy(ImageInfo source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

//@NotEmpty 用在集合类上面 Collection、Map、数组
//不能为null或者长度为0(String Collection Map的isEmpty()方法)
//
//@NotBlank
// 只用于String,不能为null且trim()之后size>0
//
//@NotNull:
// 不能为null，但可以为empty,没有Size的约束