package me.aias.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import me.aias.service.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片服务
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private static final String IMAGE_FILE_LIST = "image-list.json";
    private Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    private ConcurrentHashMap<String, String> map;

    public ImageServiceImpl() {
        StringBuilder sb = new StringBuilder();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path, IMAGE_FILE_LIST);
            BufferedReader br;
            if (file.exists()) {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException e) {
            logger.error("File read error", e);
        }
        String jsonStr = sb.toString();
        if (!StringUtils.isBlank(jsonStr)) {
            map = new Gson().fromJson(jsonStr, new TypeToken<ConcurrentHashMap<String, String>>() {
            }.getType());
        } else {
            map = new ConcurrentHashMap<>();
        }
    }

    /**
     * 新增图片
     */
    public void addImageFile(String id, String imagePath) {
        map.put(id, imagePath);
        saveImageList();
    }

    /**
     * 根据ID查询
     */
    public String findById(String id) {
        return map.get(id);
    }

    /**
     * 获取图片清单
     */
    public ConcurrentHashMap<String, String> getMap() {
        return map;
    }

    /**
     * 保存上传文件列表
     */
    private void saveImageList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(map);
        try {
            File file = new File(IMAGE_FILE_LIST);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(jsonStr);
        } catch (FileNotFoundException e) {
            logger.error("Storage file not found", e);
        }
    }
}