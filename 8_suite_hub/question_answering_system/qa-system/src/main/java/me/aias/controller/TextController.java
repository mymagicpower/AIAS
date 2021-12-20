package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.HasCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.config.FileProperties;
import me.aias.domain.LocalStorage;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultRes;
import me.aias.domain.TextInfoDto;
import me.aias.service.FeatureService;
import me.aias.service.LocalStorageService;
import me.aias.service.SearchService;
import me.aias.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本管理
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "文本管理")
@RequestMapping("/api/text")
public class TextController {
    private final FileProperties properties;

    @Autowired
    private TextService textService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private LocalStorageService localStorageService;

    @Value("${search.dimension}")
    String dimension;

    @Value("${search.collectionName}")
    String collectionName;

    @ApiOperation(value = "提取文本特征值")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id) {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String input = localStorage.getPath();
        File file = new File(input);
        CsvReader csvReader = new CsvReader();
        List<TextInfoDto> list = new ArrayList<>();
        TextInfoDto textInfoDto;
        // 解析文本信息
        ConcurrentHashMap<Long, TextInfoDto> map = textService.getMap();
        long size = map.size();
        try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
            CsvRow row;
            while ((row = csvParser.nextRow()) != null) {
                textInfoDto = new TextInfoDto();
                String question = row.getField(0);
                String answer = row.getField(1);
                log.info("question: " + question);
                log.info("answer: " + answer);
                textInfoDto.setId(size++);
                textInfoDto.setQuestion(question);
                textInfoDto.setAnswer(answer);
                List<Float> feature = featureService.textFeature(question);
                textInfoDto.setFeature(feature);
                list.add(textInfoDto);
            }

            // 将向量插入向量引擎
            try {

                HasCollectionResponse response = searchService.hasCollection(this.collectionName);
                if (!response.hasCollection()) {
                    searchService.createCollection(this.collectionName, Long.parseLong(this.dimension));
                    searchService.createIndex(this.collectionName);
                }
                searchService.insertVectors(this.collectionName, list);
                textService.addTexts(list);
            } catch (ConnectFailedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}