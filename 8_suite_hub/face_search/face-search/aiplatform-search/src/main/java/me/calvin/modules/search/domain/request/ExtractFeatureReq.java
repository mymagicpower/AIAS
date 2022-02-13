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
}