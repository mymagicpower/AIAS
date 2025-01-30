package top.aias.training.common.enums;

import lombok.Getter;

/**
 * 状态枚举
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    ZIP_FILE_FAIL("0001", "错误的压缩文件类型"),
    DECOMPRESSION_FAIL("0002", "解压缩出错"),
    SYSTEM_ERROR("1001", "内部系统错误");

    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
