package top.aias.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aias.common.utils.FeatureUtils;
import top.aias.domain.TextInfo;
import top.aias.domain.TextInfoRes;
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
    public List<TextInfoRes> search(List<TextInfo> dataList, Integer topK, float[] vectorToSearch) {
        System.out.println("========== searchTexts() ==========");
        List<TextInfoRes> resList = new ArrayList<>();

        TextInfoRes textInfoRes;
        for (TextInfo textInfo : dataList) {
            float[] feature = textInfo.getFeature();
            float score = FeatureUtils.cosineSim(vectorToSearch, feature);
            textInfoRes = new TextInfoRes();
            textInfoRes.setId(textInfo.getId());
            textInfoRes.setTitle(textInfo.getTitle());
            textInfoRes.setText(textInfo.getText());

            textInfoRes.setScore(score);
            resList.add(textInfoRes);
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
