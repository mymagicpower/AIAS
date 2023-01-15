package me.aias.service;

import me.aias.common.exception.BusinessException;
import me.aias.domain.DataInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务接口
 *
 * @author Calvin
 * @date 2021-12-12
 **/
public interface DataService {
    /**
     * 根据ID查询图片路径
     */
    String findById(String id);

    /**
     * 添加图片
     */
    void addData(String id, String audioPath);

    /**
     * 获取清单
     */
    ConcurrentHashMap<String, String> getMap();

    /**
     * 上传音频文件
     */
    public List<DataInfo> uploadData(String rootPath, String UUID)
            throws BusinessException, IOException;
}