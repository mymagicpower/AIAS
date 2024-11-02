package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图片信息入参对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
@ApiModel(value = "UrlImageReq", description = "Url图片信息操作对象")
public class UrlImageReq {
  @NotBlank(message = "url 字段必须")
  @ApiModelProperty(value = "图片url", name = "url", required = true)
  String url;
}
