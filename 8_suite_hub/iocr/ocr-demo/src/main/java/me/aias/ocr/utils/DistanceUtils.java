package me.aias.ocr.utils;

import me.aias.ocr.model.LabelBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public class DistanceUtils {
    /**
     * Calculate L2 distance
     *
     * @param contentLabels 内容识别区
     * @param detectedTexts 文本检测区
     * @return
     */
    public static Map<String, String> l2Distance(List<LabelBean> contentLabels, List<LabelBean> detectedTexts) {
        Map<String, String> hashMap = new ConcurrentHashMap<>();
        for (int i = 0; i < contentLabels.size(); i++) {
            String field = contentLabels.get(i).getField();
            double minDistance = Double.MAX_VALUE;
            String value = "";
            for (int j = 0; j < detectedTexts.size(); j++) {
                double dis = l2Distance(contentLabels.get(i).getCenterPoint(), detectedTexts.get(j).getCenterPoint());
                if (dis < minDistance) {
                    minDistance = dis;
                    value = detectedTexts.get(j).getValue();
                }
            }
            System.out.println(field + " : " + value);
            hashMap.put(field, value);
        }
        return hashMap;
    }

    /**
     * Calculate iou
     *
     * @param contentLabels 内容识别区
     * @param detectedTexts 文本检测区
     * @return
     */
    public static Map<String, String> iou(List<LabelBean> contentLabels, List<LabelBean> detectedTexts) {
        Map<String, String> hashMap = new ConcurrentHashMap<>();
        for (int i = 0; i < contentLabels.size(); i++) {
            String field = contentLabels.get(i).getField();
            double maxIOU = 0d;
            String value = "";
            int[] box_1 = PointUtils.rectXYXY(contentLabels.get(i).getPoints());
            for (int j = 0; j < detectedTexts.size(); j++) {
                int[] box_2 = PointUtils.rectXYXY(detectedTexts.get(j).getPoints());
                double iou = compute_iou(box_1, box_2);
                if (iou > maxIOU) {
                    maxIOU = iou;
                    value = detectedTexts.get(j).getValue();
                }
            }
            System.out.println(field + " : " + value);
            hashMap.put(field, value);
        }
        return hashMap;
    }

    /**
     * Calculate L2 distance
     *
     * @param point1
     * @param point2
     * @return
     */
    public static double l2Distance(ai.djl.modality.cv.output.Point point1, ai.djl.modality.cv.output.Point point2) {
        double partX = Math.pow((point1.getX() - point2.getX()), 2);
        double partY = Math.pow((point1.getY() - point2.getY()), 2);
        return Math.sqrt(partX + partY);
    }

    /**
     * computing IoU
     *
     * @param rec1: (y0, x0, y1, x1), which reflects (top, left, bottom, right)
     * @param rec2: (y0, x0, y1, x1)
     * @return scala value of IoU
     */
    public static float compute_iou(int[] rec1, int[] rec2) {
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
}
