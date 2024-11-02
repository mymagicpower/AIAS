package me.calvin.modules.search.domain.enums;

import lombok.Getter;
/**
 * 状态枚举
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    PACKAGE_FILE_FAIL("0001", "压缩包类型应为zip格式"),
    PACKAGE_DECOMPRESSION_FAIL("0002", "压缩包解压异常"),
    IMAGE_NOT_FOUND("0003", "上传压缩包未找到照片!"),
    MODEL_ERROR("0004", "模型推理出错"),
    MILVUS_CONNECTION_ERROR("0005", "向量引擎连接错误"),
    SYSTEM_ERROR("1001", "内部系统错误");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
