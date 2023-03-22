package me.aias.service;

import me.aias.domain.LocalStorage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件存储服务接口
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Service
public interface LocalStorageService {
    /**
     * 保存上传文件列表
     * save file list
     */
    void saveStorageList();

    /**
     * 新增文件
     * add file
     */
    void addStorageFile(LocalStorage localStorage);

    /**
     * 根据ID查询
     * get file by id
     */
    LocalStorage findById(int id);

    /**
     * 删除
     * delete
     */
    boolean delete(int id);

    List<LocalStorage> getStorageList();

    void setStorageList(List<LocalStorage> storageList);


}