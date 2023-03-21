package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 信息返回对象
 */
@Data
@ApiModel(value = "DataInfoRes", description = "DataInfoRes")
public class DataInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "score", name = "score")
    private Float score;

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "preName", name = "preName")
    private String preName;

    @ApiModelProperty(value = "url", name = "url")
    private String url;

    @ApiModelProperty(value = "createTime", name = "createTime")
    private Date createTime;
}
