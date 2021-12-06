package me.calvin.modules.search.service.dto;

import lombok.Data;
import me.calvin.annotation.Query;

import java.util.List;

/**
* @author Calvin
* @date 2021-02-17
**/
@Data
public class ImageInfoQueryCriteria{
    // IN 查询
    @Query(type = Query.Type.IN)
    private List<String> imageId;

    // 等于
    @Query
    private String uuid;
}