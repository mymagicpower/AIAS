package me.aias.example.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiaoqiang
 * @Date: 2020/12/9 9:16
 * @Description:
 */
public class ConvertHtml2Excel {
    /**
     * html表格转excel
     *
     * @param tableHtml 如
     *            <table>
     *            ..
     *            </table>
     * @return
     */
    public static HSSFWorkbook table2Excel(String tableHtml) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<>();
        int rowIndex = 0;
        try {
            Document data = DocumentHelper.parseText(tableHtml);
            // 生成表头
            Element thead = data.getRootElement().element("thead");
            HSSFCellStyle titleStyle = getTitleStyle(wb);
            int ls=0;//列数
            if (thead != null) {
                List<Element> trLs = thead.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("td");
                    ls=thLs.size();
                    makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
            }
            // 生成表体
            Element tbody = data.getRootElement().element("tbody");
            HSSFCellStyle contentStyle = getContentStyle(wb);
            if (tbody != null) {
                List<Element> trLs = tbody.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("th");
                    int cellIndex = makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    List<Element> tdLs = trEle.elements("td");
                    makeRowCell(tdLs, rowIndex, row, cellIndex, contentStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
            }
            // 合并表头
            for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                sheet.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLastRow(), crcm.getFirstCol(), crcm.getLastCol()));
                setRegionStyle(sheet, new CellRangeAddress(crcm.getFirstRow(), crcm.getLastRow(), crcm.getFirstCol(), crcm.getLastCol()),titleStyle);
            }
            for(int i=0;i<sheet.getRow(0).getPhysicalNumberOfCells();i++){
                sheet.autoSizeColumn(i, true);//设置列宽
                if(sheet.getColumnWidth(i)<255*256){
                    sheet.setColumnWidth(i, sheet.getColumnWidth(i) < 9000 ? 9000 : sheet.getColumnWidth(i));
                }else{
                    sheet.setColumnWidth(i, 15000);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return wb;
    }

    /**
     * 生产行内容
     *
     * @return 最后一列的cell index
     */
    /**
     * @param tdLs th或者td集合
     * @param rowIndex 行号
     * @param row POI行对象
     * @param startCellIndex
     * @param cellStyle 样式
     * @param crossRowEleMetaLs 跨行元数据集合
     * @return
     */
    private static int makeRowCell(List<Element> tdLs, int rowIndex, HSSFRow row, int startCellIndex, HSSFCellStyle cellStyle,
                                   List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int i = startCellIndex;
        for (int eleIndex = 0; eleIndex < tdLs.size(); i++, eleIndex++) {
            int captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            while (captureCellSize > 0) {
                for (int j = 0; j < captureCellSize; j++) {// 当前行跨列处理（补单元格）
                    row.createCell(i);
                    i++;
                }
                captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            }
            Element thEle = tdLs.get(eleIndex);
            String val = thEle.getTextTrim();
            if (StringUtils.isBlank(val)) {
                Element e = thEle.element("a");
                if (e != null) {
                    val = e.getTextTrim();
                }
            }
            HSSFCell c = row.createCell(i);
            if (NumberUtils.isNumber(val)) {
                c.setCellValue(Double.parseDouble(val));
                c.setCellType(CellType.NUMERIC);
            } else {
                c.setCellValue(val);
            }
            int rowSpan = NumberUtils.toInt(thEle.attributeValue("rowspan"), 1);
            int colSpan = NumberUtils.toInt(thEle.attributeValue("colspan"), 1);
            c.setCellStyle(cellStyle);
            if (rowSpan > 1 || colSpan > 1) { // 存在跨行或跨列
                crossRowEleMetaLs.add(new CrossRangeCellMeta(rowIndex, i, rowSpan, colSpan));
            }
            if (colSpan > 1) {// 当前行跨列处理（补单元格）
                for (int j = 1; j < colSpan; j++) {
                    i++;
                    row.createCell(i);
                }
            }
        }
        return i;
    }

    /**
     * 设置合并单元格的边框样式
     *
     * @param sheet
     * @param region
     * @param cs
     */
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                cell.setCellStyle(cs);
            }
        }
    }

    /**
     * 获得因rowSpan占据的单元格
     *
     * @param rowIndex 行号
     * @param colIndex 列号
     * @param crossRowEleMetaLs 跨行列元数据
     * @return 当前行在某列需要占据单元格
     */
    private static int getCaptureCellSize(int rowIndex, int colIndex, List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int captureCellSize = 0;
        for (CrossRangeCellMeta crossRangeCellMeta : crossRowEleMetaLs) {
            if (crossRangeCellMeta.getFirstRow() < rowIndex && crossRangeCellMeta.getLastRow() >= rowIndex) {
                if (crossRangeCellMeta.getFirstCol() <= colIndex && crossRangeCellMeta.getLastCol() >= colIndex) {
                    captureCellSize = crossRangeCellMeta.getLastCol() - colIndex + 1;
                }
            }
        }
        return captureCellSize;
    }

    /**
     * 获得标题样式
     *
     * @param workbook
     * @return
     */
    private static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        //short titlebackgroundcolor = IndexedColors.GREY_25_PERCENT.index;
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //style.setFillForegroundColor(titlebackgroundcolor);// 背景色

        HSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 获得内容样式
     *
     * @param wb
     * @return
     */
    private static HSSFCellStyle getContentStyle(HSSFWorkbook wb) {
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        HSSFFont font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);
        return style;
    }
}

