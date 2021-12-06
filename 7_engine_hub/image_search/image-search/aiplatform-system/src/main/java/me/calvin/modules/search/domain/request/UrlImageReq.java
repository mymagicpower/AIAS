package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/** 图片信息入参对象 */
@Data
@ApiModel(value = "UrlImageReq", description = "Url图片信息操作对象")
public class UrlImageReq {
  @NotBlank(message = "url 字段必须")
  @ApiModelProperty(value = "图片url", name = "url", required = true)
  String url;

  @NotBlank(message = "save 字段必须")
  @ApiModelProperty(value = "必填: 0 不存，1 存盘", name = "save", required = true)
  String save;

  @NotBlank(message = "类型")
  @ApiModelProperty(value = "0：通用搜索，1：人脸搜索", name = "type", required = true)
  String type;
}
