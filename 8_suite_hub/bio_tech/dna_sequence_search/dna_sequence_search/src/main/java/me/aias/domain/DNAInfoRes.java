package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 信息返回对象
 */
@Data
@ApiModel(value = "DNAInfoRes", description = "信息返回对象")
public class DNAInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "类别", name = "label")
    private String label;

    @ApiModelProperty(value = "DNA序列", name = "sequence")
    private String sequence;
}
