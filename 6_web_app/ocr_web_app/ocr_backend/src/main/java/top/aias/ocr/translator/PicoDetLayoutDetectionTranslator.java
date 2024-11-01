package top.aias.ocr.translator;

import ai.djl.Model;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 布局检测前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class PicoDetLayoutDetectionTranslator implements Translator<Image, DetectedObjects> {
    private List<String> table;

    NDArray scale_factor;
    Shape input_shape;
    Shape ori_shape;

    int[] strides = new int[]{8, 16, 32, 64};
    float score_threshold = 0.6f; // default = 0.4
    float nms_threshold = 0.5f;
    int nms_top_k = 1000;
    int keep_top_k = 100;

    private int width;
    private int height;

    public PicoDetLayoutDetectionTranslator() {
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        Model model = ctx.getModel();
        try (InputStream is = model.getArtifact("dict.txt").openStream()) {
            table = Utils.readLines(is, true);
        }
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
            NDArray img = input.toNDArray(ctx.getNDManager());
            width = input.getWidth();
            height = input.getHeight();

            img = NDImageUtils.resize(img, 608, 800);
            img = img.transpose(2, 0, 1).div(255);
            img =
                    NDImageUtils.normalize(
                            img, new float[]{0.485f, 0.456f, 0.406f}, new float[]{0.229f, 0.224f, 0.225f});
            img = img.expandDims(0);

            // (im_scale_y im_scale_x)
            scale_factor = ctx.getNDManager().create(new float[]{800f / height, 608f / width});
            input_shape = new Shape(800, 608);
            ori_shape = input_shape; //new Shape(height, width);

            return new NDList(img);
    }

    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
//        NDManager manager = ctx.getNDManager();
        try (NDManager manager =
                     NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {

            NDList raw_boxes = list.subNDList(4);
            NDList scores = list;
            scores.removeAll(raw_boxes);

            int dimension = raw_boxes.get(0).getShape().dimension();
            int reg_max = (int) (raw_boxes.get(0).getShape().get(dimension - 1) / 4 - 1);
            NDArray out_boxes_list = manager.zeros(new Shape(0, 6));

            List<BoundingBox> boxesList = new ArrayList<>();
            List<String> namesList = new ArrayList<>();
            List<Double> probsList = new ArrayList<>();

            NDArray bboxes = manager.zeros(new Shape(0, 4));
            NDArray confidences = manager.zeros(new Shape(0, scores.get(0).getShape().get(2))); // CN 10, EN 5

            for (int i = 0; i < scores.size(); i++) {
                NDArray box_distribute = raw_boxes.get(i);
                box_distribute = box_distribute.squeeze();
                NDArray score = scores.get(i);
                score = score.squeeze(0);
                int stride = strides[i];
                // centers
                float fm_h = input_shape.get(0) / (float) stride;
                float fm_w = input_shape.get(1) / (float) stride;
                NDArray h_range = manager.arange(fm_h);
                NDArray w_range = manager.arange(fm_w);
                w_range = w_range.reshape(1, w_range.size());

                NDArray ww = manager.zeros(new Shape(0, w_range.size()));
                for (int j = 0; j < h_range.size(); j++) {
                    ww = ww.concat(w_range, 0);
                }

                h_range = h_range.reshape(h_range.size(), 1);

                NDArray hh = manager.zeros(new Shape(h_range.size(), 0));
                for (int j = 0; j < w_range.size(); j++) {
                    hh = hh.concat(h_range, 1);
                }

                NDArray ct_row = hh.flatten().add(0.5).mul(stride);
                NDArray ct_col = ww.flatten().add(0.5).mul(stride);
                ct_row = ct_row.reshape(ct_row.size(), 1);
                ct_col = ct_col.reshape(ct_col.size(), 1);

                NDArray center = ct_col.concat(ct_row, 1).concat(ct_col, 1).concat(ct_row, 1);
                // box distribution to distance
                NDArray reg_range = manager.arange(reg_max + 1);
                NDArray box_distance = box_distribute.reshape(-1, reg_max + 1);
                box_distance = box_distance.softmax(1);
                box_distance = box_distance.mul(reg_range.expandDims(0));
                box_distance = box_distance.sum(new int[]{1}).reshape(-1, 4);
                box_distance = box_distance.mul(stride);

                // top K candidate
                NDArray topk_idx = score.max(new int[]{1}).argSort(0, false);
                topk_idx = topk_idx.get(new NDIndex(":" + this.nms_top_k));
                center = center.get(topk_idx);
                score = score.get(topk_idx);
                box_distance = box_distance.get(topk_idx);

                // decode box
                NDArray decode_box = center.add(manager.create(new int[]{-1, -1, 1, 1}).mul(box_distance));
                bboxes = bboxes.concat(decode_box, 0);
                confidences = confidences.concat(score, 0);
            }

            // nms
            NDArray picked_box_probs = manager.zeros(new Shape(0, 5));
            ArrayList<Integer> picked_labels = new ArrayList<>();
            for (int class_index = 0; class_index < confidences.getShape().get(1); class_index++) {
                NDArray probs = confidences.get(new NDIndex(":," + class_index));
                NDArray mask = probs.gt(this.score_threshold);
                probs = probs.get(mask);
                if (probs.getShape().get(0) == 0) {
                    continue;
                }
                NDArray subset_boxes = bboxes.get(mask);
                NDArray box_probs = subset_boxes.concat(probs.reshape(-1, 1), 1);
                box_probs = hard_nms(manager, box_probs, this.nms_threshold, this.keep_top_k, 200);

                picked_box_probs = picked_box_probs.concat(box_probs);
                for (int i = 0; i < box_probs.size(0); i++) {
                    picked_labels.add(class_index);
                }
            }

            if (picked_box_probs.size() == 0) {
//            out_boxes_list.concat(manager.zeros(new Shape(0,4)));
//            out_boxes_num.concat(manager.create(0));
            } else {
                // resize output boxes
                NDArray wb = warp_boxes(manager, picked_box_probs.get(new NDIndex(":, :4")));
                picked_box_probs.set(new NDIndex(":, :4"), wb);

                NDArray im_scale = scale_factor.flip(0).concat(scale_factor.flip(0));

                picked_box_probs.set(new NDIndex(":, :4"), picked_box_probs.get(new NDIndex(":, :4")).div(im_scale));

                // clas score box
                float[] arr = new float[picked_labels.size()];
                for (int i = 0; i < picked_labels.size(); i++) {
                    arr[i] = picked_labels.get(i);
                }

                int rows = picked_labels.size();
                NDArray labels = manager.create(arr).reshape(rows, 1);
                NDArray picked_box_prob_1 = picked_box_probs.get(new NDIndex(":, 4")).reshape(rows, 1);
                NDArray picked_box_prob_2 = picked_box_probs.get(new NDIndex(":, :4"));
                NDArray out_boxes = labels.concat(picked_box_prob_1, 1).concat(picked_box_prob_2, 1);
                out_boxes_list = out_boxes_list.concat(out_boxes);
            }


            for (int i = 0; i < out_boxes_list.size(0); i++) {
                NDArray dt = out_boxes_list.get(i);
                float[] array = dt.toFloatArray();
//            if (array[1] <= 0.5 || array[0] <= -1) continue;
                int clsid = (int) array[0];
                double score = array[1];
                String name = table.get(clsid);

                float x = array[2] / width;
                float y = array[3] / height;
                float w = (array[4] - array[2]) / width;
                float h = (array[5] - array[3]) / height;

                Rectangle rect = new Rectangle(x, y, w, h);
                boxesList.add(rect);
                namesList.add(name);
                probsList.add(score);
            }
            return new DetectedObjects(namesList, probsList, boxesList);
        }
    }

    private NDArray warp_boxes(NDManager manager, NDArray boxes) {
        int width = (int) ori_shape.get(1);
        int height = (int) ori_shape.get(0);
        int n = (int) boxes.size(0);
        if (n > 0) {
            // warp points
            NDArray xy = manager.ones(new Shape(n * 4, 3));
            NDArray box1 = boxes.get(new NDIndex(":,0")).reshape(n, 1);
            NDArray box2 = boxes.get(new NDIndex(":,3")).reshape(n, 1);
            NDArray box3 = boxes.get(new NDIndex(":,2")).reshape(n, 1);
            NDArray box4 = boxes.get(new NDIndex(":,1")).reshape(n, 1);
            boxes = boxes.concat(box1, 1).concat(box2, 1).concat(box3, 1).concat(box4, 1);
            boxes = boxes.reshape(n * 4, 2);
            xy.set(new NDIndex(":, :2"), boxes);

            xy = xy.get(new NDIndex(":, :2")).div(xy.get(new NDIndex(":, 2:3"))).reshape(n, 8);

            // create new boxes
            NDArray xy0 = xy.get(new NDIndex(":,0")).reshape(n, 1);
            NDArray xy2 = xy.get(new NDIndex(":,2")).reshape(n, 1);
            NDArray xy4 = xy.get(new NDIndex(":,4")).reshape(n, 1);
            NDArray xy6 = xy.get(new NDIndex(":,6")).reshape(n, 1);
            NDArray x = xy0.concat(xy2, 1).concat(xy4, 1).concat(xy6, 1);

            NDArray xy1 = xy.get(new NDIndex(":,1")).reshape(n, 1);
            NDArray xy3 = xy.get(new NDIndex(":,3")).reshape(n, 1);
            NDArray xy5 = xy.get(new NDIndex(":,5")).reshape(n, 1);
            NDArray xy7 = xy.get(new NDIndex(":,7")).reshape(n, 1);
            NDArray y = xy1.concat(xy3, 1).concat(xy5, 1).concat(xy7, 1);
            xy = x.min(new int[]{1}).concat(y.min(new int[]{1})).concat(x.max(new int[]{1})).concat(y.max(new int[]{1})).reshape(4, n).transpose();

            // clip boxes
            xy.set(new NDIndex(":,0"), xy.get(new NDIndex(":,0")).clip(0, width));
            xy.set(new NDIndex(":,2"), xy.get(new NDIndex(":,2")).clip(0, width));
            xy.set(new NDIndex(":,1"), xy.get(new NDIndex(":,1")).clip(0, height));
            xy.set(new NDIndex(":,3"), xy.get(new NDIndex(":,3")).clip(0, height));

            return xy;

        } else {
            return boxes;
        }

    }

    private NDArray hard_nms(NDManager manager, NDArray box_scores, float iou_threshold, int top_k, int candidate_size) {
        NDArray scores = box_scores.get(new NDIndex(":, -1"));
        NDArray boxes = box_scores.get(new NDIndex(":, :-1"));
        NDArray indexes = scores.argSort();
        if (candidate_size < indexes.size()) {
            indexes = indexes.get(new NDIndex((-candidate_size) + ":"));
        }
        NDArray picked = manager.zeros(new Shape(0), DataType.INT64);
        while (indexes.size() > 0) {
            NDArray current = indexes.get(new NDIndex("-1")).reshape(1);
            picked = picked.concat(current, 0);
            if (top_k == picked.size() || indexes.size() == 1) {
                break;
            }
            NDArray current_box = boxes.get(current);
            indexes = indexes.get(new NDIndex(":-1"));
            NDArray rest_boxes = boxes.get(indexes);
            NDArray iou = iou_of(rest_boxes, current_box.expandDims(0));
            iou = iou.squeeze();
            if (iou.getShape().dimension() == 0)
                iou = iou.reshape(1);
            NDArray cutOff = iou.lte(iou_threshold);
            indexes = indexes.get(cutOff);
        }

        return box_scores.get(picked);
    }

    private NDArray iou_of(NDArray boxes0, NDArray boxes1) {
        NDArray overlap_left_top = NDArrays.maximum(boxes0.get(new NDIndex("..., :2")), boxes1.get(new NDIndex("..., :2")));
        NDArray overlap_right_bottom = NDArrays.minimum(boxes0.get(new NDIndex("..., 2:")), boxes1.get(new NDIndex("..., 2:")));
        NDArray overlap_area = area_of(overlap_left_top, overlap_right_bottom);
        NDArray area0 = area_of(boxes0.get(new NDIndex("..., :2")), boxes0.get(new NDIndex("..., 2:")));
        NDArray area1 = area_of(boxes1.get(new NDIndex("..., :2")), boxes1.get(new NDIndex("..., 2:")));
        return overlap_area.div(area0.add(area1).sub(overlap_area).add(Math.exp(-5)));

    }

    private NDArray area_of(NDArray left_top, NDArray right_bottom) {
        NDArray hw = right_bottom.sub(left_top).clip(0.0f, Float.MAX_VALUE);
        return hw.get(new NDIndex("..., 0")).mul(hw.get(new NDIndex("..., 1")));
    }

    @Override
    public Batchifier getBatchifier() {
        return null;
    }
}
