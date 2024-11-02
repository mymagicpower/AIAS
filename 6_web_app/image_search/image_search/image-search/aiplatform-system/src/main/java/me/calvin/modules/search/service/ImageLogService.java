package me.calvin.modules.search.service;

import me.calvin.modules.search.domain.ImageLog;
import me.calvin.modules.search.service.dto.ImageLogDto;
import me.calvin.modules.search.service.dto.ImageLogQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 图像日志信息服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface ImageLogService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ImageLogQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ImageLogDto>
    */
    List<ImageLogDto> queryAll(ImageLogQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ImageLogDto
     */
    ImageLogDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return ImageLogDto
    */
    ImageLogDto create(ImageLog resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(ImageLog resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ImageLogDto> all, HttpServletResponse response) throws IOException;
}