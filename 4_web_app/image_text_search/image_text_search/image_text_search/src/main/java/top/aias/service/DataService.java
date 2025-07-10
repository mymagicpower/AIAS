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
     * 根据ID查询图片路径
     * Find image path by ID
     */
    String findById(String id);

    /**
     * 添加图片
     * Add image
     */
    void addData(String id, String audioPath);

    /**
     * 获取清单
     * Get list
     */
    ConcurrentHashMap<String, String> getMap();

    /**
     * 上传音频文件
     * Upload audio files
     */
    public List<DataInfo> uploadData(String rootPath, String UUID)
            throws BusinessException, IOException;
}