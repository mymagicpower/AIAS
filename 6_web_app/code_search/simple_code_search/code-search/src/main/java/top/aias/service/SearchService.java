package top.aias.service;

import top.aias.domain.TextInfo;
import top.aias.domain.TextInfoRes;

import java.util.List;
/**
 * 搜索服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface SearchService {
    /**
     * 搜索向量
     * @param dataList
     * @param topK
     * @param vectorToSearch
     * @return
     */
    List<TextInfoRes> search(List<TextInfo> dataList, Integer topK, float[] vectorToSearch);
}
