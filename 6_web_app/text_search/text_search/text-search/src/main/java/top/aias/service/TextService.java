package top.aias.service;

import top.aias.domain.TextInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 * Service Interface
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface TextService {
    /**
     * 根据ID查询
     */
    TextInfoDto findById(Long id);

    /**
     * 添加文本
     */
    public void addText(TextInfoDto textInfoDto);

    /**
     * 添加文本
     */
    public void addTexts(List<TextInfoDto> texts);

    /**
     * 清空数据
     */
    public void clear();

    /**
     * 获取图片清单
     */
    ConcurrentHashMap<Long, TextInfoDto> getMap();
}