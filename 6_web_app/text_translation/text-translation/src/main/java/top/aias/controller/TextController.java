package top.aias.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aias.domain.ResEnum;
import top.aias.domain.ResultBean;
import top.aias.domain.TextInfo;
import top.aias.service.TextService;

/**
 * 文本翻译
 * Data management
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "文本翻译")
@RequestMapping("/api/text")
public class TextController {
    private Logger logger = LoggerFactory.getLogger(TextController.class);
    @Autowired
    private TextService textService;

    @ApiOperation(value = "文本翻译")
    @GetMapping("/translate")
    public ResultBean translate(@RequestParam(value = "text") String text, @RequestParam(value = "srcLangId") long srcLangId, @RequestParam(value = "targetLangId") long targetLangId) {

        // 文本翻译
        try {
            String result = textService.translate(text, srcLangId, targetLangId);
            return ResultBean.success().add("result", result);
        } catch (TranslateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE);
        }
    }
}