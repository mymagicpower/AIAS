package me.aias.service;

import me.aias.domain.DNAInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface DNAService {
    /**
     * 根据ID查询
     */
    DNAInfoDto findById(Long id);

    /**
     * 添加文本
     */
    public void addText(DNAInfoDto textInfoDto);

    /**
     * 添加文本
     */
    public void addTexts(List<DNAInfoDto> texts);

    /**
     * 获取图片清单
     */
    ConcurrentHashMap<Long, DNAInfoDto> getMap();
}