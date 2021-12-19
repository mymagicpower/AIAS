package me.aias.controller;

import com.google.common.collect.Lists;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultBean;
import me.aias.domain.TextInfoDto;
import me.aias.domain.TextInfoRes;
import me.aias.service.FeatureService;
import me.aias.service.SearchService;
import me.aias.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private SearchService searchService;
    @Autowired
    private TextService textService;
    @Autowired
    private FeatureService featureService;

    @Value("${search.collectionName}")
    String collectionName;

    @GetMapping("/text")
    @ApiOperation(value = "文本搜索", nickname = "search")
    public ResultBean search(@RequestParam("text") String text, @RequestParam(value = "topK") String topk) {
        Long topK = Long.parseLong(topk);
        List<Float> vectorToSearch = null;
        try {
            vectorToSearch = featureService.textFeature(text);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据向量搜索
            SearchResponse searchResponse = searchService.search(this.collectionName, topK, vectorsToSearch);
            List<List<Long>> resultIds = searchResponse.getResultIdsList();
            if (resultIds == null || resultIds.size() == 0) {
                return ResultBean.failure().add(ResEnum.INFO_NOT_FOUND.KEY, ResEnum.INFO_NOT_FOUND.VALUE);
            }
            List<String> idList = Lists.transform(resultIds.get(0), (entity) -> {
                return entity.toString();
            });

            // 根据ID获取文本信息
            ConcurrentHashMap<Long, TextInfoDto> map = textService.getMap();
            List<TextInfoRes> textInfoResList = new ArrayList<>();
            for (String uid : idList) {
                Long id = Long.parseLong(uid);
                TextInfoDto textInfoDto = map.get(id);
                TextInfoRes textInfoRes = new TextInfoRes();
                Float score = maxScoreForTextId(searchResponse, id);
                textInfoRes.setId(id);
                textInfoRes.setScore(score);
                textInfoRes.setTitle(textInfoDto.getTitle());
                textInfoRes.setText(textInfoDto.getText());
                textInfoResList.add(textInfoRes);
            }

            return ResultBean.success().add("result", textInfoResList);
//            return new ResponseEntity<>(ResultRes.success(textInfoResList, textInfoResList.size()), HttpStatus.OK);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE);
        }
    }

    private Float maxScoreForTextId(SearchResponse searchResponse, Long id) {
        float maxScore = -1;
        List<SearchResponse.QueryResult> list = searchResponse.getQueryResultsList().get(0);
        for (SearchResponse.QueryResult result : list) {
            if (result.getVectorId() == id.longValue()) {
                if (result.getDistance() > maxScore) {
                    maxScore = result.getDistance();
                }
            }
        }

        return maxScore;
    }
}
