package top.aias.controller;

import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.response.SearchResultsWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.aias.service.FeatureService;
import top.aias.service.SearchService;
import top.aias.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultBean;
import top.aias.domain.TextInfo;
import top.aias.domain.TextInfoRes;
import top.aias.service.TranslationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索管理
 * Search management
 *
 * @author Calvin
 * @email 179209347@qq.com
 **/
@Slf4j
@Api(tags = "搜索管理")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private TextService textService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private TranslationService translationService;

    @Value("${search.collectionName}")
    String collectionName;

    @GetMapping("/text")
    @ApiOperation(value = "英文语义搜索", nickname = "search")
    public ResultBean search(@RequestParam("text") String text, @RequestParam(value = "topK") String topk) {
        Integer topK = Integer.parseInt(topk);
        List<Float> vectorToSearch = null;
        try {
            // 生成向量
            vectorToSearch = featureService.textFeature(text);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据向量搜索
            R<SearchResults> searchResponse = searchService.search(topK, vectorsToSearch);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(searchResponse.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(0);

            // 根据ID获取文本信息
            List<TextInfo> dataList = textService.getTextList();
            List<TextInfoRes> textInfoResList = new ArrayList<>();
            for (SearchResultsWrapper.IDScore score : scores) {
                TextInfo textInfo = new TextInfo();
                for (TextInfo item : dataList) {
                    if (item.getId() == score.getLongID()) {
                        textInfo = item;
                        break;
                    }
                }
                TextInfoRes textInfoRes = new TextInfoRes();
                textInfoRes.setId(score.getLongID());
                textInfoRes.setScore(score.getScore());
                textInfoRes.setTitle(textInfo.getTitle());
                textInfoRes.setText(textInfo.getText());
                textInfoResList.add(textInfoRes);
            }

            return ResultBean.success().add("result", textInfoResList);
//            return new ResponseEntity<>(ResultRes.success(textInfoResList, textInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE);
        }
    }

    @GetMapping("/zhtext")
    @ApiOperation(value = "中文语义搜索", nickname = "zhSearch")
    public ResultBean zhSearch(@RequestParam("text") String text, @RequestParam(value = "topK") String topk) {
        Integer topK = Integer.parseInt(topk);
        List<Float> vectorToSearch = null;
        try {
            // 翻译成英文
            String enText = translationService.translate(text);
            // 生成向量
            vectorToSearch = featureService.textFeature(enText);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }

        List<List<Float>> vectorsToSearch = new ArrayList<List<Float>>();
        vectorsToSearch.add(vectorToSearch);

        try {
            // 根据向量搜索
            R<SearchResults> searchResponse = searchService.search(topK, vectorsToSearch);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(searchResponse.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(0);

            // 根据ID获取文本信息
            List<TextInfo> dataList = textService.getTextList();
            List<TextInfoRes> textInfoResList = new ArrayList<>();
            for (SearchResultsWrapper.IDScore score : scores) {
                TextInfo textInfo = new TextInfo();
                for (TextInfo item : dataList) {
                    if (item.getId() == score.getLongID()) {
                        textInfo = item;
                        break;
                    }
                }
                TextInfoRes textInfoRes = new TextInfoRes();
                textInfoRes.setId(score.getLongID());
                textInfoRes.setScore(score.getScore());
                textInfoRes.setTitle(textInfo.getTitle());
                textInfoRes.setText(textInfo.getText());
                textInfoResList.add(textInfoRes);
            }

            return ResultBean.success().add("result", textInfoResList);
//            return new ResponseEntity<>(ResultRes.success(textInfoResList, textInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE);
        }
    }
}
