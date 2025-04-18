package top.aias.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aias.domain.LocalStorage;
import top.aias.domain.ResultBean;
import top.aias.domain.TextInfo;
import top.aias.service.FeatureService;
import top.aias.service.LocalStorageService;
import top.aias.service.SearchService;
import top.aias.service.TextService;

import java.io.File;
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
 * @website www.aias.top
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
        CsvReader csvReader = new CsvReader();
        List<TextInfo> list = new ArrayList<>();
        TextInfo textInfo;
        // 解析文本信息
        List<TextInfo> dataList = textService.getTextList();
        long size = dataList.size();
        try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
            CsvRow row;
            while ((row = csvParser.nextRow()) != null) {
                textInfo = new TextInfo();
                String title = row.getField(0);
                String text = row.getField(1);
                log.info("title: " + title);
                log.info("text: " + text);
                textInfo.setId(size++);
                textInfo.setStorageId(localStorage.getId());
                textInfo.setTitle(title);
                textInfo.setText(text);
                float[] feature = featureService.textFeature(title);
                textInfo.setFeature(feature);
                list.add(textInfo);
            }

            // 保存数据
            textService.addTexts(list);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ResultBean.success(), HttpStatus.OK);
    }
}