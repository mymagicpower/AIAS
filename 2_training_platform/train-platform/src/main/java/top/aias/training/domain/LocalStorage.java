package top.aias.training.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 存储对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Getter
@Setter
@NoArgsConstructor
public class LocalStorage  implements Serializable {

    private int id;
    private String realName;
    private String name;
    private String suffix;
    private String path;
    private String type;
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