package top.aias.iocr.bean;

import lombok.Data;

import java.util.List;
/**
 * 模板对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class TemplateBean {
    private String uid;
    private String name;
    private String imageName;
    List<LabelBean> labelData;
}