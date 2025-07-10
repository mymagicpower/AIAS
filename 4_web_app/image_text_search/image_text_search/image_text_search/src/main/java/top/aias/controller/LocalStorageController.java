package top.aias.controller;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import top.aias.common.utils.FileUtils;
import top.aias.config.FileProperties;
import top.aias.domain.LocalStorage;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultRes;
import top.aias.service.LocalStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 存储管理
 * Storage Management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "存储管理 - Storage management")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;
    private final FileProperties properties;

    @ApiOperation("查询文件列表 - Query file list")
    @GetMapping("/list")
    public ResponseEntity<Object> getContact() {
        List<LocalStorage> result = localStorageService.getStorageList();
        return new ResponseEntity<>(ResultRes.success(result, result.size()), HttpStatus.OK);
    }

    @ApiOperation("上传文件 - Upload file")
    @PostMapping("/file")
    public ResponseEntity<Object> create(@RequestParam("file") MultipartFile multipartFile) {
        FileUtils.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        File file = FileUtils.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.UPLOAD_FAIL.KEY, ResEnum.UPLOAD_FAIL.VALUE), HttpStatus.OK);
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
            return new ResponseEntity<>(ResultRes.error(ResEnum.UPLOAD_FAIL.KEY, ResEnum.UPLOAD_FAIL.VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @ApiOperation("删除文件 - Delete file")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody LocalStorage localStorage) {
        LocalStorage storage = localStorageService.findById(localStorage.getId());
        FileUtils.del(storage.getPath());
        localStorageService.delete(localStorage.getId());
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }
}