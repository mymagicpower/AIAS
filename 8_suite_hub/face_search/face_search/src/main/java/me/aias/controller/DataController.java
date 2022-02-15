package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.constant.Constant;
import me.aias.common.face.FaceObject;
import me.aias.common.utils.common.UUIDUtil;
import me.aias.common.utils.common.UserAgentUtil;
import me.aias.common.utils.common.ZipUtil;
import me.aias.config.FileProperties;
import me.aias.domain.*;
import me.aias.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据管理
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "数据管理")
@RequestMapping("/api/data")
public class DataController {
    private final FileProperties properties;

    @Autowired
    private DataService dataService;

    @Autowired
    private DetectService detectService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocalStorageService localStorageService;

    @ApiOperation(value = "提取特征")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id, HttpServletRequest request) throws IOException {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String suffix = localStorage.getSuffix();
        if (!Constant.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.PACKAGE_FILE_FAIL.KEY, ResEnum.PACKAGE_FILE_FAIL.VALUE), HttpStatus.OK);
        }

        // 获取上传者操作系统
        UserAgentUtil userAgentGetter = new UserAgentUtil(request);
        String os = userAgentGetter.getOS();

        if (!new File(properties.getPath().getRootPath()).exists()) {
            new File(properties.getPath().getRootPath()).mkdirs();
        }
        //生成UUID作为解压缩的目录
        String UUID = UUIDUtil.getUUID();
        String unZipFilePath = properties.getPath().getRootPath() + UUID;
        if (!new File(unZipFilePath).exists()) {
            new File(unZipFilePath).mkdirs();
        }
        ZipUtil.unZip(localStorage.getPath(), os, unZipFilePath);

        //生成视频文件提取的图片帧目录
        String imagesPath = properties.getPath().getRootPath();
        if (!new File(imagesPath).exists()) {
            new File(imagesPath).mkdirs();
        }

        List<DataInfo> dataInfoList = dataService.uploadData(properties.getPath().getRootPath(), UUID);
        try {
            List<SimpleFaceObject> faceList = new ArrayList<>();
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            for (DataInfo dataInfo : dataInfoList) {
                // 保存图片信息
                ConcurrentHashMap<String, String> map = dataService.getMap();
                int size = map.size();
                long imageId = size + 1;
                dataService.addData(String.valueOf(imageId), dataInfo.getRelativePath());

                Path imageFile = Paths.get(dataInfo.getFullPath());
                Image image = ImageFactory.getInstance().fromFile(imageFile);
                List<FaceObject> faceObjects = detectService.faceDetect("" + imageId, image);
                for (FaceObject faceObject : faceObjects) {
                    Rectangle rect = faceObject.getBoundingBox().getBounds();
                    SimpleFaceObject faceDTO = new SimpleFaceObject();
                    faceDTO.setScore(faceObject.getScore());
                    faceDTO.setFeature(faceObject.getFeature());
                    faceDTO.setX((int) rect.getX());
                    faceDTO.setY((int) rect.getY());
                    faceDTO.setWidth((int) rect.getWidth());
                    faceDTO.setHeight((int) rect.getHeight());
                    faceList.add(faceDTO);

                    vectorIds.add(imageId);
                    vectors.add(faceObject.getFeature());
                }
            }

            // 将向量插入Milvus向量引擎
            try {
                R<Boolean> response = searchService.hasCollection();
                if (!response.getData()) {
                    searchService.initSearchEngine();
                }
                searchService.insert(vectorIds, vectors);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}