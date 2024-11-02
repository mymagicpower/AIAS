package top.aias.domain;

import lombok.Getter;
/**
 * 状态枚举
 * Status enumeration
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Getter
public enum ResEnum {
    SUCCESS("0000", "success"),
    PACKAGE_FILE_FAIL("0001", "Wrong compressed file type"),
    PACKAGE_DECOMPRESSION_FAIL("0002", "Exception occurred while decompressing the package"),
    UPLOAD_FAIL("0003", "Failed to upload compressed file!"),
    IMAGE_NOT_FOUND("0004", "Failed to find photo in the uploaded compressed file!"),
    MODEL_ERROR("0005", "Error occurred while inferring the model"),
    MILVUS_CONNECTION_ERROR("0006", "Vector engine connection error"),
    SYSTEM_ERROR("1001", "Internal system error");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
