package top.aias.service;

import top.aias.domain.TextInfo;

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
     * 添加文本
     */
    public void addText(TextInfo textInfoDto);

    /**
     * 添加文本
     */
    public void addTexts(List<TextInfo> texts);

    /**
     * 更新图片信息
     */
    public void update(List<TextInfo> list);

    /**
     * 图片信息列表
     */
    public List<TextInfo> getTextList();
}