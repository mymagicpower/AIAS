package top.aias.service;

import top.aias.domain.DataInfo;
import top.aias.domain.DataInfoRes;

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
     * @param baseUrl
     * @param imageDataList
     * @param topK
     * @param vectorToSearch
     * @return
     */
    List<DataInfoRes> search(String baseUrl, List<DataInfo> imageDataList, Integer topK, float[] vectorToSearch);

}
