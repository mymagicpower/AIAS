package me.aias.controller;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.aias.common.exception.BadRequestException;
import me.aias.common.utils.FileUtil;
import me.aias.config.FileProperties;
import me.aias.domain.LocalStorage;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultBean;
import me.aias.domain.ResultRes;
import me.aias.service.LocalStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> getContact() {
        List<LocalStorage> result = localStorageService.getStorageList();
        return new ResponseEntity<>(ResultRes.success(result, result.size()), HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping("/file")
    public ResponseEntity<Object> create(@RequestParam("file") MultipartFile multipartFile) {
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.UPLOAD_FAIL.KEY, ResEnum.UPLOAD_FAIL.VALUE), HttpStatus.OK);
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
            return new ResponseEntity<>(ResultRes.error(ResEnum.UPLOAD_FAIL.KEY, ResEnum.UPLOAD_FAIL.VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody LocalStorage localStorage) {
        LocalStorage storage = localStorageService.findById(localStorage.getId());
        FileUtil.del(storage.getPath());
        localStorageService.delete(localStorage.getId());
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}