package me.aias.service;

import me.aias.domain.MolInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface MolService {
    /**
     * 根据ID查询
     */
    MolInfoDto findById(Long id);

    /**
     * 添加文本
     */
    public void addText(MolInfoDto textInfoDto);

    /**
     * 添加文本
     */
    public void addTexts(List<MolInfoDto> texts);

    /**
     * 获取图片清单
     */
    ConcurrentHashMap<Long, MolInfoDto> getMap();
}