package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.constant.Constant;
import me.aias.common.utils.UUIDUtil;
import me.aias.common.utils.UserAgentUtil;
import me.aias.common.utils.ZipUtil;
import me.aias.config.FileProperties;
import me.aias.domain.DataInfo;
import me.aias.domain.LocalStorage;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultRes;
import me.aias.service.DataService;
import me.aias.service.FeatureService;
import me.aias.service.LocalStorageService;
import me.aias.service.SearchService;
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
 * 视频管理
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
    private FeatureService featureService;

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

        // 抓取图像画面
        try {
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            for (DataInfo dataInfo : dataInfoList) {
                Path imageFile = Paths.get(dataInfo.getFullPath());
                Image image = ImageFactory.getInstance().fromFile(imageFile);
                List<Float> feature = featureService.imageFeature(image);
                // 保存文件信息
                ConcurrentHashMap<String, String> map = dataService.getMap();
                int size = map.size();
                dataService.addData(String.valueOf(size + 1), dataInfo.getRelativePath());
                vectorIds.add(Long.valueOf(size + 1));
                vectors.add(feature);
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