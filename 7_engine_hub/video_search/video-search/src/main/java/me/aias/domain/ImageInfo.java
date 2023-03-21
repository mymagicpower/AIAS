package me.aias.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Calvin
 *
 * @date 2021-02-17
 **/
@Data
public class ImageInfo implements Serializable {

    @ApiModelProperty(value = "id")
    private Long imageId;

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "preName")
    private String preName;

    @ApiModelProperty(value = "group id")
    private String groupId;

    @ApiModelProperty(value = "json")
    private String detectObjs;

    @ApiModelProperty(value = "image url")
    private String imgUrl;

    @ApiModelProperty(value = "fullPath")
    private String fullPath;

    @ApiModelProperty(value = "1: local url，0: remote url")
    private Integer type;

    @ApiModelProperty(value = "Created Time")
    private Timestamp createTime;

    @ApiModelProperty(value = "createBy")
    private String createBy;

    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @ApiModelProperty(value = "updateBy")
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