package top.aias.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import top.aias.domain.LocalStorage;
import top.aias.service.LocalStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件存储服务
 * File Storage Service
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Service
public class LocalStorageServiceImpl implements LocalStorageService {
    private static final String STORAGE_FILE = "storage-list.json";
    private Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    private int storageId = 1;
    private List<LocalStorage> storageList;

    public LocalStorageServiceImpl() {
        StringBuilder sb = new StringBuilder();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path, STORAGE_FILE);
            BufferedReader br;
            if (file.exists()) {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException e) {
            logger.error("Storage file read error", e);
        }
        String jsonStr = sb.toString();
        if (!StringUtils.isBlank(jsonStr)) {
            storageList = new Gson().fromJson(jsonStr, new TypeToken<List<LocalStorage>>() {
            }.getType());
        } else {
            storageList = new ArrayList<>();
        }
        for (LocalStorage localStorage : storageList) {
            if (localStorage.getId() >= storageId) {
                storageId = localStorage.getId() + 1;
            }
        }
    }

    /**
     * 保存上传文件列表
     * Save Storage List
     */
    public void saveStorageList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(storageList);
        try {
            File file = new File(STORAGE_FILE);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(jsonStr);
        } catch (FileNotFoundException e) {
            logger.error("Storage file not found", e);
        }
    }

    /**
     * 新增文件
     * Add file
     */
    public void addStorageFile(LocalStorage localStorage) {
        localStorage.setId(storageId);
        storageId++;
        storageList.add(localStorage);
        saveStorageList();
    }

    /**
     * 根据ID查询
     * Get file by id
     */
    public LocalStorage findById(int id) {
        for (LocalStorage localStorage : storageList) {
            if (localStorage.getId() == id) {
                return localStorage;
            }
        }
        return null;
    }

    /**
     * 删除
     * Delete
     */
    public boolean delete(int id) {
        for (LocalStorage localStorage : storageList) {
            if (localStorage.getId() == id) {
                storageList.remove(localStorage);
                saveStorageList();
                return true;
            }
        }
        return false;
    }

    public List<LocalStorage> getStorageList() {
        return storageList;
    }

    public void setStorageList(List<LocalStorage> storageList) {
        this.storageList = storageList;
    }


}