package top.aias.platform.bean;

import lombok.Data;

import java.util.List;

/**
 * 图片对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class ImageBean {
    private String uid;
    private String name;
    private String imageName;
    List<LabelBean> labelData;
}