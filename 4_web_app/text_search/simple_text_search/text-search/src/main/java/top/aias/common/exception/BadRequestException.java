package top.aias.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 错误异常类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Getter
public class BadRequestException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public BadRequestException(String msg){
        super(msg);
    }

    public BadRequestException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
