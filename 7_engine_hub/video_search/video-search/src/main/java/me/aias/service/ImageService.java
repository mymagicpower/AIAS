package me.aias.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片服务接口
 * Image service interface
 *
 * @author Calvin
 * @date 2021-12-12
 **/
public interface ImageService {
    /**
     * 根据ID查询图片路径
     * get image by id
     */
    String findById(String id);

    /**
     * 添加图片
     * add image
     */
    void addImageFile(String id, String imagePath);

    /**
     * 获取图片清单
     * get image list
     */
    ConcurrentHashMap<String, String> getMap();
}