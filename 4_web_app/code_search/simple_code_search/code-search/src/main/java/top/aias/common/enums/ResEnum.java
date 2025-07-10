package top.aias.common.enums;

import lombok.Getter;

/**
 * 状态枚举
 * Status enumeration
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    ZIP_FILE_FAIL("0001", "压缩包类型错误"),
    DECOMPRESSION_FAIL("0002", "压缩包解压异常"),
    SYSTEM_ERROR("1001", "内部系统错误");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
