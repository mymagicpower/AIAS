package me.aias.service;

import me.aias.common.exception.BusinessException;
import me.aias.domain.DataInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务接口
 * Service Interface
 *
 * @author Calvin
 * @date 2021-12-12
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