package me.aias.service;

import me.aias.domain.TextInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 *
 * @author Calvin
 * @date 2021-12-19
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
     * 获取图片清单
     */
    ConcurrentHashMap<Long, TextInfoDto> getMap();
}