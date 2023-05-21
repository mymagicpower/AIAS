package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 信息返回对象
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Data
@ApiModel(value = "TextInfoRes", description = "信息返回对象")
public class TextInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "问题", name = "question")
    private String question;

    @ApiModelProperty(value = "答案", name = "answer")
    private String answer;
}
