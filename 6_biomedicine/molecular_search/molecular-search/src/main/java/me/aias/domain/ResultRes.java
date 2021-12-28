package me.aias.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.aias.common.enums.ResEnum;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "公共输出对象", description = "公共输出对象")
public class ResultRes<T> {
    @ApiModelProperty(value = "输出编码，如：0000", name = "code", example = "0000")
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

    /**
     * 系统默认返回内置错误编码
     * code : 9999
     * msg : 系统繁忙
     *
     * @return BaseRes
     */
    public static ResultRes error() {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(ResEnum.SYSTEM_ERROR.KEY);
        baseRes.setMsg(ResEnum.SYSTEM_ERROR.VALUE);
        return baseRes;
    }

    /**
     * 系统默认返回内置错误编码
     * code : 9999
     * msg : 系统繁忙
     *
     * @param data 返回消息数据
     * @return BaseRes
     */
    public static ResultRes error(String data) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(ResEnum.SYSTEM_ERROR.KEY);
        baseRes.setMsg(ResEnum.SYSTEM_ERROR.VALUE);
        return baseRes;
    }

    /**
     * 返回错误消息编码，不包含消息体
     *
     * @param code
     * @param msg
     * @return BaseRes
     */
    public static ResultRes error(String code, String msg) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(code);
        baseRes.setMsg(msg);
        return baseRes;
    }

    /**
     * 返回错误消息编码，包含消息体
     *
     * @param code 编码
     * @param msg  消息
     * @param data 字符串
     * @return BaseRes
     */
    public static ResultRes error(String code, String msg, String data) {
        ResultRes baseRes = new ResultRes();
        baseRes.setCode(code);
        baseRes.setMsg(msg);
        baseRes.setData(data);
        return baseRes;
    }

    /**
     * 返回正确消息体
     *
     * @param data
     * @param <T>
     * @return BaseRes
     */
    public static <T> ResultRes<T> success(T data) {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE, data, 1);
    }

    /**
     * 返回正确消息体
     *
     * @param data
     * @param <T>
     * @return BaseRes
     */
    public static <T> ResultRes<T> success(T data, int total) {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE, data, total);
    }

    /**
     * 返回正确消息
     *
     * @param <T>
     * @return
     */
    public static <T> ResultRes<T> success() {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE);
    }

    /**
     * 判断是否执行正确
     *
     * @return
     */
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
