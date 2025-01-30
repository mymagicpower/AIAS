package top.aias.training.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练超参数
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class TrainArgument implements Serializable {

    //迭代周期
    private Integer epoch;

    //批次大小
    private Integer batchSize;

    //分类数量
    private Integer nClasses;

    //图像分类标签
    private String classLabels;

    //目标检测分类标签
    private String detLabels;

    public void copy(TrainArgument source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}