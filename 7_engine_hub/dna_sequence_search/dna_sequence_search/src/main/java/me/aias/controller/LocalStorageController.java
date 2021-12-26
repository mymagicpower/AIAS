package me.aias.controller;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.aias.common.exception.BadRequestException;
import me.aias.common.utils.FileUtils;
import me.aias.config.FileProperties;
import me.aias.domain.LocalStorage;
import me.aias.domain.ResultBean;
import me.aias.service.LocalStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 存储管理
 * @author Calvin
 * @date 2021-12-12
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "存储管理")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;
    private final FileProperties properties;

    @ApiOperation("查询文件列表")
    @GetMapping("/list")
    public ResultBean getContact() {
        List<LocalStorage> result = localStorageService.getStorageList();
        return ResultBean.success().add("result", result);
    }

    @ApiOperation("上传文件")
    @PostMapping("/file")
    public ResultBean create(@RequestParam("file") MultipartFile multipartFile) {
        FileUtils.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        File file = FileUtils.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    FileUtils.getFileNameNoEx(multipartFile.getOriginalFilename()),
                    suffix,
                    file.getPath(),
                    type,
                    FileUtils.getSize(multipartFile.getSize())
            );

            localStorageService.addStorageFile(localStorage);
        } catch (Exception e) {
            FileUtils.del(file);
            throw e;
        }
        return ResultBean.success();
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public ResultBean delete(@RequestBody LocalStorage localStorage) {
        LocalStorage storage = localStorageService.findById(localStorage.getId());
        FileUtils.del(storage.getPath());
        localStorageService.delete(localStorage.getId());
        return ResultBean.success();
    }
}