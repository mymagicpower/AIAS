package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 音频信息对象
 */
@Data
@ApiModel(value = "AudioInfo", description = "音频信息对象")
public class AudioInfo {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "score", name = "score")
    private Float score;

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "原名称", name = "preName")
    private String preName;

    @ApiModelProperty(value = "全路径", name = "fullPath")
    private String fullPath;

    @ApiModelProperty(value = "相对路径", name = "relativePath")
    private String relativePath;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;
}
