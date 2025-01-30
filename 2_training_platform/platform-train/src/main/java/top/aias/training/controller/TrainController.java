package top.aias.training.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.aias.training.common.constant.Constant;
import top.aias.training.common.enums.ResEnum;
import top.aias.training.common.utils.UUIDUtil;
import top.aias.training.common.utils.UserAgentUtil;
import top.aias.training.common.utils.ZipUtil;
import top.aias.training.config.FileProperties;
import top.aias.training.config.UIServerInstance;
import top.aias.training.domain.LocalStorage;
import top.aias.training.domain.ResultBean;
import top.aias.training.domain.TrainArgument;
import top.aias.training.service.LocalStorageService;
import top.aias.training.service.TrainArgumentService;
import top.aias.training.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 模型训练服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/train")
public class TrainController {

    private final FileProperties properties;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private TrainArgumentService trainArgumentService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private UIServerInstance uiServer;

    /**
     * 训练模型
     * @param storage
     * @param request
     * @return
     * @throws Exception
     */
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
        if (!new File(properties.getPath().getDataRootPath()).exists()) {
            new File(properties.getPath().getDataRootPath()).mkdirs();
        }
        //生成UUID作为解压缩的目录
        String UUID = UUIDUtil.getUUID();
        String unZipFilePath = properties.getPath().getDataRootPath() + UUID;
        if (!new File(unZipFilePath).exists()) {
            new File(unZipFilePath).mkdirs();
        }
        String file = localStorage.getPath();
        ZipUtil.unZipTrainingData(localStorage.getPath(), os, unZipFilePath);

//        String dataPath = imageRootPath + UUID + File.separator + "TRAIN";
//        String testPath = imageRootPath + UUID + File.separator + "VALIDATION";
        String fileRootPath = properties.getPath().getDataRootPath() + UUID;

//        if (os.toUpperCase().contains(Constant.PC_WINDOW_TYPE)) {
//            fileRootPath = fileRootPath + File.separator + localStorage.getName();
//        }

        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        String modelPath = properties.getPath().getModelPath();
        String savePath = properties.getPath().getSavePath();
        trainService.train(uiServer, trainArgument, modelPath, savePath, fileRootPath);

//        FileUtil.delete(unZipFilePath);
        return ResultBean.success();
    }
}