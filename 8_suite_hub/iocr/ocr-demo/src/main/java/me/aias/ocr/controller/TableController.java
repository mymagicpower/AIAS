package me.aias.ocr.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.aias.ocr.configuration.FileProperties;
import me.aias.ocr.model.DataBean;
import me.aias.ocr.model.ResultBean;
import me.aias.ocr.service.InferService;
import me.aias.ocr.service.TableInferService;
import me.aias.ocr.utils.ConvertHtml2Excel;
import me.aias.ocr.utils.UUIDUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Api(tags = "表格文字识别")
@RestController
@RequestMapping("/table")
public class TableController {
    private Logger logger = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableInferService tableInferService;

    @Value("${server.baseUri}")
    private String baseUri;

    /**
     * file configuration
     */
    @Autowired
    private FileProperties properties;

    @ApiOperation(value = "单表格文字识别-URL")
    @GetMapping(value = "/tableInfoForImageUrl")
    public ResultBean tableInfoForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = ImageFactory.getInstance().fromUrl(url);
            String tableHtml = tableInferService.getTableHtml(image);
            // 创建一个Excel文件
            tableHtml = tableHtml.replace("<html><body>", "");
            tableHtml = tableHtml.replace("</body></html>", "");
            HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(tableHtml);

            FileProperties.ElPath path = properties.getPath();
            String fileRelativePath = path.getPath().replace("\\", "/") + "tables/";
            //Check & create file path
            Path filePath = Paths.get(fileRelativePath);
            File file = filePath.toFile();
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            String fileId = UUIDUtils.getUUID();
            workbook.write(new File(fileRelativePath + fileId + ".xls"));

            try (OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(fileRelativePath + fileId + ".html"), "UTF-8")) {
                out.write(tableHtml);
            }

            String excelUri = baseUri + File.separator + fileRelativePath + fileId + ".xls";
            String htmlUri = baseUri + File.separator + fileRelativePath + fileId + ".html";

            Map<String, String> map = new ConcurrentHashMap<>();
            map.put("excelUri", excelUri);
            map.put("htmlUri", htmlUri);
            map.put("html", tableHtml);
            return ResultBean.success().add("result", map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }

    @ApiOperation(value = "单表格文字识别-图片")
    @PostMapping("/tableInfoForImageFile")
    public ResultBean tableInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            String tableHtml = tableInferService.getTableHtml(image);
            // 创建一个Excel文件
            tableHtml = tableHtml.replace("<html><body>", "");
            tableHtml = tableHtml.replace("</body></html>", "");
            HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(tableHtml);

            FileProperties.ElPath path = properties.getPath();
            String fileRelativePath = path.getPath().replace("\\", "/") + "tables/";
            //Check & create file path
            Path filePath = Paths.get(fileRelativePath);
            File file = filePath.toFile();
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }

            String fileId = UUIDUtils.getUUID();
            workbook.write(new File(fileRelativePath + fileId + ".xls"));

            try (OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(fileRelativePath + fileId + ".html"), "UTF-8")) {
                out.write(tableHtml);
            }

            String excelUri = baseUri + File.separator + fileRelativePath + fileId + ".xls";
            String htmlUri = baseUri + File.separator + fileRelativePath + fileId + ".html";

            Map<String, String> map = new ConcurrentHashMap<>();
            map.put("excelUri", excelUri);
            map.put("htmlUri", htmlUri);
            map.put("html", tableHtml);

            return ResultBean.success().add("result", map)
                    .add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }

    @ApiOperation(value = "表单表格自动检测文字识别-URL")
    @GetMapping(value = "/autoTableInfoForImageUrl")
    public ResultBean autoTableInfoForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = ImageFactory.getInstance().fromUrl(url);
            List<String> tableHtmlList = tableInferService.getTableHtmlList(image);
            List<HSSFWorkbook> workbookList = new ArrayList<>();

            for (String tableHtml : tableHtmlList) {
                tableHtml = tableHtml.replace("<html><body>", "");
                tableHtml = tableHtml.replace("</body></html>", "");
                // Create workbook for each table
                HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(tableHtml);
                workbookList.add(workbook);
            }

            FileProperties.ElPath path = properties.getPath();
            String fileRelativePath = path.getPath().replace("\\", "/") + "tables/";
            //Check & create file path
            Path filePath = Paths.get(fileRelativePath);
            File file = filePath.toFile();
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }

            List<Map<String, String>> uriList = new ArrayList<>();
            for (int i = 0; i < tableHtmlList.size(); i++) {
                String fileId = UUIDUtils.getUUID();
                HSSFWorkbook workbook = workbookList.get(i);
                workbook.write(new File(fileRelativePath + fileId + ".xls"));
                String tableHtml = tableHtmlList.get(i);
                try (OutputStreamWriter out = new OutputStreamWriter(
                        new FileOutputStream(fileRelativePath + fileId + ".html"), "UTF-8")) {
                    out.write(tableHtml);
                }
                String excelUri = baseUri + File.separator + fileRelativePath + fileId + ".xls";
                String htmlUri = baseUri + File.separator + fileRelativePath + fileId + ".html";
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("excelUri", excelUri);
                map.put("htmlUri", htmlUri);
                map.put("html", tableHtml);
                uriList.add(map);
            }

            return ResultBean.success().add("result", uriList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }

    @ApiOperation(value = "表单表格自动检测文字识别-URL")
    @PostMapping("/autoTableInfoForImageFile")
    public ResultBean autoTableInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = ImageFactory.getInstance().fromInputStream(inputStream);

            List<String> tableHtmlList = tableInferService.getTableHtmlList(image);
            List<HSSFWorkbook> workbookList = new ArrayList<>();

            for (String tableHtml : tableHtmlList) {
                tableHtml = tableHtml.replace("<html><body>", "");
                tableHtml = tableHtml.replace("</body></html>", "");
                // Create workbook for each table
                HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(tableHtml);
                workbookList.add(workbook);
            }

            FileProperties.ElPath path = properties.getPath();
            String fileRelativePath = path.getPath().replace("\\", "/") + "tables/";
            //Check & create file path
            Path filePath = Paths.get(fileRelativePath);
            File file = filePath.toFile();
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }

            List<Map<String, String>> uriList = new ArrayList<>();
            for (int i = 0; i < tableHtmlList.size(); i++) {
                String fileId = UUIDUtils.getUUID();
                HSSFWorkbook workbook = workbookList.get(i);
                workbook.write(new File(fileRelativePath + fileId + ".xls"));
                String tableHtml = tableHtmlList.get(i);


                try (OutputStreamWriter out = new OutputStreamWriter(
                        new FileOutputStream(fileRelativePath + fileId + ".html"), "UTF-8")) {
                    out.write(tableHtml);
                }
                String excelUri = baseUri + File.separator + fileRelativePath + fileId + ".xls";
                String htmlUri = baseUri + File.separator + fileRelativePath + fileId + ".html";
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("excelUri", excelUri);
                map.put("htmlUri", htmlUri);
                map.put("html", tableHtml);
                uriList.add(map);
            }

            return ResultBean.success().add("result", uriList).add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }
}
