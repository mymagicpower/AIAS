package top.aias.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import top.aias.common.exception.BadRequestException;
import top.aias.common.exception.BusinessException;
import top.aias.common.utils.FileUtils;
import top.aias.common.utils.UUIDUtils;
import top.aias.domain.DataInfo;
import top.aias.domain.ImageType;
import top.aias.domain.ResEnum;
import top.aias.service.DataService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;

/**
 * 数据服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class DataServiceImpl implements DataService {
    private static final String DATA_FILE = "data-list.json";
    private Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    private List<DataInfo> imageDataList;

    public DataServiceImpl() {
        StringBuilder sb = new StringBuilder();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path, DATA_FILE);
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
            imageDataList = new Gson().fromJson(jsonStr, new TypeToken<List<DataInfo>>() {
            }.getType());
        } else {
            imageDataList = new ArrayList<>();
        }
    }

    /**
     * 新增文件
     */
    public void addData(DataInfo imageInfo) {
        imageDataList.add(imageInfo);
        saveImageList();
    }

    /**
     * 新增文件
     */
    public void addData(List<DataInfo> list) {
        imageDataList.addAll(list);
        saveImageList();
    }

    /**
     * 更新
     */
    public void update(List<DataInfo> list) {
        imageDataList = list;
        saveImageList();
    }

    /**
     * 保存上传文件列表
     * save file list
     */
    private void saveImageList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(imageDataList);
        try {
            File file = new File(DATA_FILE);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(jsonStr);
        } catch (FileNotFoundException e) {
            logger.error("Storage file not found", e);
        }
    }

    public List<DataInfo> getImageList() {
        return imageDataList;
    }

    @Override
    public List<DataInfo> uploadData(String rootPath, String UUID)
            throws BusinessException, IOException {
        if (!new File(rootPath).exists()) {
            new File(rootPath).mkdirs();
        }
        String unZipFilePath = rootPath + UUID; // 以压缩文件名为新目录 - create new folder
        try {
            // 记录文件集合
            // record file list
            List<DataInfo> resultList = new ArrayList<>();
            // 解压缩后的文件
            // file list after unzip
            File[] listFiles = new File(unZipFilePath).listFiles();

            // 判断上传文件是否包含音频文件
            // check if audio file existing
            boolean found = false;
            for (File file : listFiles) {
                String suffix = FileUtils.getExtensionName(file.getName());
                if ((suffix.equalsIgnoreCase(ImageType.FILE_JPEG.key)
                        || suffix.equalsIgnoreCase(ImageType.FILE_JPG.key)
                        || suffix.equalsIgnoreCase(ImageType.FILE_PNG.key))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new BadRequestException(ResEnum.IMAGE_NOT_FOUND.VALUE);
            }

            // 保存文件到可访问路径
            // save file to available path
            for (File file : listFiles) {
                DataInfo dataInfo = new DataInfo();
                dataInfo.setPreName(file.getName());
                String suffix = FileUtils.getExtensionName(file.getName());
                if ((suffix.equalsIgnoreCase(ImageType.FILE_JPEG.key)
                        || suffix.equalsIgnoreCase(ImageType.FILE_JPG.key)
                        || suffix.equalsIgnoreCase(ImageType.FILE_PNG.key))) {

                    byte[] bytes = FileUtils.getByte(file);
                    String uuid = UUIDUtils.getUUID();
                    dataInfo.setUuid(uuid);
                    String fileName = uuid + "." + suffix;
                    String relativePath = FileUtils.generatePath(rootPath);
                    // filePath 完整路径（含uuid文件名）
                    // full file path
                    String filePath = rootPath + relativePath + fileName;
                    dataInfo.setFullPath(filePath);
                    dataInfo.setRelativePath(relativePath + fileName);
                    // 转成文件保存
                    // convert into file to save
                    FileUtils.bytesToFile(bytes, filePath);
                    resultList.add(dataInfo);
                }
            }
            return resultList;
        } finally {
            // unZipFilePath = rootPath + uuid;
            FileUtils.delete(unZipFilePath);

        }
    }
}