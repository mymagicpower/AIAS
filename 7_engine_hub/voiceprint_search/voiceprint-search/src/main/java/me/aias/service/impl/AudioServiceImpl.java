package me.aias.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.exception.BadRequestException;
import me.aias.common.exception.BusinessException;
import me.aias.common.utils.FileUtil;
import me.aias.common.utils.UUIDUtil;
import me.aias.domain.AudioInfo;
import me.aias.domain.ResEnum;
import me.aias.service.AudioService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 音频文件服务
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@Service
public class AudioServiceImpl implements AudioService {
    private static final String FILE_LIST = "audio-list.json";
    private Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    private ConcurrentHashMap<String, String> map;

    public AudioServiceImpl() {
        StringBuilder sb = new StringBuilder();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path, FILE_LIST);
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
     * 新增文件
     */
    public void addAudioFile(String id, String audioPath) {
        map.put(id, audioPath);
        saveAudioList();
    }

    /**
     * 根据ID查询
     */
    public String findById(String id) {
        return map.get(id);
    }

    /**
     * 获取清单
     */
    public ConcurrentHashMap<String, String> getMap() {
        return map;
    }

    /**
     * 保存上传文件列表
     */
    private void saveAudioList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(map);
        try {
            File file = new File(FILE_LIST);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(jsonStr);
        } catch (FileNotFoundException e) {
            logger.error("Storage file not found", e);
        }
    }

    @Override
    public List<AudioInfo> uploadAudios(String rootPath, String UUID)
            throws BusinessException, IOException {
        if (!new File(rootPath).exists()) {
            new File(rootPath).mkdirs();
        }
        String unZipFilePath = rootPath + UUID; // 以压缩文件名为新目录
        try {
            // 记录文件集合
            List<AudioInfo> resultList = new ArrayList<>();
            // 解压缩后的文件
            File[] listFiles = new File(unZipFilePath).listFiles();

            // 判断上传文件是否包含音频文件
            boolean audioFound = false;
            for (File file : listFiles) {
                String suffix = FileUtil.getExtensionName(file.getName());
                if (suffix.equalsIgnoreCase("wav")) {
                    audioFound = true;
                    break;
                }
            }
            if (!audioFound) {
                // 通用异常
                throw new BadRequestException(ResEnum.IMAGE_NOT_FOUND.VALUE);
            }

            // 保存文件到可访问路径
            for (File file : listFiles) {
                AudioInfo audioInfo = new AudioInfo();
                audioInfo.setPreName(file.getName());
                String ext = FileUtil.getExtensionName(file.getName());
                if (ext.equalsIgnoreCase("wav")) {

                    byte[] bytes = FileUtil.getByte(file);
                    String uuid = UUIDUtil.getUUID();
                    audioInfo.setUuid(uuid);
                    String fileName = uuid + "." + ext; // 待存储的文件名
                    String relativePath = FileUtil.generatePath(rootPath);
                    // filePath 完整路径（含uuid文件名）
                    String filePath = rootPath + relativePath + fileName;
                    audioInfo.setFullPath(filePath);
                    audioInfo.setRelativePath(relativePath + fileName);
                    // 转成文件保存
                    FileUtil.bytesToFile(bytes, filePath);
                    resultList.add(audioInfo);
                }
            }
            return resultList;
        } finally {
            // unZipFilePath = rootPath + uuid; // 以压缩文件名为新目录
            FileUtil.delete(unZipFilePath);

        }
    }
}