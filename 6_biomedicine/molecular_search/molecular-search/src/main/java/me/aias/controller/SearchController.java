package me.aias.controller;

import com.google.common.collect.Lists;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.domain.MolInfoDto;
import me.aias.domain.MolInfoRes;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultBean;
import me.aias.service.FeatureService;
import me.aias.service.MolService;
import me.aias.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.ByteBuffer;
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
    private MolService textService;
    @Autowired
    private FeatureService featureService;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${image.baseUrl}")
    String baseUrl;

    @GetMapping("/mol")
    @ApiOperation(value = "分子搜索", nickname = "search")
    public ResultBean search(@RequestParam("smiles") String smiles, @RequestParam(value = "topK") String topk) {
        Long topK = Long.parseLong(topk);
        ByteBuffer vectorToSearch = null;
        try {
            smiles = URLDecoder.decode(smiles,"UTF-8");
            vectorToSearch = featureService.molFeature(smiles);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }

        List<ByteBuffer> vectorsToSearch = new ArrayList<>();
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
            ConcurrentHashMap<Long, MolInfoDto> map = textService.getMap();
            List<MolInfoRes> textInfoResList = new ArrayList<>();
            for (String uid : idList) {
                Long id = Long.parseLong(uid);
                MolInfoDto molInfoDto = map.get(id);
                MolInfoRes molInfoRes = new MolInfoRes();
                Float score = maxScoreForTextId(searchResponse, id);
                molInfoRes.setId(id);
                molInfoRes.setScore(score);
                molInfoRes.setSmiles(molInfoDto.getSmiles());
                molInfoRes.setUrl(baseUrl + molInfoDto.getId() + ".png");
                textInfoResList.add(molInfoRes);
            }

            return ResultBean.success().add("result", textInfoResList);
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
