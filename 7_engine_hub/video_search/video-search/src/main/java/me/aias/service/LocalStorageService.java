package me.aias.service;

import me.aias.domain.LocalStorage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件存储服务接口
 * File storage service interface
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Service
public interface LocalStorageService {
    /**
     * 保存上传文件列表
     * Save uploaded file list
     *
     */
    void saveStorageList();

    /**
     * 新增文件
     * Add file
     */
    void addStorageFile(LocalStorage localStorage);

    /**
     * 根据ID查询
     * Find by ID
     */
    LocalStorage findById(int id);

    /**
     * 删除
     * Delete
     */
    boolean delete(int id);

    List<LocalStorage> getStorageList();

    void setStorageList(List<LocalStorage> storageList);


}