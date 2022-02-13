package me.calvin.modules.search.rest;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import com.google.common.collect.Lists;
import io.milvus.Response.SearchResultsWrapper;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.modules.search.common.utils.common.ImageUtil;
import me.calvin.modules.search.common.face.FaceObject;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.SearchB64ImageReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.DetectService;
import me.calvin.modules.search.service.FeatureService;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.SearchService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "搜索管理")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private DetectService detectService;

    @ApiOperation("搜索图片")
    @PostMapping
    public ResponseEntity<Object> create(@RequestParam String topK, @RequestParam("file") MultipartFile file) {
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(file);
        Integer topk = Integer.parseInt(topK);
        return searchImage(bufferedImage, topk);
    }

    @Log("搜索图片")
    @PostMapping(value = "/image")
    @ApiOperation(value = "搜索图片", nickname = "searchImage")
    public ResponseEntity<Object> searchImage(@RequestParam("image") MultipartFile imageFile, @RequestParam(value = "topK") String topk) {
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(imageFile);
        Integer topK = Integer.parseInt(topk);
        return searchImage(bufferedImage, topK);
    }

    @Log("搜索base64图片")
    @ApiOperation(value = "搜索base64图片")
    @PostMapping(value = "/base64")
    public ResponseEntity<Object> searchImageByBase64(@RequestBody SearchB64ImageReq searchB64ImageReq) {
        String base64ImgData = searchB64ImageReq.getBase64().split("base64,")[1]; // 图片数据
        // 根据base64 生成向量
        BufferedImage bufferedImage = ImageUtil.base64ToBufferedImage(base64ImgData);
        Integer topK = Integer.parseInt(searchB64ImageReq.getTopk());
        return searchImage(bufferedImage, topK);
    }

    private ResponseEntity<Object> searchImage(BufferedImage bufferedImage, Integer topk) {
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        List<Float> vectorToSearch;
        try {
            //人脸检测 & 特征提取
            List<FaceObject> faceObjects = detectService.faceDetect("", img);
            //如何有多个人脸，取第一个（也可以选最大的，或者一起送入搜索引擎搜索）
            vectorToSearch = faceObjects.get(0).getFeature();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据图片向量搜索
            R<SearchResults> searchResponse = searchService.search(topk, vectorsToSearch);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(searchResponse.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(0);
            List<Long> idList = Lists.transform(scores, (entity) -> {
                return entity.getLongID();
            });

            // 根据ID获取图片信息
            ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
            imageInfoQueryCriteria.setImageId(idList);
            List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);

            List<ImageInfoRes> imageInfoResList = new ArrayList<>();
            for (ImageInfoDto imageInfo : imageInfoList) {
                ImageInfoRes imageInfoRes = new ImageInfoRes();
                BeanUtils.copyProperties(imageInfo, imageInfoRes);
                Float score = maxScoreForImageId(scores, imageInfo.getImageId());
                imageInfoRes.setScore(score);
                imageInfoRes.setId(imageInfo.getImageId());
                imageInfoResList.add(imageInfoRes);
            }
            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private Float maxScoreForImageId(List<SearchResultsWrapper.IDScore> scores, Long imageId) {
        float maxScore = -1;
        for (SearchResultsWrapper.IDScore score : scores) {
            if (score.getLongID() == imageId.longValue()) {
                if (score.getScore() > maxScore) {
                    maxScore = score.getScore();
                }
            }
        }
        return maxScore;
    }
}
