package top.aias.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aias.common.utils.FeatureUtils;
import top.aias.domain.DataInfo;
import top.aias.domain.DataInfoRes;
import top.aias.service.SearchService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 搜素服务
 * File Storage Service
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    // 搜索向量
    public List<DataInfoRes> search(String baseUrl, List<DataInfo> imageDataList, Integer topK, float[] vectorToSearch) {
        System.out.println("========== searchImage() ==========");
        List<DataInfoRes> resList = new ArrayList<>();

        DataInfoRes dataInfoRes;
        for (DataInfo dataInfo : imageDataList) {
            float[] feature = dataInfo.getFeature();
            float score = FeatureUtils.cosineSim(vectorToSearch, feature);
            dataInfoRes = new DataInfoRes();
            dataInfoRes.setScore(score);
            dataInfoRes.setUrl(baseUrl + dataInfo.getRelativePath());
            resList.add(dataInfoRes);
        }

        // 根据 score 降序排序
        Collections.sort(resList);

        // 取前 topK 个
        if (topK >= resList.size()) {
            return resList;
        } else {
            resList = resList.subList(0, topK);
            return resList;
        }
    }

}
