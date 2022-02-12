package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图片信息入参对象
 */
@Data
@ApiModel(value = "B64ImageReq", description = "Base64图片信息操作对象")
public class B64ImageReq {
    @NotBlank(message = "base64 字段必须")
    @ApiModelProperty(value = "base64图片", name = "base64", required = true)
    String base64;
}
