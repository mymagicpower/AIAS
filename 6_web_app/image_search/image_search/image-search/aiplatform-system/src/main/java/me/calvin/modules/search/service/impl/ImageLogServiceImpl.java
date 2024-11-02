package me.calvin.modules.search.service.impl;

import lombok.RequiredArgsConstructor;
import me.calvin.modules.search.domain.ImageLog;
import me.calvin.modules.search.repository.ImageLogRepository;
import me.calvin.modules.search.service.ImageLogService;
import me.calvin.modules.search.service.dto.ImageLogDto;
import me.calvin.modules.search.service.dto.ImageLogQueryCriteria;
import me.calvin.modules.search.service.mapstruct.ImageLogMapper;
import me.calvin.utils.FileUtil;
import me.calvin.utils.PageUtil;
import me.calvin.utils.QueryHelp;
import me.calvin.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 图像日志服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Service
@RequiredArgsConstructor
public class ImageLogServiceImpl implements ImageLogService {

    private final ImageLogRepository imageLogRepository;
    private final ImageLogMapper imageLogMapper;

    @Override
    public Map<String,Object> queryAll(ImageLogQueryCriteria criteria, Pageable pageable){
        Page<ImageLog> page = imageLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(imageLogMapper::toDto));
    }

    @Override
    public List<ImageLogDto> queryAll(ImageLogQueryCriteria criteria){
        return imageLogMapper.toDto(imageLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ImageLogDto findById(Long id) {
        ImageLog imageLog = imageLogRepository.findById(id).orElseGet(ImageLog::new);
        ValidationUtil.isNull(imageLog.getLogId(),"ImageLog","id",id);
        return imageLogMapper.toDto(imageLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImageLogDto create(ImageLog resources) {
        return imageLogMapper.toDto(imageLogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ImageLog resources) {
        ImageLog imageLog = imageLogRepository.findById(resources.getLogId()).orElseGet(ImageLog::new);
        ValidationUtil.isNull( imageLog.getLogId(),"ImageLog","id",resources.getLogId());
        imageLog.copy(resources);
        imageLogRepository.save(imageLog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            imageLogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ImageLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ImageLogDto imageLog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("主键ID", imageLog.getLogId());
            map.put("图片ZIP包名称", imageLog.getZipName());
            map.put("上传图片清单", imageLog.getImageList());
            map.put("创建时间", imageLog.getCreateTime());
            map.put("创建人", imageLog.getCreateBy());
            map.put("修改时间", imageLog.getUpdateTime());
            map.put("修改人", imageLog.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}