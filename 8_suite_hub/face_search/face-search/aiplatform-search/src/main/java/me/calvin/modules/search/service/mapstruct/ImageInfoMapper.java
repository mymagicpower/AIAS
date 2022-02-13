package me.calvin.modules.search.service.mapstruct;

import me.calvin.base.BaseMapper;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Calvin
* @date 2021-02-17
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageInfoMapper extends BaseMapper<ImageInfoDto, ImageInfo> {

}