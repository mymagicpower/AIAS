package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TableDetection {

  private static final Logger logger = LoggerFactory.getLogger(TableDetection.class);

  public TableDetection() {}

  public Criteria<Image, TableResult> criteria() {

    Criteria<Image, TableResult> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, TableResult.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/en_table.zip")
            //            .optModelUrls(
            // "/Users/calvin/Documents/build/paddle_models/ppocr/en_ppocr_mobile_v2.0_table_structure_infer")
            // .optDevice(Device.cpu())
            .optOption("removePass", "repeated_fc_relu_fuse_pass")
            .optTranslator(new TableStructTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }

  public List<String> cellContents(
      DetectedObjects textDetections, List<BoundingBox> cells, int width, int height) {
    List<DetectedObjects.DetectedObject> dt_boxes = textDetections.items();

    // 获取 Cell 与 文本检测框 的对应关系(1:N)。
    Map<Integer, List<Integer>> matched = new ConcurrentHashMap<>();

    for (int i = 0; i < dt_boxes.size(); i++) {
      DetectedObjects.DetectedObject item = dt_boxes.get(i);
      Rectangle textBounds = item.getBoundingBox().getBounds();
      int[] box_1 = rectXYXY(textBounds, width, height);
      // 获取两两cell之间的L1距离和 1- IOU
      List<Pair<Float, Float>> distances = new ArrayList<>();
      for (BoundingBox cell : cells) {
        Rectangle cellBounds = cell.getBounds();
        int[] box_2 = rectXYXY(cellBounds, width, height);
        float distance = distance(box_1, box_2);
        float iou = 1 - compute_iou(box_1, box_2);
        distances.add(Pair.of(distance, iou));
      }
      // 根据距离和IOU挑选最"近"的cell
      Pair<Float, Float> nearest = sorted(distances);

      // 获取最小距离对应的下标id，也等价于cell的下标id  （distances列表是根据遍历cells生成的）
      int id = 0;
      for (int idx = 0; idx < distances.size(); idx++) {
        Pair<Float, Float> current = distances.get(idx);
        if (current.getLeft().floatValue() == nearest.getLeft().floatValue()
            && current.getRight().floatValue() == nearest.getRight().floatValue()) {
          id = idx;
          break;
        }
      }
      if (!matched.containsKey(id)) {
        List<Integer> textIds = new ArrayList<>();
        textIds.add(i);
        // cell id, text id list (dt_boxes index list)
        matched.put(id, textIds);
      } else {
        matched.get(id).add(i);
      }
    }

    List<String> cell_contents = new ArrayList<>();
    List<Double> probs = new ArrayList<>();
    for (int i = 0; i < cells.size(); i++) {
      List<Integer> textIds = matched.get(i);
      List<String> contents = new ArrayList<>();
      String content = "";
      if (textIds != null) {
        for (Integer id : textIds) {
          DetectedObjects.DetectedObject item = dt_boxes.get(id);
          contents.add(item.getClassName());
        }
        content = StringUtils.join(contents, " ");
      }

      cell_contents.add(content);
      probs.add(-1.0);
    }
    return cell_contents;
  }

  /**
   * Calculate L1 distance
   *
   * @param box_1
   * @param box_2
   * @return
   */
  private int distance(int[] box_1, int[] box_2) {
    int x1 = box_1[0];
    int y1 = box_1[1];
    int x2 = box_1[2];
    int y2 = box_1[3];
    int x3 = box_2[0];
    int y3 = box_2[1];
    int x4 = box_2[2];
    int y4 = box_2[3];
    int dis = Math.abs(x3 - x1) + Math.abs(y3 - y1) + Math.abs(x4 - x2) + Math.abs(y4 - y2);
    int dis_2 = Math.abs(x3 - x1) + Math.abs(y3 - y1);
    int dis_3 = Math.abs(x4 - x2) + Math.abs(y4 - y2);
    return dis + Math.min(dis_2, dis_3);
  }

  /**
   * Get absolute coordinations
   *
   * @param rect
   * @param width
   * @param height
   * @return
   */
  private int[] rectXYXY(Rectangle rect, int width, int height) {
    int left = Math.max((int) (width * rect.getX()), 0);
    int top = Math.max((int) (height * rect.getY()), 0);
    int right = Math.min((int) (width * (rect.getX() + rect.getWidth())), width - 1);
    int bottom = Math.min((int) (height * (rect.getY() + rect.getHeight())), height - 1);
    return new int[] {left, top, right, bottom};
  }

  /**
   * computing IoU
   *
   * @param rec1: (y0, x0, y1, x1), which reflects (top, left, bottom, right)
   * @param rec2: (y0, x0, y1, x1)
   * @return scala value of IoU
   */
  private float compute_iou(int[] rec1, int[] rec2) {
    // computing area of each rectangles
    int S_rec1 = (rec1[2] - rec1[0]) * (rec1[3] - rec1[1]);
    int S_rec2 = (rec2[2] - rec2[0]) * (rec2[3] - rec2[1]);

    // computing the sum_area
    int sum_area = S_rec1 + S_rec2;

    // find the each edge of intersect rectangle
    int left_line = Math.max(rec1[1], rec2[1]);
    int right_line = Math.min(rec1[3], rec2[3]);
    int top_line = Math.max(rec1[0], rec2[0]);
    int bottom_line = Math.min(rec1[2], rec2[2]);

    // judge if there is an intersect
    if (left_line >= right_line || top_line >= bottom_line) {
      return 0.0f;
    } else {
      float intersect = (right_line - left_line) * (bottom_line - top_line);
      return (intersect / (sum_area - intersect)) * 1.0f;
    }
  }

  /**
   * Distance sorted
   *
   * @param distances
   * @return
   */
  private Pair<Float, Float> sorted(List<Pair<Float, Float>> distances) {
    Comparator<Pair<Float, Float>> comparator =
        new Comparator<Pair<Float, Float>>() {
          @Override
          public int compare(Pair<Float, Float> a1, Pair<Float, Float> a2) {
            // 首先根据IoU排序
            if (a1.getRight().floatValue() > a2.getRight().floatValue()) {
              return 1;
            } else if (a1.getRight().floatValue() == a2.getRight().floatValue()) {
              // 然后根据L1距离排序
              if (a1.getLeft().floatValue() > a2.getLeft().floatValue()) {
                return 1;
              }
              return -1;
            }
            return -1;
          }
        };

    // 距离排序
    List<Pair<Float, Float>> newDistances = new ArrayList<>();
    CollectionUtils.addAll(newDistances, new Object[distances.size()]);
    Collections.copy(newDistances, distances);
    Collections.sort(newDistances, comparator);
    return newDistances.get(0);
  }

  /**
   * Generate table html
   *
   * @param pred_structures
   * @param cell_contents
   * @return
   */
  public String get_pred_html(List<String> pred_structures, List<String> cell_contents) {
    StringBuffer html = new StringBuffer();
    int td_index = 0;
    for (String tag : pred_structures) {
      if (tag.contains("</td>")) {
        String content = cell_contents.get(td_index);
        html.append(content);
        td_index++;
      }
      html.append(tag);
    }

    return html.toString();
  }
}
