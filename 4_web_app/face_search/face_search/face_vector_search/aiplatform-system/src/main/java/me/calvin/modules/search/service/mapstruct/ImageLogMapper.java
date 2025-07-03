package me.calvin.modules.search.service.mapstruct;

import me.calvin.base.BaseMapper;
import me.calvin.modules.search.domain.ImageLog;
import me.calvin.modules.search.service.dto.ImageLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 图像日志信息 Mapper
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageLogMapper extends BaseMapper<ImageLogDto, ImageLog> {

}