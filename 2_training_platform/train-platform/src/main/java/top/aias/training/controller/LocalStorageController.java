package top.aias.training.controller;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import top.aias.training.common.exception.BadRequestException;
import top.aias.training.config.FileProperties;
import top.aias.training.domain.LocalStorage;
import top.aias.training.domain.ResultBean;
import top.aias.training.service.LocalStorageService;
import top.aias.training.utils.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 存储服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;
    private final FileProperties properties;

    /**
     * 获取文件列表
     * @return
     */
    @GetMapping("/list")
    public ResultBean getContact() {
        List<LocalStorage> result = localStorageService.getStorageList();
        return ResultBean.success().add("result", result);
    }

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    @PostMapping("/file")
    public ResultBean create(@RequestParam("file") MultipartFile multipartFile) {
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()),
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );

            localStorageService.addStorageFile(localStorage);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
        return ResultBean.success();
    }

    /**
     * 删除文件
     * @param localStorage
     * @return
     */
    @DeleteMapping
    public ResultBean delete(@RequestBody LocalStorage localStorage) {
        LocalStorage storage = localStorageService.findById(localStorage.getId());
        FileUtil.del(storage.getPath());
        localStorageService.delete(localStorage.getId());
        return ResultBean.success();
    }

    /**
     * 提取特征
     * @param ids
     * @return
     */
    @PostMapping("/features")
    public ResponseEntity<Object> extractFeatures(@RequestBody Long[] ids) {
        Long[] idArr = ids;
//        localStorageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}