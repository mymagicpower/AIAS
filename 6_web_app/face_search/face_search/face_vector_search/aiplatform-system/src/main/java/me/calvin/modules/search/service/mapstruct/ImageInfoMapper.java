package me.calvin.modules.search.service.mapstruct;

import me.calvin.base.BaseMapper;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 图像信息 Mapper
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageInfoMapper extends BaseMapper<ImageInfoDto, ImageInfo> {

}