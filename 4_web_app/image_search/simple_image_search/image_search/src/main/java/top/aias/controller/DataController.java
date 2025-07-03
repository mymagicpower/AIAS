package top.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aias.common.constant.Constants;
import top.aias.common.utils.UUIDUtils;
import top.aias.common.utils.UserAgentUtils;
import top.aias.common.utils.ZipUtils;
import top.aias.config.FileProperties;
import top.aias.domain.DataInfo;
import top.aias.domain.LocalStorage;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultRes;
import top.aias.service.DataService;
import top.aias.service.FeatureService;
import top.aias.service.LocalStorageService;
import top.aias.service.SearchService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 数据管理
 * Data management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "数据管理 - Data Management")
@RequestMapping("/api/data")
public class DataController {
    private final FileProperties properties;

    @Autowired
    private DataService dataService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private LocalStorageService localStorageService;

    @ApiOperation(value = "提取特征 - Extract Features")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id, HttpServletRequest request) throws IOException {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String suffix = localStorage.getSuffix();
        if (!Constants.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.PACKAGE_FILE_FAIL.KEY, ResEnum.PACKAGE_FILE_FAIL.VALUE), HttpStatus.OK);
        }

        UserAgentUtils userAgentGetter = new UserAgentUtils(request);
        String os = userAgentGetter.getOS();

        if (!new File(properties.getPath().getRootPath()).exists()) {
            new File(properties.getPath().getRootPath()).mkdirs();
        }
        //生成UUID作为解压缩的目录
        // Generate UUID as the directory for unzipping
        String UUID = UUIDUtils.getUUID();
        String unZipFilePath = properties.getPath().getRootPath() + UUID;
        if (!new File(unZipFilePath).exists()) {
            new File(unZipFilePath).mkdirs();
        }
        ZipUtils.unZip(localStorage.getPath(), os, unZipFilePath);

        //生成提取的图片目录
        // Generate the directory for the extracted image
        String imagesPath = properties.getPath().getRootPath();
        if (!new File(imagesPath).exists()) {
            new File(imagesPath).mkdirs();
        }

        List<DataInfo> dataInfoList = dataService.uploadData(properties.getPath().getRootPath(), UUID);

        // 提取图片特征
        try {
            for (DataInfo dataInfo : dataInfoList) {
                Path imageFile = Paths.get(dataInfo.getFullPath());
                Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
                float[] feature = featureService.imageFeature(image);
                dataInfo.setFeature(feature);
            }

            // 保存数据
            dataService.addData(dataInfoList);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}