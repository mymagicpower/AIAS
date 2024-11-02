package me.calvin.modules.search.repository;

import me.calvin.modules.search.domain.ImageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 图片日志信息读写
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface ImageLogRepository extends JpaRepository<ImageLog, Long>, JpaSpecificationExecutor<ImageLog> {
}