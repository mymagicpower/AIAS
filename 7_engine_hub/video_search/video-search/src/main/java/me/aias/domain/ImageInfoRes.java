package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 图片信息返回对象
 * Image information response object
 *
 */
@Data
@ApiModel(value = "ImageInfoRes", description = "图片信息返回对象 - Image information response object")
public class ImageInfoRes {
  @ApiModelProperty(value = "大图 - Big picture", name = "id")
  private Long id;

  @ApiModelProperty(value = "Score", name = "score")
  private Float score;
  
  @ApiModelProperty(value = "uuid", name = "uuid")
  private String uuid;

  @ApiModelProperty(value = "图片原名称 - Image original name", name = "preName")
  private String preName;

  @ApiModelProperty(value = "图片url - Image URL", name = "imgUrl")
  private String imgUrl;

  @ApiModelProperty(value = "Type - 1: local URL, 0: remote image URL", name = "type")
  private String type;

  @ApiModelProperty(value = "Image URL", name = "imgUrl")
  private List<SimpleFaceObject> faces;

  @ApiModelProperty(value = "Creation time", name = "createTime")
  private Date createTime; 
}
