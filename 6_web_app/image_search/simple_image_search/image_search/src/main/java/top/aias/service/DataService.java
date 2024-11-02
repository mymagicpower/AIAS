package top.aias.service;

import top.aias.common.exception.BusinessException;
import top.aias.domain.DataInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据服务接口
 * Service Interface
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface DataService {
    /**
     * 添加图片信息
     */
    public void addData(DataInfo imageInfo);

    /**
     * 添加图片信息
     */
    public void addData(List<DataInfo> list);

    /**
     * 更新图片信息
     */
    void update(List<DataInfo> list);

    /**
     * 图片信息列表
     */
    public List<DataInfo> getImageList();


    /**
     * 上传音频文件
     * Upload audio files
     */
    public List<DataInfo> uploadData(String rootPath, String UUID)
            throws BusinessException, IOException;
}