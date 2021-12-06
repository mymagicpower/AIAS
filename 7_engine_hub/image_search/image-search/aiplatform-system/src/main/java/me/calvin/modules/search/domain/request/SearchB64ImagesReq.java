package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

/** 图片信息入参对象 */
@Data
@ApiModel(value = "SearchB64ImagesReq", description = "Base64图片信息操作对象")
public class SearchB64ImagesReq {
  @NotBlank(message = "topk 字段必须")
  @ApiModelProperty(value = "返回topk个结果", name = "topk", required = true)
  String topk;

  @NotEmpty(message = "b64s 字段必须")
  @ApiModelProperty(value = "上传图片base64s", name = "b64s", required = true)
  private HashMap<String, String> b64s;

  @NotBlank(message = "类型")
  @ApiModelProperty(value = "0：通用搜索，1：人脸搜索", name = "type", required = true)
  String type;
}
