package top.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 数据信息对象
 * Data Info Object
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Data
@ApiModel(value = "DataInfo", description = "DataInfo")
public class DataInfo {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "score", name = "score")
    private Float score;

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "preName", name = "preName")
    private String preName;

    @ApiModelProperty(value = "fullPath", name = "fullPath")
    private String fullPath;

    @ApiModelProperty(value = "relativePath", name = "relativePath")
    private String relativePath;

    @ApiModelProperty(value = "feature", name = "feature")
    private float[] feature;

    @ApiModelProperty(value = "createTime", name = "createTime")
    private Date createTime;
}
