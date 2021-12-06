package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图片信息入参对象
 */
@Data
@ApiModel(value = "ExtractFeatureReq", description = "提取图片特征操作对象")
public class ExtractFeatureReq {
    @NotBlank(message = "压缩包id")
    @ApiModelProperty(value = "上传的压缩包id", name = "id", required = true)
    String id;

    @NotBlank(message = "类型")
    @ApiModelProperty(value = "0：通用图片特征，1：人脸特征", name = "type", required = true)
    String type;
}


//@NotEmpty 用在集合类上面 Collection、Map、数组
//不能为null或者长度为0(String Collection Map的isEmpty()方法)
//
//@NotBlank
// 只用于String,不能为null且trim()之后size>0
//
//@NotNull:
// 不能为null，但可以为empty,没有Size的约束