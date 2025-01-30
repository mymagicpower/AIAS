package top.aias.training.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import top.aias.training.domain.LocalStorage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 存储服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface LocalStorageService {

    /**
     * 保存上传文件列表
     * save file list
     */
    public void saveStorageList();

    /**
     * 新增文件
     * add new file
     */
    public void addStorageFile(LocalStorage localStorage);

    /**
     * 根据ID查询
     * file file by id
     */
    public LocalStorage findById(int id);

    /**
     * 删除
     * delete
     */
    public boolean delete(int id);

    public List<LocalStorage> getStorageList();

    public void setStorageList(List<LocalStorage> storageList);

}