package top.aias.controller;

import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.domain.DataInfo;
import top.aias.domain.DataInfoRes;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultRes;
import top.aias.service.DataService;
import top.aias.service.FeatureService;
import top.aias.service.SearchService;

import java.io.InputStream;
import java.util.List;

/**
 * 搜索管理
 * Search management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Api(tags = "搜索管理 - Search management")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private DataService dataService;
    @Autowired
    private FeatureService featureService;

    @Value("${image.baseUrl}")
    String baseUrl;

    @PostMapping(value = "/image")
    @ApiOperation(value = "searchImage", nickname = "searchImage")
    public ResponseEntity<Object> searchImage(@RequestParam("image") MultipartFile imageFile, @RequestParam(value = "topK") String topk) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            Integer topK = Integer.parseInt(topk);
            //特征提取
            float[] vectorToSearch = featureService.imageFeature(image);
            // 根据图片特征向量搜索
            // Search by image vectors
            List<DataInfo> imageDataList = dataService.getImageList();
            List<DataInfoRes> searchResponse = searchService.search(baseUrl, imageDataList, topK, vectorToSearch);

            return new ResponseEntity<>(ResultRes.success(searchResponse, searchResponse.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }
    }
}
