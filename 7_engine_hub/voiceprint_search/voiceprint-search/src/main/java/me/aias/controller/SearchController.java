package me.aias.controller;

import ai.djl.Device;
import ai.djl.ndarray.NDManager;
import io.milvus.Response.SearchResultsWrapper;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
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
        Integer topK = Integer.parseInt(topk);

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
            R<SearchResults> searchResponse = searchService.search(topK, vectorsToSearch);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(searchResponse.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(0);

            // 根据ID获取图片信息
            ConcurrentHashMap<String, String> map = audioService.getMap();
            List<AudioInfoRes> audioInfoResList = new ArrayList<>();
            for (SearchResultsWrapper.IDScore score : scores) {
                AudioInfoRes imageInfoRes = new AudioInfoRes();
                imageInfoRes.setScore(score.getScore());
                imageInfoRes.setId(score.getLongID());
                imageInfoRes.setUrl(baseUrl + map.get("" + score.getLongID()));
                audioInfoResList.add(imageInfoRes);
            }

            return new ResponseEntity<>(ResultRes.success(audioInfoResList, audioInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }
}
