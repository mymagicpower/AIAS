package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 信息返回对象
 */
@Data
@ApiModel(value = "TextInfoRes", description = "信息返回对象")
public class TextInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "标题", name = "title")
    private String title;

    @ApiModelProperty(value = "内容", name = "text")
    private String text;
}
