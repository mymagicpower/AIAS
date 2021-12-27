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
     */
    void saveStorageList();

    /**
     * 新增文件
     */
    void addStorageFile(LocalStorage localStorage);

    /**
     * 根据ID查询
     */
    LocalStorage findById(int id);

    /**
     * 删除
     */
    boolean delete(int id);

    List<LocalStorage> getStorageList();

    void setStorageList(List<LocalStorage> storageList);


}