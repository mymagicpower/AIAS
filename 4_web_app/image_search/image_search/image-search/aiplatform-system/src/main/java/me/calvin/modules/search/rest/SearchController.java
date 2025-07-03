package me.calvin.modules.search.rest;

import ai.djl.ModelException;
import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
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
import me.calvin.config.FileProperties;
import me.calvin.modules.search.common.utils.ImageUtil;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.SearchB64ImageReq;
import me.calvin.modules.search.domain.request.SearchImageReq;
import me.calvin.modules.search.domain.request.SearchUrlImageReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.FeatureService;
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

import java.awt.image.BufferedImage;
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
    private final FileProperties properties;

    @Autowired
    private SearchService searchService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private FeatureService featureService;

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
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(searchImageReq.getFile());
        Integer topk = searchImageReq.getTopK();
        Integer currentTotal = searchImageReq.getTotal();
        try {
            List<ImageInfoRes> imageInfoResList = singleImage(bufferedImage, topk);
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
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(file);
        Integer topk = Integer.parseInt(topK);
        try {
            List<ImageInfoRes> imageInfoResList = singleImage(bufferedImage, topk);
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
        // img 单图片字节数组
        byte[] bytes = ImageUtil.getImageByUrl(searchImageUrlReq.getUrl());
        BufferedImage bufferedImage = ImageUtil.bytesToBufferedImage(bytes);
        Integer topk = Integer.parseInt(searchImageUrlReq.getTopk());
        try {
            List<ImageInfoRes> imageInfoResList = singleImage(bufferedImage, topk);
            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ModelException | TranslateException | IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private List<ImageInfoRes> singleImage(BufferedImage bufferedImage, Integer topk) throws ModelException, TranslateException, IOException {
        ImageFactory imageFactory = new BufferedImageFactory();
        Image img = imageFactory.fromImage(bufferedImage);

        //特征提取
        List<Float> vectorToSearch = featureService.imageFeature(img);

        // 根据图片向量搜索
        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);
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
