package top.aias.img.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String value;
    private Map<String, Object> data = new HashMap<String, Object>();

    public static ResultBean success() {
        ResultBean rb = new ResultBean();
        rb.setCode(0);
        rb.setValue("Success");
        return rb;
    }

    public static ResultBean failure() {
        ResultBean msg = new ResultBean();
        msg.setCode(-1);
        msg.setValue("Failure");
        return msg;
    }

    public ResultBean() {

    }

    public ResultBean add(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }
}
