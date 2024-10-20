package top.aias.controller;

import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.response.SearchResultsWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.aias.domain.DataInfoRes;
import top.aias.domain.FaceObject;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultRes;
import top.aias.service.DataService;
import top.aias.service.DetectService;
import top.aias.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索管理
 * Search management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Api(tags = "搜索管理 -Search management")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private DataService imageService;
    @Autowired
    private DetectService detectService;

    @Value("${image.baseUrl}")
    String baseUrl;

    @PostMapping(value = "/image")
    @ApiOperation(value = "搜索图片 - image search", nickname = "searchImage")
    public ResponseEntity<Object> searchImage(@RequestParam("image") MultipartFile imageFile, @RequestParam(value = "topK") String topk) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image img = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            Integer topK = Integer.parseInt(topk);
            List<Float> vectorToSearch;
            try {
                //人脸检测 & 特征提取
                // Face detection & feature extraction
                List<FaceObject> faceObjects = detectService.faceDetect(img);
                //如何有多个人脸，取第一个（也可以选最大的，或者一起送入搜索引擎搜索）
                // If there are multiple faces, take the first one (or the largest, or search together with the search engine)
                vectorToSearch = faceObjects.get(0).getFeature();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
            }

            List<List<Float>> vectorsToSearch = new ArrayList<>();
            vectorsToSearch.add(vectorToSearch);

            // 根据图片向量搜索
            // Search for vectors based on image files
            R<SearchResults> searchResponse = searchService.search(topK, vectorsToSearch);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(searchResponse.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(0);

            // 根据ID获取图片信息
            // Get image information based on ID
            ConcurrentHashMap<String, String> map = imageService.getMap();
            List<DataInfoRes> imageInfoResList = new ArrayList<>();
            for (SearchResultsWrapper.IDScore score : scores) {
                DataInfoRes imageInfoRes = new DataInfoRes();
                imageInfoRes.setScore(score.getScore());
                imageInfoRes.setId(score.getLongID());
                imageInfoRes.setUrl(baseUrl + map.get("" + score.getLongID()));
                imageInfoResList.add(imageInfoRes);
            }

            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }
}
