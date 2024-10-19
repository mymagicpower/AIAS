package top.aias.service;

import top.aias.domain.LocalStorage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件存储服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
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