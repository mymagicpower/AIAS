package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 图片信息入参对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
@ApiModel(value = "SearchImageReq", description = "图片信息操作对象")
public class SearchImageReq {
  @NotBlank(message = "topK 字段必须")
  @ApiModelProperty(value = "返回topk个结果", name = "topK", required = true)
  int topK;

  @NotBlank(message = "total 字段必须")
  @ApiModelProperty(value = "total 字段", name = "total", required = true)
  int total;

  @NotBlank(message = "file 字段必须")
  @ApiModelProperty(value = "file 字段", name = "file", required = true)
  MultipartFile file;
}
