package me.calvin.modules.search.repository;

import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 图片信息读写
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long>, JpaSpecificationExecutor<ImageInfo> {
    @Query(value ="select * from `image-search`.image_info i where i.group_id =?1", nativeQuery = true)
    public List<ImageInfo> findByGroupId(Long id);
}