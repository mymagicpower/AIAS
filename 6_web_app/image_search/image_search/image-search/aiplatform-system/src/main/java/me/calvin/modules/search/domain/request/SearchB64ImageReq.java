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
@ApiModel(value = "SearchB64ImageReq", description = "Base64图片信息操作对象")
public class SearchB64ImageReq {
  @NotBlank(message = "base64 字段必须")
  @ApiModelProperty(value = "图片base64", name = "base64", required = true)
  String base64;

  @NotBlank(message = "topk 字段必须")
  @ApiModelProperty(value = "返回topk个结果", name = "topk", required = true)
  String topk;

  @NotBlank(message = "类型")
  @ApiModelProperty(value = "1：人脸搜索，2：通用搜索", name = "type", required = true)
  String type;
}
