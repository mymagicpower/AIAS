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
@ApiModel(value = "SearchUrlImageReq", description = "Url图片信息操作对象")
public class SearchUrlImageReq {
  @NotBlank(message = "url 字段必须")
  @ApiModelProperty(value = "图片url", name = "url", required = true)
  String url;

  @NotBlank(message = "topk 字段必须")
  @ApiModelProperty(value = "返回topk个结果", name = "topk", required = true)
  String topk;

  @NotBlank(message = "save 字段必须")
  @ApiModelProperty(value = "0 不存，1 存盘，必填", name = "save", required = true)
  String save;

  @NotBlank(message = "类型")
  @ApiModelProperty(value = "0：通用搜索，1：人脸搜索", name = "type", required = true)
  String type;
}
