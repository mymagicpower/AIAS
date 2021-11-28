package me.aias.common.exception;

import lombok.Data;

/**
 * @author Calvin
 * @date 2021-06-20
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
