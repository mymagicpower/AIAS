package top.aias.training.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import top.aias.training.common.enums.ResEnum;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultRes<T> {
    //输出编码
    private String code;
    //输出信息
    private String msg;
    //输出对象数量
    private int total;
    //输出数据
    private T data;

    public ResultRes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 系统默认返回内置错误编码
     * System default return built-in error code
     *
     * code : 9999
     * msg : 系统繁忙 - System busy
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
     * System default return built-in error code
     *
     * code : 9999
     * msg : 系统繁忙 - System busy
     *
     * @param data
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
     * Return error message code, does not include message body
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
     * Return error message code, including message body
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
     * Return correct message body
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
     *Return correct message body
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
     * Return correct message
     *
     * @param <T>
     * @return
     */
    public static <T> ResultRes<T> success() {
        return new ResultRes(ResEnum.SUCCESS.KEY, ResEnum.SUCCESS.VALUE);
    }

    /**
     * 判断是否执行正确
     * Determine whether to execute correctly
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
