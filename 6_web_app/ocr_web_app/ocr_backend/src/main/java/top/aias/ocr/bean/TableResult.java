package top.aias.ocr.bean;

import ai.djl.modality.cv.output.BoundingBox;
import lombok.Data;

import java.util.List;
/**
 * 表格检测结果
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class TableResult {
    private List<String> structure_str_list;
    private List<BoundingBox> boxes;

    public TableResult(List<String> structure_str_list, List<BoundingBox> boxes) {
        this.structure_str_list = structure_str_list;
        this.boxes = boxes;
    }
}
