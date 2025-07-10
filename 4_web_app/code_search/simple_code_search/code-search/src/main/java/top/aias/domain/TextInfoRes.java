package top.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 信息返回对象
 * Information Return Object
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Data
@ApiModel(value = "TextInfoRes", description = "信息返回对象")
public class TextInfoRes implements Comparable<TextInfoRes> {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "标题", name = "title")
    private String title;

    @ApiModelProperty(value = "内容", name = "text")
    private String text;

    /**
     * 根据 score 降序排序，1为相似度最高
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(TextInfoRes o) {
        return (score.floatValue() > o.getScore().floatValue()) ? -1 : 1;
    }
}
