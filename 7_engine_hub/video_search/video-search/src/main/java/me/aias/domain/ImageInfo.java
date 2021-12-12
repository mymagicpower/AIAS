package me.aias.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Calvin
 * @description /
 * @date 2021-02-17
 **/
@Data
public class ImageInfo implements Serializable {

    @ApiModelProperty(value = "图片主键id")
    private Long imageId;

    @ApiModelProperty(value = "图片uuid")
    private String uuid;

    @ApiModelProperty(value = "preName")
    private String preName;

    @ApiModelProperty(value = "图片分组id")
    private String groupId;

    @ApiModelProperty(value = "检测目标json")
    private String detectObjs;

    @ApiModelProperty(value = "图片相对路径")
    private String imgUrl;

    @ApiModelProperty(value = "fullPath")
    private String fullPath;

    @ApiModelProperty(value = "1: 本地url，0: 远程图片url")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

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