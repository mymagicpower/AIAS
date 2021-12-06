package me.calvin.modules.search.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

/**
 * 图片信息入参对象
 */
@Data
@ApiModel(value = "B64ImagesReq", description = "一组Base64图片信息")
public class B64ImagesReq {
    @NotEmpty(message = "b64s 字段必须")
    @ApiModelProperty(value = "上传一组base64图片", name = "b64s", required = true)
    private HashMap<String, String> b64s;

    @NotBlank(message = "类型")
    @ApiModelProperty(value = "0：通用搜索，1：人脸搜索", name = "type", required = true)
    String type;
}
