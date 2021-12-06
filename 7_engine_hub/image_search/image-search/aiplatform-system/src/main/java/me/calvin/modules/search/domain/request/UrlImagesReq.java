package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

/**
 * 图片信息入参对象
 */
@Data
@ApiModel(value = "UrlImagesReq", description = "一组Url图片信息操作对象")
public class UrlImagesReq {
    @NotEmpty(message = "urls 字段必须")
    @ApiModelProperty(value = "图片urls", name = "urls", required = true)
    private HashMap<String, String> urls;

    @NotBlank(message = "save 字段必须")
    @ApiModelProperty(value = "0 不存，1 存盘，必填", name = "save", required = true)
    String save;

    @NotBlank(message = "类型")
    @ApiModelProperty(value = "0：通用搜索，1：人脸搜索", name = "type", required = true)
    String type;
}
