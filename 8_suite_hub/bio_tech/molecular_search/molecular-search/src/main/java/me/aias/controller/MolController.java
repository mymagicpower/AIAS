package me.aias.controller;

import io.milvus.client.ConnectFailedException;
import io.milvus.client.HasCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.utils.FileUtils;
import me.aias.common.utils.SvgUtils;
import me.aias.config.FileProperties;
import me.aias.domain.LocalStorage;
import me.aias.domain.MolInfoDto;
import me.aias.domain.ResEnum;
import me.aias.domain.ResultBean;
import me.aias.service.FeatureService;
import me.aias.service.LocalStorageService;
import me.aias.service.MolService;
import me.aias.service.SearchService;
import org.RDKit.RWMol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本管理
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "数据管理")
@RequestMapping("/api/text")
public class MolController {
    private final FileProperties properties;
    @Autowired
    private MolService textService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private LocalStorageService localStorageService;

    @Value("${search.dimension}")
    int dimension;

    @Value("${search.collectionName}")
    String collectionName;

    @ApiOperation(value = "提取分子特征值")
    @GetMapping("/extractFeatures")
    public ResultBean extractFeatures(@RequestParam(value = "id") String id) {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        String input = localStorage.getPath();
        File file = new File(input);
        List<String> lines = FileUtils.readFile(file);
        List<MolInfoDto> list = new ArrayList<>();
        MolInfoDto molInfoDto;
        // 解析DNA信息
        ConcurrentHashMap<Long, MolInfoDto> map = textService.getMap();
        long size = map.size();
        for (int i = 0; i < lines.size(); i++) {
            molInfoDto = new MolInfoDto();
            String smiles = lines.get(i).split("\t")[0];
            log.info("smiles: " + smiles);
            molInfoDto.setId(size++);
            molInfoDto.setSmiles(smiles);
            // 获取特征向量
            molInfoDto.setFeature(featureService.molFeature(smiles));
            //生成分子图片
            RWMol m1 = RWMol.MolFromSmiles(smiles);
            String rootPath = properties.getPath().getPath();
            SvgUtils.convertToPng(m1.ToSVG(), rootPath + molInfoDto.getId() + ".png");
            list.add(molInfoDto);
        }

        // 将向量插入向量引擎
        try {

            HasCollectionResponse response = searchService.hasCollection(this.collectionName);
            if (!response.hasCollection()) {
                searchService.createCollection(this.collectionName, dimension);
                searchService.createIndex(this.collectionName);
            }
            searchService.insertVectors(this.collectionName, list);
            textService.addTexts(list);
        } catch (ConnectFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ResultBean.failure().add(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE);
        }

        return ResultBean.success();
    }
}