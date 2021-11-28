package me.aias.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.constant.Constant;
import me.aias.common.enums.ResEnum;
import me.aias.common.utils.UUIDUtil;
import me.aias.common.utils.UserAgentUtil;
import me.aias.common.utils.ZipUtil;
import me.aias.config.FileProperties;
import me.aias.domain.LocalStorage;
import me.aias.domain.ResultBean;
import me.aias.domain.TrainArgument;
import me.aias.service.LocalStorageService;
import me.aias.service.TrainArgumentService;
import me.aias.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "模型训练管理")
@RequestMapping("/api/train")
public class TrainController {

    private final FileProperties properties;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private TrainArgumentService trainArgumentService;

    @Autowired
    private TrainService trainService;

    @ApiOperation(value = "训练模型")
    @PostMapping(value = "/trigger")
    public ResultBean extractFeatures(@RequestBody LocalStorage storage, HttpServletRequest request) throws Exception {

        int id = storage.getId();
        LocalStorage localStorage = localStorageService.findById(id);
        String suffix = localStorage.getSuffix();
        if (!Constant.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
            return ResultBean.failure().add(ResEnum.ZIP_FILE_FAIL.KEY, ResEnum.ZIP_FILE_FAIL.VALUE);
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
        String file = localStorage.getPath();
        ZipUtil.unZipTrainingData(localStorage.getPath(), os, unZipFilePath);

//        String dataPath = imageRootPath + UUID + File.separator + "TRAIN";
//        String testPath = imageRootPath + UUID + File.separator + "VALIDATION";
        String fileRootPath = properties.getPath().getImageRootPath() + UUID;

//        if (os.toUpperCase().contains(Constant.PC_WINDOW_TYPE)) {
//            fileRootPath = fileRootPath + File.separator + localStorage.getName();
//        }

        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        trainService.train(trainArgument, properties.getPath().getNewModelPath(), fileRootPath);

//        FileUtil.delete(unZipFilePath);
        return ResultBean.success();
    }
}