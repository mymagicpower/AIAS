package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/** 图片信息返回对象 */
@Data
@ApiModel(value = "ImageInfoRes", description = "图片信息返回对象")
public class ImageInfoRes {
  @ApiModelProperty(value = "大图", name = "id")
  private Long id;

  @ApiModelProperty(value = "Score", name = "score")
  private Float score;
  
  @ApiModelProperty(value = "大图uuid", name = "uuid")
  private String uuid;

  @ApiModelProperty(value = "图片原名称", name = "preName")
  private String preName;

  @ApiModelProperty(value = "图片url", name = "imgUrl")
  private String imgUrl;

  @ApiModelProperty(value = "类型 - 1: 本地url，0: 远程图片url", name = "type")
  private String type;

  @ApiModelProperty(value = "图片url", name = "imgUrl")
  private List<SimpleFaceObject> faces;

  @ApiModelProperty(value = "创建时间", name = "createTime")
  private Date createTime; 
}
