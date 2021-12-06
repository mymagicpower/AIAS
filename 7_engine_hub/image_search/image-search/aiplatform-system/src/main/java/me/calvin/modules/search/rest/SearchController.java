package me.calvin.modules.search.rest;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import com.google.common.collect.Lists;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.config.FileProperties;
import me.calvin.modules.search.common.constant.ImageConst;
import me.calvin.modules.search.common.utils.common.ImageUtil;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.SearchB64ImageReq;
import me.calvin.modules.search.domain.request.SearchB64ImagesReq;
import me.calvin.modules.search.domain.request.SearchUrlImageReq;
import me.calvin.modules.search.domain.request.SearchUrlImagesReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.FeatureService;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.SearchService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import me.calvin.service.dto.LocalStorageQueryCriteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.*;

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

    @Value("${newModel.enabled}")
    private boolean newModel;
    
    @Value("${search.faceCollectionName}")
    String faceCollectionName;

    @Value("${search.commCollectionName}")
    String commCollectionName;

    @Log("初始化向量引擎")
    @ApiOperation(value = "初始化向量引擎")
    @PutMapping(value = "/initSearchEngine")
    public ResponseEntity<Object> initSearchEngine() {
        try {
            searchService.initSearchEngine();
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @ApiOperation("查询文件")
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<Object> query(LocalStorageQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Object> create(@RequestParam String type, @RequestParam String topK, @RequestParam("file") MultipartFile file) {
        // 根据base64 生成向量
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(file);
        Long topk = Long.parseLong(topK);
        return singleImage(bufferedImage, topk, type);
    }

    @Log("搜索图片")
    @PostMapping(value = "/image")
    @ApiOperation(value = "搜索图片", nickname = "searchImage")
    public ResponseEntity<Object> searchImage(@RequestParam String type,
                                              @RequestParam("image") MultipartFile imageFile, @RequestParam(value = "topK") String topk) {
        // 根据base64 生成向量
        BufferedImage bufferedImage = ImageUtil.multipartFileToBufImage(imageFile);
        Long topK = Long.parseLong(topk);
        return singleImage(bufferedImage, topK, type);
    }

    @Log("搜索base64图片")
    @ApiOperation(value = "搜索base64图片")
    @PostMapping(value = "/base64")
    public ResponseEntity<Object> searchImageByBase64(@RequestBody SearchB64ImageReq searchB64ImageReq) {
        String base64ImgData = searchB64ImageReq.getBase64().split("base64,")[1]; // 图片数据
        // 根据base64 生成向量
        BufferedImage bufferedImage = ImageUtil.base64ToBufferedImage(base64ImgData);
        Long topK = Long.parseLong(searchB64ImageReq.getTopk());
        return singleImage(bufferedImage, topK, searchB64ImageReq.getType());
    }

    @Log("搜索一组base64图片")
    @ApiOperation(value = "搜索一组base64图片")
    @PostMapping(value = "/base64s")
    public ResponseEntity<Object> searchImagesByBase64(@RequestBody SearchB64ImagesReq searchB64ImagesReq) {
        Long topK = Long.parseLong(searchB64ImagesReq.getTopk());
        return batchImages(searchB64ImagesReq.getB64s(), topK, searchB64ImagesReq.getType());
    }

    @Log("根据图片url搜索")
    @ApiOperation(value = "根据图片url搜索")
    @PostMapping(value = "/url")
    public ResponseEntity<Object> searchImageByUrl(@RequestBody SearchUrlImageReq searchImageUrlReq) {
        // img 单图片字节数组
        byte[] bytes = ImageUtil.getImageByUrl(searchImageUrlReq.getUrl());
        BufferedImage bufferedImage = ImageUtil.bytesToBufferedImage(bytes);
        Long topK = Long.parseLong(searchImageUrlReq.getTopk());
        return singleImage(bufferedImage, topK, searchImageUrlReq.getType());
    }

    @Log("根据图片url列表搜索")
    @ApiOperation(value = "根据图片url列表搜索")
    @PostMapping(value = "/urls")
    public ResponseEntity<Object> searchImageByUrls(@RequestBody SearchUrlImagesReq searchUrlImagesReq) {
        // 将urls转换为图片
        Long topK = Long.parseLong(searchUrlImagesReq.getTopk());
        return batchImages(searchUrlImagesReq.getUrls(), topK, searchUrlImagesReq.getType());
    }

    private ResponseEntity<Object> singleImage(BufferedImage bufferedImage, Long topk, String type) {
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        List<Float> vectorToSearch = null;
        try {
            if (type.equals(ImageConst.COMMON)) {
                //特征提取
                if(this.newModel){
                    vectorToSearch = featureService.feature(properties.getPath().getNewModelPath(),img);
                }else{
                    vectorToSearch = featureService.commonFeature(img);
                }
            } else if (type.equals(ImageConst.FACE)) {
                vectorToSearch = featureService.faceFeature(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据图片向量搜索
            SearchResponse searchResponse = null;
            if (type.equals(ImageConst.COMMON)) {
                searchResponse = searchService.search(this.commCollectionName, topk, vectorsToSearch);
            } else if (type.equals(ImageConst.FACE)) {
                searchResponse = searchService.search(this.faceCollectionName, topk, vectorsToSearch);
            }

            List<List<Long>> resultIds = searchResponse.getResultIdsList();
            List<String> idList = Lists.transform(resultIds.get(0), (entity) -> {
                return entity.toString();
            });

            // 根据ID获取图片信息
            ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
            imageInfoQueryCriteria.setImageId(idList);
            List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);

            List<ImageInfoRes> imageInfoResList = new ArrayList<>();
            for (ImageInfoDto imageInfo : imageInfoList) {
                ImageInfoRes imageInfoRes = new ImageInfoRes();
                BeanUtils.copyProperties(imageInfo, imageInfoRes);
                Float score = maxScoreForImageId(searchResponse, imageInfo.getImageId());
                imageInfoRes.setScore(score);
                imageInfoRes.setId(imageInfo.getImageId());
                imageInfoResList.add(imageInfoRes);
            }

            return new ResponseEntity<>(ResultRes.success(imageInfoResList, imageInfoResList.size()), HttpStatus.OK);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }
    }

    private ResponseEntity<Object> batchImages(HashMap<String, String> hashMap, Long topk, String type) {
        Iterator iter = hashMap.entrySet().iterator();
        List<List<Float>> vectorsToSearch = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String id = (String) entry.getKey();
            idList.add(id);
            String value = (String) entry.getValue();
            Image img = null;
            BufferedImage bufferedImage = null;
            if (value.startsWith("data:image")) {
                // data:image/png;base64,base64编码 的png图片数据
                String base64ImgData = value.split("base64,")[1]; // 图片数据
                bufferedImage = ImageUtil.base64ToBufferedImage(base64ImgData);
            } else {
                byte[] bytes = ImageUtil.getImageByUrl(value);
                bufferedImage = ImageUtil.bytesToBufferedImage(bytes);
            }

            img = ImageFactory.getInstance().fromImage(bufferedImage);
            List<Float> vectorToSearch = null;
            try {
                if (type.equals(ImageConst.COMMON)) {
                    //特征提取
                    if(this.newModel){
                        vectorToSearch = featureService.feature(properties.getPath().getNewModelPath(),img);
                    }else{
                        vectorToSearch = featureService.commonFeature(img);
                    }
                } else if (type.equals(ImageConst.FACE)) {
                    vectorToSearch = featureService.faceFeature(img);
                }
                vectorsToSearch.add(vectorToSearch);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
            }
        }

        try {
            // 根据图片向量搜索
            SearchResponse searchResponse = null;
            if (type.equals(ImageConst.COMMON)) {
                searchResponse = searchService.search(this.commCollectionName, topk, vectorsToSearch);
            } else if (type.equals(ImageConst.FACE)) {
                searchResponse = searchService.search(this.faceCollectionName, topk, vectorsToSearch);
            }
            List<List<Long>> resultIds = searchResponse.getResultIdsList();

            HashMap<String, List<ImageInfoDto>> result = new HashMap<>();
            for (int i = 0; i < resultIds.size(); i++) {
                // 根据ID获取图片信息
                List<String> idQueryList = Lists.transform(resultIds.get(i), (entity) -> {
                    return entity.toString();
                });
                // 根据ID获取图片信息
                ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
                imageInfoQueryCriteria.setImageId(idQueryList);
                List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);

                result.put(idList.get(i), imageInfoList);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
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
