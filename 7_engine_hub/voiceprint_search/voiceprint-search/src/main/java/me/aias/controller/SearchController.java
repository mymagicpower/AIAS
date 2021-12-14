package me.aias.controller;

import ai.djl.Device;
import ai.djl.ndarray.NDManager;
import com.google.common.collect.Lists;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.utils.FileUtil;
import me.aias.common.utils.JLibrasaEx;
import me.aias.config.FileProperties;
import me.aias.domain.AudioInfoRes;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultRes;
import me.aias.service.AudioService;
import me.aias.service.FeatureService;
import me.aias.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索管理
 *
 * @author Calvin
 * @date 2021-12-12
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
    private AudioService audioService;
    @Autowired
    private FeatureService featureService;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${audio.baseUrl}")
    String baseUrl;

    @PostMapping(value = "/audio")
    @ApiOperation(value = "音频搜索", nickname = "searchAudio")
    public ResponseEntity<Object> searchImage(@RequestParam("audio") MultipartFile audioFile, @RequestParam(value = "topK") String topk) {
        // 生成向量
        Long topK = Long.parseLong(topk);

        float[][] mag;
        try {
            String tmpDir = properties.getPath().getRootPath() + "tmp";
            if (!new File(tmpDir).exists()) {
                new File(tmpDir).mkdirs();
            }
            File file = FileUtil.upload(audioFile, tmpDir);
            NDManager manager = NDManager.newBaseManager(Device.cpu());
            mag = JLibrasaEx.magnitude(manager, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.AUDIO_ERROR.KEY, ResEnum.AUDIO_ERROR.VALUE), HttpStatus.OK);
        }

        List<Float> vectorToSearch;
        try {
            vectorToSearch = featureService.feature(mag);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据音频文件向量搜索
            SearchResponse searchResponse = searchService.search(this.collectionName, topK, vectorsToSearch);
            List<List<Long>> resultIds = searchResponse.getResultIdsList();
            List<String> idList = Lists.transform(resultIds.get(0), (entity) -> {
                return entity.toString();
            });

            // 根据ID获取图片信息
            ConcurrentHashMap<String, String> map = audioService.getMap();
            List<AudioInfoRes> audioInfoResList = new ArrayList<>();
            for (String id : idList) {
                AudioInfoRes imageInfoRes = new AudioInfoRes();
                Float score = maxScore(searchResponse, Long.parseLong(id));
                imageInfoRes.setScore(score);
                imageInfoRes.setId(Long.parseLong(id));
                imageInfoRes.setUrl(baseUrl + map.get(id));
                audioInfoResList.add(imageInfoRes);
            }

            return new ResponseEntity<>(ResultRes.success(audioInfoResList, audioInfoResList.size()), HttpStatus.OK);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private Float maxScore(SearchResponse searchResponse, Long imageId) {
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
