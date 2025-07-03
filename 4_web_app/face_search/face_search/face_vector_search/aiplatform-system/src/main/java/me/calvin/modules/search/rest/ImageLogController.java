package me.calvin.modules.search.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.calvin.annotation.Log;
import me.calvin.modules.search.domain.ImageLog;
import me.calvin.modules.search.service.ImageLogService;
import me.calvin.modules.search.service.dto.ImageLogQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片日志信息管理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "ImageLogService管理")
@RequestMapping("/api/imageLog")
public class ImageLogController {

    private final ImageLogService imageLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('imageLog:list')")
    public void download(HttpServletResponse response, ImageLogQueryCriteria criteria) throws IOException {
        imageLogService.download(imageLogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ImageLogService")
    @ApiOperation("查询ImageLogService")
    @PreAuthorize("@el.check('imageLog:list')")
    public ResponseEntity<Object> query(ImageLogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(imageLogService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ImageLogService")
    @ApiOperation("新增ImageLogService")
    @PreAuthorize("@el.check('imageLog:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ImageLog resources){
        return new ResponseEntity<>(imageLogService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ImageLogService")
    @ApiOperation("修改ImageLogService")
    @PreAuthorize("@el.check('imageLog:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ImageLog resources){
        imageLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ImageLogService")
    @ApiOperation("删除ImageLogService")
    @PreAuthorize("@el.check('imageLog:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        imageLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}