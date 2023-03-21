package me.aias.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@Getter
@Setter
@NoArgsConstructor
public class LocalStorage  implements Serializable {

    @ApiModelProperty(value = "ID")
    private int id;

    @ApiModelProperty(value = "realName")
    private String realName;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "suffix")
    private String suffix;

    @ApiModelProperty(value = "path")
    private String path;

    @ApiModelProperty(value = "type")
    private String type;

    @ApiModelProperty(value = "size")
    private String size;

    public LocalStorage(String realName, String name, String suffix, String path, String type, String size) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public void copy(LocalStorage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}