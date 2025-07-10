package top.aias.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import cn.hutool.json.JSONObject;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.aias.common.constant.Constants;
import top.aias.common.utils.FileUtils;
import top.aias.domain.*;
import top.aias.service.FeatureService;
import top.aias.service.LocalStorageService;
import top.aias.service.SearchService;
import top.aias.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本管理
 * Data management
 *
 * @author Calvin
 * @email 179209347@qq.com
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "文本管理")
@RequestMapping("/api/text")
public class TextController {
    @Autowired
    private TextService textService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private LocalStorageService localStorageService;

    @ApiOperation(value = "提取文本特征值")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id) {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String input = localStorage.getPath();
        File file = new File(input);
        String extName = FileUtils.getExtensionName(file.getName());

        // 解析文本信息
        List<TextInfo> dataList = textService.getTextList();
        long size = dataList.size();
        List<TextInfo> list = new ArrayList<>();
        TextInfo textInfo;
        if (extName.equalsIgnoreCase(Constants.JSONL)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    textInfo = new TextInfo();
                    JSONObject json = new JSONObject(line);
                    // 从JSON对象中获取相应的键值对
                    String title = (String)json.get("url");
                    String text = (String)json.get("code");
                    log.info("title: " + title);
//                log.info("text: " + text);
                    textInfo.setId(size++);
                    textInfo.setStorageId(localStorage.getId());
                    textInfo.setTitle(title);
                    textInfo.setText(text);
                    List<Float> feature = featureService.textFeature(text);
                    textInfo.setFeature(feature);
                    list.add(textInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelException e) {
                e.printStackTrace();
            } catch (TranslateException e) {
                e.printStackTrace();
            }
        } else if (extName.equalsIgnoreCase(Constants.CSV)) {
            CsvReader csvReader = new CsvReader();
            try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
                CsvRow row;
                while ((row = csvParser.nextRow()) != null) {
                    textInfo = new TextInfo();
                    String title = row.getField(0);
                    String text = row.getField(1);
                    log.info("title: " + title);
//                    log.info("text: " + text);
                    textInfo.setId(size++);
                    textInfo.setTitle(title);
                    textInfo.setText(text);
                    List<Float> feature = featureService.textFeature(title);
                    textInfo.setFeature(feature);
                    list.add(textInfo);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(ResultBean.failure(), HttpStatus.OK);
        }

        // 将向量插入Milvus向量引擎
        R<Boolean> response = searchService.hasCollection();
        if (!response.getData()) {
            searchService.initSearchEngine();
        }

        List<Long> vectorIds = new ArrayList<>();
        List<List<Float>> vectors = new ArrayList<>();
        for (TextInfo item : list) {
            vectorIds.add(item.getId());
            vectors.add(item.getFeature());

        }
        searchService.insert(vectorIds, vectors);
        textService.addTexts(list);

        // 检查是否加载 collection， 如果没有，插入数据后加载
        boolean loaded = searchService.getCollectionState();
        if (!loaded) {
            searchService.loadCollection();
        }


        return new ResponseEntity<>(ResultBean.success(), HttpStatus.OK);
    }
}