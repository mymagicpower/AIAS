package me.calvin.modules.search.rest;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.google.common.collect.Lists;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.response.SearchResultsWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.modules.search.common.exception.FaceNotFoundException;
import me.calvin.modules.search.domain.FaceObject;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.SearchImageReq;
import me.calvin.modules.search.domain.request.SearchUrlImageReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.DetectService;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.SearchService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 图片搜索管理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
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
    private ImageInfoService imageInfoService;
    @Autowired
    private DetectService detectService;

    @Value("${image.baseUrl}")
    String baseUrl;

    @Value("${image.thumbnailUrl}")
    String thumbnailUrl;

    @Log("重置向量引擎")
    @ApiOperation(value = "重置向量引擎")
    @PutMapping(value = "/clearSearchEngine")
    public ResponseEntity<Object> clearSearchEngine() {
        searchService.clearSearchEngine();
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("C端搜索图片")
    @PostMapping(value = "/simpleSearch")
    @ApiOperation(value = "搜索图片", nickname = "simpleSearch")
    public ResponseEntity<Object> simpleSearch(SearchImageReq searchImageReq) {
        try {
            Image img = OpenCVImageFactory.getInstance().fromInputStream(searchImageReq.getFile().getInputStream());
            Integer topk = searchImageReq.getTopK();
            Integer currentTotal = searchImageReq.getTotal();
            List<ImageInfoRes> imageInfoResList = singleImage(img, topk);
            if (imageInfoResList.size() > currentTotal) {
                imageInfoResList = imageInfoResList.subList(currentTotal, imageInfoResList.size());
            }else{
                imageInfoResList = new ArrayList<>();
            }
            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ModelException | TranslateException | IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    @Log("管理端搜索图片")
    @PostMapping(value = "/image")
    @ApiOperation(value = "搜索图片", nickname = "searchImage")
    public ResponseEntity<Object> searchImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "topK") String topK) {
        try {
            Image img = OpenCVImageFactory.getInstance().fromInputStream(file.getInputStream());
            Integer topk = Integer.parseInt(topK);
            List<ImageInfoRes> imageInfoResList = singleImage(img, topk);
            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ModelException | TranslateException | IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    @Log("根据图片url搜索")
    @ApiOperation(value = "根据图片url搜索")
    @PostMapping(value = "/url")
    public ResponseEntity<Object> searchImageByUrl(@RequestBody SearchUrlImageReq searchImageUrlReq) {
        try {
            Image img = OpenCVImageFactory.getInstance().fromUrl(searchImageUrlReq.getUrl());
            Integer topk = Integer.parseInt(searchImageUrlReq.getTopk());
            List<ImageInfoRes> imageInfoResList = singleImage(img, topk);
            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ModelException | TranslateException | IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private List<ImageInfoRes> singleImage(Image img, Integer topk) throws ModelException, TranslateException, IOException, FaceNotFoundException {
        List<Float> vectorToSearch = null;
        try {
            //人脸检测 & 特征提取
            List<FaceObject> faceObjects = detectService.faceDetect(img);
            //如何有多个人脸，取第一个（也可以选最大的，或者一起送入搜索引擎搜索）
            vectorToSearch = faceObjects.get(0).getFeature();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new FaceNotFoundException(e.getMessage());
        }
        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

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
        for (int i = 0; i < idList.size(); i++) {
            for (ImageInfoDto imageInfo : imageInfoList) {
                if (idList.get(i).equals(imageInfo.getImageId())) {
                    ImageInfoRes imageInfoRes = new ImageInfoRes();
                    imageInfoRes.setScore(scores.get(i).getScore());
                    imageInfoRes.setId(scores.get(i).getLongID());
                    imageInfoRes.setCreateTime(imageInfo.getCreateTime());
                    imageInfoRes.setPreName(imageInfo.getPreName());
                    imageInfoRes.setImgUrl(baseUrl + imageInfo.getImgUri());
                    imageInfoRes.setThumbnailUrl(thumbnailUrl + imageInfo.getImgUri());
                    imageInfoResList.add(imageInfoRes);
                    break;
                }
            }
        }
        return imageInfoResList;

    }
}
