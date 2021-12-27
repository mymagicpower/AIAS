package me.aias.domain;

import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    INFO_NOT_FOUND("0001", "未找到任何相似文本"),
    MODEL_ERROR("0002", "模型推理出错"),
    MILVUS_CONNECTION_ERROR("0003", "向量引擎连接错误"),
    SYSTEM_ERROR("1001", "内部系统错误");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
