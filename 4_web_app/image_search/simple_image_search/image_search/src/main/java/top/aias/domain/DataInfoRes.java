package top.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 信息返回对象
 * Information Return Object
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Data
@ApiModel(value = "DataInfoRes", description = "DataInfoRes")
public class DataInfoRes implements Comparable<DataInfoRes> {
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

    /**
     * 根据 score 降序排序，1为相似度最高
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(DataInfoRes o) {
        return (score.floatValue() > o.getScore().floatValue()) ? -1 : 1;
    }
}
