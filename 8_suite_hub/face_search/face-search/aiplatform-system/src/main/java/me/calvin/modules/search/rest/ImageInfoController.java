package me.calvin.modules.search.rest;

import com.alibaba.fastjson.JSON;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.config.FileProperties;
import me.calvin.exception.BadRequestException;
import me.calvin.modules.search.common.constant.Constants;
import me.calvin.modules.search.common.utils.common.UUIDUtil;
import me.calvin.modules.search.common.utils.common.UserAgentUtil;
import me.calvin.modules.search.common.utils.common.ZipUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.SimpleFaceObject;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.B64ImageReq;
import me.calvin.modules.search.domain.request.ExtractFeatureReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.service.DetectService;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.SearchService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
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

    @Autowired
    private ImageInfoService imageService;

    @Autowired
    private DetectService detectService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocalStorageService localStorageService;

    private final RedisUtils redisUtils;

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
        return singleImage(imageInfo);
    }

    @Log("上传图片文件")
    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/uploadImage")
    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        ImageInfo imageInfo = imageService.uploadImage(imageFile, properties.getPath().getImageRootPath());
        return singleImage(imageInfo);
    }

    @Log("zip包解压缩并提取特征值")
    @ApiOperation(value = "上传zip格式图片压缩包")
    @PostMapping(value = "/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestBody ExtractFeatureReq extractFeatureReq, HttpServletRequest request) throws IOException {

        LocalStorageDto localStorageDto = localStorageService.findById(Long.parseLong(extractFeatureReq.getId()));

        String suffix = localStorageDto.getSuffix();
        if (!Constants.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
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

        //提取特征
        batchImages(Long.parseLong(extractFeatureReq.getId()), imageInfoList);

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("根据图片UUID查询信息")
    @ApiOperation(value = "根据图片UUID查询信息")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<Object> queryImageInfoByUuid(@PathVariable String uuid) {
        // 根据ID获取图片信息
        ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
        imageInfoQueryCriteria.setUuid(uuid);
        List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);
        ImageInfoDto imageInfo = imageInfoList.get(0);
        return new ResponseEntity<>(imageInfo, HttpStatus.OK);
    }

    private ResponseEntity<Object> singleImage(ImageInfo imageInfo) {
        ImageInfoDto imageInfoDto;
        try {
            //人脸检测 & 特征提取
            imageInfoDto = detectService.faceDetect(imageInfo);
            String jsonStr = JSON.toJSONString(imageInfoDto.getFaceObjects());
            imageInfo.setDetectObjs(jsonStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }

        imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
        imageInfo.setCreateBy(Constants.DEFAULT_OPERATE_PERSON);
        // 图片信息入库
        ImageInfoDto newImageInfoDto = imageService.create(imageInfo);

        //向量插入向量引擎
        try {
            newImageInfoDto.setFaceObjects(imageInfoDto.getFaceObjects());
            R<Boolean> response = searchService.hasCollection();
            if (!response.getData()) {
                searchService.initSearchEngine();
            }
            //插入向量引擎
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            for (SimpleFaceObject faceObject : newImageInfoDto.getFaceObjects()) {
                vectorIds.add(newImageInfoDto.getImageId()); // 同一个大图检测到的人脸图使用同一个大图的id
                vectors.add(faceObject.getFeature());
            }

            searchService.insert(vectorIds, vectors);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        ImageInfoRes imageInfoRes = new ImageInfoRes();
        BeanUtils.copyProperties(imageInfo, imageInfoRes);
        imageInfoRes.setFaces(imageInfoDto.getFaceObjects());
        return new ResponseEntity<>(imageInfoRes, HttpStatus.OK);
    }

    private ResponseEntity<Object> batchImages(Long id, List<ImageInfo> imageInfoList) {
        List<ImageInfoDto> imageInfoDtoList = new ArrayList<>();
        ImageInfoDto imageInfoDto;
        String redisKey = "COUNTER::" + id;
        int index = 0;
        for (ImageInfo imageInfo : imageInfoList) {
            try {
                //人脸检测 & 特征提取
                imageInfoDto = detectService.faceDetect(imageInfo);
                String jsonStr = JSON.toJSONString(imageInfoDto.getFaceObjects());
                imageInfo.setDetectObjs(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
            }
            imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
            imageInfo.setCreateBy(Constants.DEFAULT_OPERATE_PERSON);
            // 图片信息入库
            ImageInfoDto newImageInfoDto = imageService.create(imageInfo);
            newImageInfoDto.setFaceObjects(imageInfoDto.getFaceObjects());
            imageInfoDtoList.add(newImageInfoDto);
            index = index + 1;
            // 存入redis缓存
            String status = index + " / " + imageInfoList.size();
            boolean result = redisUtils.set(redisKey, status, expiration);
            if (!result) {
                throw new BadRequestException("Redis服务异常");
            }
        }

        try {
            R<Boolean> response = searchService.hasCollection();
            if (!response.getData()) {
                searchService.initSearchEngine();
            }

            //插入向量引擎
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            for (ImageInfoDto imageInfo : imageInfoDtoList) {
                for (SimpleFaceObject faceObject : imageInfo.getFaceObjects()) {
                    vectorIds.add(imageInfo.getImageId()); // 同一个大图检测到的人脸图使用同一个大图的id
                    vectors.add(faceObject.getFeature());
                }
            }
            searchService.insert(vectorIds, vectors);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}