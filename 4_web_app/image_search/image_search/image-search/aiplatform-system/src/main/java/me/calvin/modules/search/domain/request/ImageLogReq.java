package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图片上传信息入参对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
@ApiModel(value = "ImageLogReq", description = "图片上传信息操作对象")
public class ImageLogReq {
    @NotBlank(message = "id 字段必须")
    @ApiModelProperty(value = "id，非必填", name = "id", required = true)
    private String id;
}


