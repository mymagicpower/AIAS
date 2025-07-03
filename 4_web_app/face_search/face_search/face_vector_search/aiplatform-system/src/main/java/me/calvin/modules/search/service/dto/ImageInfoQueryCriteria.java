package me.calvin.modules.search.service.dto;

import lombok.Data;
import me.calvin.annotation.Query;

import java.util.List;

/**
 * 图片信息查询对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
public class ImageInfoQueryCriteria{
    // IN 查询
    @Query(type = Query.Type.IN)
    private List<Long> imageId;

    // 等于
    @Query
    private String uuid;
}