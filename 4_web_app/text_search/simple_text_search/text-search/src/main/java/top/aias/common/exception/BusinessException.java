package top.aias.common.exception;

import lombok.Data;

/**
 * 异常信息
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Data
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.msg = msg;
    }


}
