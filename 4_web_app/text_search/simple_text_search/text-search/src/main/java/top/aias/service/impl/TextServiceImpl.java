package top.aias.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import top.aias.domain.TextInfo;
import top.aias.service.TextService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class TextServiceImpl implements TextService {
    private static final String TEXT_LIST = "text-list.json";
    private Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    private List<TextInfo> dataList;

    public TextServiceImpl() {
        StringBuilder sb = new StringBuilder();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path, TEXT_LIST);
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
            dataList = new Gson().fromJson(jsonStr, new TypeToken<List<TextInfo>>() {
            }.getType());
        } else {
            dataList = new ArrayList<>();
        }
    }

    /**
     * 新增文本
     */
    public void addText(TextInfo textInfo) {
        dataList.add(textInfo);
        saveTextList();
    }

    /**
     * 新增文本
     */
    public void addTexts(List<TextInfo> list) {
        dataList.addAll(list);
        saveTextList();
    }

    /**
     * 更新
     */
    public void update(List<TextInfo> list) {
        dataList = list;
        saveTextList();
    }

    public List<TextInfo> getTextList() {
        return dataList;
    }

    /**
     * 保存上传文件列表
     * save file list
     */
    private void saveTextList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(dataList);
        try {
            File file = new File(TEXT_LIST);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(jsonStr);
        } catch (FileNotFoundException e) {
            logger.error("Storage file not found", e);
        }
    }
}