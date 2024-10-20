package me.calvin.modules.search.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.calvin.modules.search.domain.enums.ResEnum;
/**
 * 信息返回对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "输出对象", description = "输出对象")
public class ResultRes<T> {
    @ApiModelProperty(value = "输出编码", name = "code", example = "0000")
    private String code;
    @ApiModelProperty(value = "输出消息(String)", name = "msg", example = "操作成功")
    private String msg;
    @ApiModelProperty(value = "输出对象(Object)", name = "data")
    private int total;
    @ApiModelProperty(value = "输出对象数量", name = "total")
    private T data;

    public ResultRes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultRes error() {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(ResEnum.SYSTEM_ERROR.KEY);
        baseRes.setMsg(ResEnum.SYSTEM_ERROR.VALUE);
        return baseRes;
    }

    public static ResultRes error(String data) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(ResEnum.SYSTEM_ERROR.KEY);
        baseRes.setMsg(ResEnum.SYSTEM_ERROR.VALUE);
        return baseRes;
    }

    public static ResultRes error(String code, String msg) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(code);
        baseRes.setMsg(msg);
        return baseRes;
    }

    public static ResultRes error(String code, String msg, String data) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(code);
        baseRes.setMsg(msg);
        baseRes.setData(data);
        return baseRes;
    }

    public static <T> ResultRes<T> success(T data) {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE, data, 1);
    }

    public static <T> ResultRes<T> success(T data, int total) {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE, data, total);
    }

    public static <T> ResultRes<T> success() {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE);
    }

    public boolean isSuccess() {
        return this.code.equals(ResEnum.SUCCESS.KEY);
    }


    public String toString() {
        return "BaseRes(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public ResultRes(final String code, final String msg, final T data, int total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    public ResultRes() {
    }
}
