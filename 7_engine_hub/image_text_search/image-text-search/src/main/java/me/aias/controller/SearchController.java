package me.aias.controller;

import com.google.common.collect.Lists;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.config.FileProperties;
import me.aias.domain.DataInfoRes;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultRes;
import me.aias.service.DataService;
import me.aias.service.FeatureService;
import me.aias.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索管理
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@Api(tags = "搜索管理")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    private final FileProperties properties;

    @Autowired
    private SearchService searchService;
    @Autowired
    private DataService imageService;
    @Autowired
    private FeatureService featureService;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${image.baseUrl}")
    String baseUrl;

    @GetMapping("/text")
    @ApiOperation(value = "搜索", nickname = "search")
    public ResponseEntity<Object> searchImage(@RequestParam("text") String text, @RequestParam(value = "topK") String topk) {
        // 生成向量
        Long topK = Long.parseLong(topk);
        List<Float> vectorToSearch;
        try {
            vectorToSearch = featureService.textFeature(text);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据向量搜索
            SearchResponse searchResponse = searchService.search(this.collectionName, topK, vectorsToSearch);
            List<List<Long>> resultIds = searchResponse.getResultIdsList();
            List<String> idList = Lists.transform(resultIds.get(0), (entity) -> {
                return entity.toString();
            });

            // 根据ID获取图片信息
            ConcurrentHashMap<String, String> map = imageService.getMap();
            List<DataInfoRes> imageInfoResList = new ArrayList<>();
            for (String id : idList) {
                DataInfoRes imageInfoRes = new DataInfoRes();
                Float score = maxScoreForImageId(searchResponse, Long.parseLong(id));
                imageInfoRes.setScore(score);
                imageInfoRes.setId(Long.parseLong(id));
                imageInfoRes.setUrl(baseUrl + map.get(id));
                imageInfoResList.add(imageInfoRes);
            }

            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private Float maxScoreForImageId(SearchResponse searchResponse, Long imageId) {
        float maxScore = -1;
        List<SearchResponse.QueryResult> list = searchResponse.getQueryResultsList().get(0);
        for (SearchResponse.QueryResult result : list) {
            if (result.getVectorId() == imageId.longValue()) {
                if (result.getDistance() > maxScore) {
                    maxScore = result.getDistance();
                }
            }
        }

        return maxScore;
    }
}
