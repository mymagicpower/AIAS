package me.calvin.modules.search.rest;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import com.alibaba.fastjson.JSON;
import io.milvus.client.ConnectFailedException;
import io.milvus.client.HasCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.config.FileProperties;
import me.calvin.exception.BadRequestException;
import me.calvin.modules.search.common.constant.Constant;
import me.calvin.modules.search.common.constant.ImageConst;
import me.calvin.modules.search.common.constant.ImageGroupConst;
import me.calvin.modules.search.common.utils.common.ZipUtil;
import me.calvin.modules.search.common.utils.common.UUIDUtil;
import me.calvin.modules.search.common.utils.common.UserAgentUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.ImageLog;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.*;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.*;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import me.calvin.modules.search.service.dto.ImageLogDto;
import me.calvin.service.LocalStorageService;
import me.calvin.service.dto.LocalStorageDto;
import me.calvin.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Calvin
 * @date 2021-02-17
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "图片管理")
@RequestMapping("/api/imageInfo")
public class ImageInfoController {
    private final FileProperties properties;
    private final ImageInfoService imageInfoService;

    @Value("${code.expiration}")
    private Long expiration;

    @Value("${newModel.enabled}")
    private boolean newModel;

    @Autowired
    private ImageInfoService imageService;

    @Autowired
    private DetectService detectService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ImageLogService imageLogService;

    @Autowired
    private LocalStorageService localStorageService;

    private final RedisUtils redisUtils;

    @Value("${search.faceDimension}")
    String faceDimension;

    @Value("${search.faceCollectionName}")
    String faceCollectionName;

    @Value("${search.commDimension}")
    String commDimension;

    @Value("${search.commCollectionName}")
    String commCollectionName;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('imageInfo:list')")
    public void download(HttpServletResponse response, ImageInfoQueryCriteria criteria) throws IOException {
        imageInfoService.download(imageInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ImageInfoService")
    @ApiOperation("查询ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:list')")
    public ResponseEntity<Object> query(ImageInfoQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(imageInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ImageInfoService")
    @ApiOperation("新增ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ImageInfo resources) {
        return new ResponseEntity<>(imageInfoService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ImageInfoService")
    @ApiOperation("修改ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ImageInfo resources) {
        imageInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ImageInfoService")
    @ApiOperation("删除ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        imageInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("上传base64格式图片")
    @ApiOperation(value = "上传base64格式图片")
    @PostMapping(value = "/base64")
    public ResponseEntity<Object> uploadBase64Image(@RequestBody B64ImageReq b64ImageReq) throws IOException {
        ImageInfo imageInfo = imageService.uploadBase64Image(b64ImageReq.getBase64(), properties.getPath().getImageRootPath());
        return singleImage(imageInfo, b64ImageReq.getType());
    }

    @Log("上传一组base64格式图片")
    @ApiOperation(value = "上传一组base64格式图片")
    @PostMapping(value = "/base64s")
    public ResponseEntity<Object> uploadBase64Images(@RequestBody B64ImagesReq b64ImagesReq) throws IOException {
        // [{"b64s":{"1":"a","2":"b","3":"c","4":"d"},"base64":"test"}]
        List<ImageInfo> imageInfoList =
                imageService.uploadBase64Images(b64ImagesReq.getB64s(), properties.getPath().getImageRootPath());
        return batchImages(null, imageInfoList, b64ImagesReq.getType());
    }

    @Log("上传url图片")
    @ApiOperation(value = "上传url图片")
    @PostMapping(value = "/url")
    public ResponseEntity<Object> uploadImageByUrl(@RequestBody UrlImageReq urlImageReq) throws IOException { // 0 不存，1 存盘
        ImageInfo imageInfo =
                imageService.uploadImageByUrl(urlImageReq.getUrl(), properties.getPath().getImageRootPath(), urlImageReq.getSave());
        return singleImage(imageInfo, urlImageReq.getType());
    }

    @Log("上传一组url图片")
    @ApiOperation(value = "上传一组url图片")
    @PostMapping(value = "/urls")
    public ResponseEntity<Object> uploadImageByUrls(@RequestBody UrlImagesReq urlImagesReq) throws IOException {
        List<ImageInfo> imageInfoList =
                imageService.uploadImageByUrls(urlImagesReq.getUrls(), properties.getPath().getImageRootPath(), urlImagesReq.getSave());
        return batchImages(null, imageInfoList, urlImagesReq.getType());
    }

    @Log("上传图片文件")
    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/uploadImage")
    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile imageFile, @RequestParam("type") String type) throws IOException {
        ImageInfo imageInfo = imageService.uploadImage(imageFile, properties.getPath().getImageRootPath());
        return singleImage(imageInfo, type);
    }

    @Log("zip包解压缩并提取特征值")
    @ApiOperation(value = "上传zip格式图片压缩包")
    @PostMapping(value = "/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestBody ExtractFeatureReq extractFeatureReq, HttpServletRequest request) throws IOException {

        LocalStorageDto localStorageDto = localStorageService.findById(Long.parseLong(extractFeatureReq.getId()));

        String suffix = localStorageDto.getSuffix();
        if (!Constant.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.PACKAGE_FILE_FAIL.KEY, ResEnum.PACKAGE_FILE_FAIL.VALUE), HttpStatus.OK);
        }

        // 获取上传者操作系统
        UserAgentUtil userAgentGetter = new UserAgentUtil(request);
        String os = userAgentGetter.getOS();

        // 解压缩,以压缩文件名为新目录,文件名乱码可能是个问题
        if (!new File(properties.getPath().getImageRootPath()).exists()) {
            new File(properties.getPath().getImageRootPath()).mkdirs();
        }
        //生成UUID作为解压缩的目录
        String UUID = UUIDUtil.getUUID();
        String unZipFilePath = properties.getPath().getImageRootPath() + UUID;
        if (!new File(unZipFilePath).exists()) {
            new File(unZipFilePath).mkdirs();
        }
        ZipUtil.unZip(localStorageDto.getPath(), os, unZipFilePath);

        List<ImageInfo> imageInfoList = imageService.uploadImages(properties.getPath().getImageRootPath(), UUID);

        // 保存zip上传结果
        ImageLog imageLog = new ImageLog();
        imageLog.setFileName(localStorageDto.getName());
        String jsonStr = JSON.toJSONString(imageInfoList); // List转json
        imageLog.setImageList(jsonStr);
        imageLog.setStorageId(Long.parseLong(extractFeatureReq.getId()));
        imageLog.setCreateTime(new Timestamp(new Date().getTime()));
        imageLog.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
        ImageLogDto imageLogDto = imageLogService.create(imageLog);

//        // 读取zip上传结果
//        ImageLogDto imageLog = imageLogService.findById(Long.parseLong(imageLogReq.getId()));
//        // json字符串转List
//        List<ImageInfo> imageInfoList = JSON.parseArray(imageLog.getImageList(), ImageInfo.class);
//        return new ResponseEntity<>(imageLogDto, HttpStatus.OK);

        //提取特征
        batchImages(Long.parseLong(extractFeatureReq.getId()), imageInfoList, extractFeatureReq.getType());

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("根据图片UUID查询信息")
    @ApiOperation(value = "根据图片UUID查询信息")
    @GetMapping(value = "/queryImageInfoByUuid/{uuid}")
    public ResponseEntity<Object> queryImageInfoByUuid(@PathVariable String uuid) {
        // 根据ID获取图片信息
        ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
        imageInfoQueryCriteria.setUuid(uuid);
        List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);
        ImageInfoDto imageInfo = imageInfoList.get(0);
        return new ResponseEntity<>(imageInfo, HttpStatus.OK);
    }

    private ResponseEntity<Object> singleImage(ImageInfo imageInfo, String type) {
        ImageInfoDto imageInfoDto = null;
        List<Float> feature = null;
        try {
            if (type.equals(ImageConst.COMMON)) {
                Image img = ImageFactory.getInstance().fromUrl(imageInfo.getFullPath());
                //特征提取
                if (this.newModel) {
                    feature = featureService.feature(properties.getPath().getNewModelPath(), img);
                } else {
                    feature = featureService.commonFeature(img);
                }
            } else if (type.equals(ImageConst.FACE)) {
                //人脸检测 & 特征提取
                imageInfoDto = detectService.faceDetect(imageInfo);
                String jsonStr = JSON.toJSONString(imageInfoDto.getFaceObjects());
                imageInfo.setDetectObjs(jsonStr);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
        imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
        // 图片信息入库
        ImageInfoDto newImageInfoDto = imageService.create(imageInfo);

        //向量插入向量引擎
        try {
            if (type.equals(ImageConst.FACE)) {
                newImageInfoDto.setFaceObjects(imageInfoDto.getFaceObjects());
                HasCollectionResponse response = searchService.hasCollection(this.faceCollectionName);
                if (!response.hasCollection()) {
                    searchService.createCollection(this.faceCollectionName, Long.parseLong(this.faceDimension));
                    searchService.createIndex(this.faceCollectionName);
                }
                searchService.insertVectors(this.faceCollectionName, newImageInfoDto);
            } else if (type.equals(ImageConst.COMMON)) {
                HasCollectionResponse response = searchService.hasCollection(this.commCollectionName);
                if (!response.hasCollection()) {
                    searchService.createCollection(this.commCollectionName, Long.parseLong(this.commDimension));
                    searchService.createIndex(this.commCollectionName);
                }
                searchService.insertVectors(this.commCollectionName, newImageInfoDto.getImageId(), feature);
            }
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        ImageInfoRes imageInfoRes = new ImageInfoRes();
        BeanUtils.copyProperties(imageInfo, imageInfoRes);
        if (type.equals(ImageConst.FACE)) {
            imageInfoRes.setFaces(imageInfoDto.getFaceObjects());
        }
        return new ResponseEntity<>(imageInfoRes, HttpStatus.OK);
    }

    private ResponseEntity<Object> batchImages(Long id, List<ImageInfo> imageInfoList, String type) {
        List<ImageInfoDto> imageInfoDtoList = new ArrayList<>();
        ImageInfoDto imageInfoDto = null;
        List<Long> vectorIds = new ArrayList<>();
        List<List<Float>> vectors = new ArrayList<>();

        String redisKey = "COUNTER::" + id;
        int index = 0;
        for (ImageInfo imageInfo : imageInfoList) {
            List<Float> feature = null;
            try {
                if (type.equals(ImageConst.COMMON)) {
                    Image img = ImageFactory.getInstance().fromUrl(imageInfo.getFullPath());
                    //特征提取
                    if (this.newModel) {
                        feature = featureService.feature(properties.getPath().getNewModelPath(), img);
                    } else {
                        feature = featureService.commonFeature(img);
                    }
                } else if (type.equals(ImageConst.FACE)) {
                    //人脸检测 & 特征提取
                    imageInfoDto = detectService.faceDetect(imageInfo);
                    String jsonStr = JSON.toJSONString(imageInfoDto.getFaceObjects());
                    imageInfo.setDetectObjs(jsonStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
            }
            imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
            imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
            // 图片信息入库
            ImageInfoDto newImageInfoDto = imageService.create(imageInfo);
            if (type.equals(ImageConst.FACE)) {
                newImageInfoDto.setFaceObjects(imageInfoDto.getFaceObjects());
                imageInfoDtoList.add(newImageInfoDto);
            } else if (type.equals(ImageConst.COMMON)) {
                vectorIds.add(newImageInfoDto.getImageId());
                vectors.add(feature);
            }

            index = index + 1;
            // 存入redis缓存
            String status = index + " / " + imageInfoList.size();
            boolean result = redisUtils.set(redisKey, status, expiration);
            if (!result) {
                throw new BadRequestException("Redis服务异常");
            }
        }

        try {

            if (type.equals(ImageConst.FACE)) {
                HasCollectionResponse response = searchService.hasCollection(this.faceCollectionName);
                if (!response.hasCollection()) {
                    searchService.createCollection(this.faceCollectionName, Long.parseLong(this.faceDimension));
                    searchService.createIndex(this.faceCollectionName);
                }
                searchService.insertVectors(this.faceCollectionName, imageInfoDtoList);
            } else if (type.equals(ImageConst.COMMON)) {
                HasCollectionResponse response = searchService.hasCollection(this.commCollectionName);
                if (!response.hasCollection()) {
                    searchService.createCollection(this.commCollectionName, Long.parseLong(this.commDimension));
                    searchService.createIndex(this.commCollectionName);
                }
                searchService.insertVectors(this.commCollectionName, vectorIds, vectors);
            }

        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}