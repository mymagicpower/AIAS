package me.aias.domain;

import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    PACKAGE_FILE_FAIL("0001", "压缩包类型错误"),
    PACKAGE_DECOMPRESSION_FAIL("0002", "压缩包解压异常"),
    UPLOAD_FAIL("0003", "上传压缩包失败!"),
    IMAGE_NOT_FOUND("0004", "上传压缩包未找到照片!"),
    MODEL_ERROR("0005", "模型推理出错"),
    MILVUS_CONNECTION_ERROR("0006", "向量引擎连接错误"),
    SYSTEM_ERROR("1001", "内部系统错误");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
