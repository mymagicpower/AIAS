package top.aias.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultBean;
import top.aias.domain.TextInfo;
import top.aias.domain.TextInfoRes;
import top.aias.service.FeatureService;
import top.aias.service.SearchService;
import top.aias.service.TextService;

import java.util.List;

/**
 * 搜索管理
 * Search management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
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

    @GetMapping("/text")
    @ApiOperation(value = "文本搜索", nickname = "search")
    public ResultBean search(@RequestParam("text") String text, @RequestParam(value = "topK") String topk) {
        try {
            Integer topK = Integer.parseInt(topk);
            // 特征提取
            float[] vectorToSearch = featureService.textFeature(text);
            List<TextInfo> dataList = textService.getTextList();
            // 根据文本特征向量搜索
            List<TextInfoRes> textInfoResList = searchService.search(dataList, topK, vectorToSearch);
            return ResultBean.success().add("result", textInfoResList);
//            return new ResponseEntity<>(ResultRes.success(textInfoResList, textInfoResList.size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }
    }
}
