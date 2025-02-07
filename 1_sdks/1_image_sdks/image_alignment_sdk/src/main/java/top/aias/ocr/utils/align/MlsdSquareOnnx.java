package top.aias.ocr.utils.align;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.opencv.core.Mat;
import top.aias.ocr.utils.common.ImageUtils;
import top.aias.ocr.utils.opencv.NDArrayUtils;
import top.aias.ocr.utils.opencv.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * 文本转正对齐
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class MlsdSquareOnnx implements AutoCloseable {
    private float thr_v = 0.1f;
    private float thr_d = 0.1f;

    private int detect_resolution = 512;

    private boolean drawSquare;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;

    public MlsdSquareOnnx(Device device, boolean drawSquare) throws ModelException, IOException {
        this.device = device;
        this.model = ModelZoo.loadModel(criteria());
        this.predictor = model.newPredictor();
        this.drawSquare = drawSquare;
    }

    public Image predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, Image> criteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/mlsd_traced_model.onnx"))
                        .optDevice(device)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

    private final class FeatureTranslator implements Translator<Image, Image> {
        protected Batchifier batchifier = Batchifier.STACK;
        private int topk_n = 200;
        private int ksize = 3;
        private float score = 0.06f;
        private float outside_ratio = 0.28f;
        private float inside_ratio = 0.45f;
        private float w_overlap = 0.0f;
        private float w_degree = 1.95f;
        private float w_length = 0.0f;
        private float w_area = 1.86f;
        private float w_center = 0.1f;
        private NDArray imgArray;
        private int original_shape[] = new int[2];
        private int input_shape[] = new int[2];

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {
                original_shape[1] = input.getWidth(); // w - input_shape[1]
                original_shape[0] = input.getHeight();  // h - input_shape[0]

                NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

                array = array.toType(DataType.UINT8, false);

                imgArray = array;

                input_shape = resize64(original_shape[0], original_shape[1], detect_resolution);

                array = NDImageUtils.resize(array, input_shape[1], input_shape[0], Image.Interpolation.AREA);

                NDArray ones = manager.ones(new Shape(array.getShape().get(0), array.getShape().get(1), 1), DataType.UINT8);

                array = array.concat(ones, -1);

                array = array.transpose(2, 0, 1); // HWC -> CHW RGB

                array = array.toType(DataType.FLOAT32, false);

                array = array.div(127.5f).sub(1.0f);

                array = array.flip(0);

                return new NDList(array);
            }
        }

        @Override
        public Image processOutput(TranslatorContext ctx, NDList list) {
            try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {

                NDArray tpMap = list.singletonOrThrow();

                // deccode_output_score_and_ptss(tpMap, topk_n = 200, ksize = 3) start
                int width = (int) (tpMap.getShape().get(2));

                NDArray displacement = tpMap.get("1:5, :, :");

                NDArray center = tpMap.get("0, :, :");

                // Sigmoid 函数，即f(x)=1/(1+e-x)
                NDArray heat = NDArrayUtils.Sigmoid(center);

                NDArray hmax = NDArrayUtils.maxPool(manager, heat, ksize, 1, (ksize - 1) / 2);

                NDArray keep = hmax.eq(heat);
                keep = keep.toType(DataType.FLOAT32, false);

                heat = heat.mul(keep);
                heat = heat.reshape(-1);

                NDArray indices = heat.argSort(-1, false).get("0:200");
                NDArray pts_score = heat.get(indices);
                indices = indices.toType(DataType.FLOAT32, true);
                NDArray yy = indices.div(width).floor().expandDims(-1);
                NDArray xx = indices.mod(width).expandDims(-1);
                NDArray pts = yy.concat(xx, -1);

                NDArray vmap = displacement.transpose(1, 2, 0);
                // deccode_output_score_and_ptss  end

                NDArray start = vmap.get(":, :, :2");
                NDArray end = vmap.get(":, :, 2:");

                NDArray dist_map = start.sub(end).pow(2).sum(new int[]{-1}).sqrt();

                ArrayList<float[]> junc_list = new ArrayList<>();
                ArrayList<float[]> segments_list = new ArrayList<>();

                for (int i = 0; i < pts_score.size(); i++) {
                    center = pts.get(i);
                    int y = (int) center.getFloat(0);
                    int x = (int) center.getFloat(1);
                    float score = pts_score.getFloat(i);
                    float distance = dist_map.getFloat(y, x);

                    if (score > this.score && distance > 20.0f) {
                        float[] junc = new float[2];
                        junc[0] = x;
                        junc[1] = y;
                        junc_list.add(junc);

                        NDArray array = vmap.get(y + "," + x + ",:");  // y, x, :
                        float disp_x_start = array.getFloat(0);
                        float disp_y_start = array.getFloat(1);
                        float disp_x_end = array.getFloat(2);
                        float disp_y_end = array.getFloat(3);

                        float x_start = x + disp_x_start;
                        float y_start = y + disp_y_start;
                        float x_end = x + disp_x_end;
                        float y_end = y + disp_y_end;

                        float[] segment = new float[4];
                        segment[0] = x_start;
                        segment[1] = y_start;
                        segment[2] = x_end;
                        segment[3] = y_end;

                        segments_list.add(segment);
                    }
                }

                float[][] segmentsArr = new float[segments_list.size()][4];
                for (int i = 0; i < segments_list.size(); i++) {
                    float[] item = segments_list.get(i);
                    segmentsArr[i][0] = item[0];
                    segmentsArr[i][1] = item[1];
                    segmentsArr[i][2] = item[2];
                    segmentsArr[i][3] = item[3];
                }

                NDArray segments = manager.create(segmentsArr).toType(DataType.FLOAT32, false);

                // ####### post processing for squares
                // 1. get unique lines
                start = segments.get(":, :2");
                end = segments.get(":, 2:");
                NDArray diff = start.sub(end);

                NDArray a = diff.get(":, 1");
                NDArray b = diff.get(":, 0").neg();
                NDArray c = a.mul(start.get(":, 0")).add(b.mul(start.get(":, 1")));
                NDArray d = c.abs().div(a.square().add(b.square().add(Math.exp(-10))).sqrt());

                NDArray theta = NDArrayUtils.arctan2(diff.get(":, 0"), diff.get(":, 1"));
                NDArray index = theta.lt(0.0f);
                index = index.toType(DataType.INT32, false).mul(180);
                theta = theta.add(index);

                NDArray hough = d.expandDims(1).concat(theta.expandDims(1), -1);

                int d_quant = 1;
                int theta_quant = 2;
                hough.get(":, 0").divi(d_quant);
                hough.get(":, 1").divi(theta_quant);
                hough = hough.floor();
                float[][] houghArr = NDArrayUtils.floatNDArrayToArray(hough);

                NDList ndList = hough.unique(0, true, false, true);
                // 唯一的元素列表
                NDArray yx_indices = ndList.get(0).toType(DataType.INT32, false);
                int[][] yx_indicesArr = NDArrayUtils.intNDArrayToArray(yx_indices);
                int[] inds = new int[yx_indicesArr.length];
                // 唯一的元素,对应的数量
                NDArray counts = ndList.get(2);
                long[] countsArr = counts.toLongArray();

                for (int i = 0; i < yx_indicesArr.length; i++) {
                    for (int j = 0; j < houghArr.length; j++) {
                        if (yx_indicesArr[i][0] == houghArr[j][0] && yx_indicesArr[i][1] == houghArr[j][1]) {
                            inds[i] = j;
                            break;
                        }
                    }
                }


                NDArray acc_map = manager.zeros(new Shape(512 / d_quant + 1, 360 / theta_quant + 1), DataType.FLOAT32);
                NDArray idx_map = manager.zeros(new Shape(512 / d_quant + 1, 360 / theta_quant + 1), DataType.INT32).sub(1);

                for (int i = 0; i < yx_indicesArr.length; i++) {
                    acc_map.set(new NDIndex(yx_indicesArr[i][0], yx_indicesArr[i][1]), countsArr[i]);
                    idx_map.set(new NDIndex(yx_indicesArr[i][0], yx_indicesArr[i][1]), inds[i]);
                }

                float[][] acc_map_np = NDArrayUtils.floatNDArrayToArray(acc_map);

                NDArray max_acc_map = NDArrayUtils.maxPool(manager, acc_map, 5, 1, 2);


                keep = acc_map.eq(max_acc_map);
                keep = keep.toType(DataType.FLOAT32, false);
                acc_map = acc_map.mul(keep);
                NDArray flatten_acc_map = acc_map.flatten();

                indices = flatten_acc_map.argSort(-1, false).get("0:200");

                NDArray scores = flatten_acc_map.get(indices);
                int cols = (int) (acc_map.getShape().get(1));
                yy = indices.div(cols).floor().expandDims(-1);
                xx = indices.mod(cols).expandDims(-1);
                NDArray yx = yy.concat(xx, -1);
                float[][] yx_arr = NDArrayUtils.floatNDArrayToArray(yx);
                float[] topk_values = scores.toFloatArray();
                int[][] idx_map_arr = NDArrayUtils.intNDArrayToArray(idx_map);

                int[] indices_arr = new int[yx_arr.length];
                for (int i = 0; i < yx_arr.length; i++) {
                    indices_arr[i] = idx_map_arr[(int) yx_arr[i][0]][(int) yx_arr[i][1]];
                }

                int basis = 5 / 2;
                NDArray merged_segments = manager.zeros(new Shape(0, 4), DataType.FLOAT32);
                for (int i = 0; i < yx_arr.length; i++) {
                    float[] yx_pt = yx_arr[i];
                    float y = yx_pt[0];
                    float x = yx_pt[1];
                    int max_indice = indices_arr[i];
                    float value = topk_values[i];
                    if (max_indice == -1 || value == 0) {
                        continue;
                    }

                    NDList segment_list = new NDList();
                    for (int y_offset = -basis; y_offset < basis + 1; y_offset++) {
                        for (int x_offset = -basis; x_offset < basis + 1; x_offset++) {
                            if (y + y_offset < 0 || x + x_offset < 0) {
                                continue;
                            }
                            int indice = idx_map_arr[(int) (y + y_offset)][(int) (x + x_offset)];
                            int cnt = (int) acc_map_np[(int) (y + y_offset)][(int) (x + x_offset)];
                            if (indice != -1) {
                                segment_list.add(segments.get(indice));
                            }
                            if (cnt > 1) {
                                int check_cnt = 1;
                                NDArray current_hough = hough.get(indice);
                                for (int new_indice = 0; new_indice < hough.size(0); new_indice++) {
                                    NDArray new_hough = hough.get(new_indice);
                                    if (current_hough.eq(new_hough).all().toBooleanArray()[0] && indice != new_indice) {
                                        segment_list.add(segments.get(new_indice));
                                        check_cnt += 1;
                                        if (check_cnt == cnt)
                                            break;
                                    }
                                }

                            }
                        }
                    }

                    NDArray group_segments = NDArrays.concat(segment_list).reshape(-1, 2);
                    NDArray sorted_group_segments = group_segments.sort(0);

                    float[] min = sorted_group_segments.get("0, :").toFloatArray();
                    float[] max = sorted_group_segments.get("-1, :").toFloatArray();
                    float x_min = min[0];
                    float y_min = min[1];
                    float x_max = max[0];
                    float y_max = max[1];

                    float deg = theta.get(max_indice).toFloatArray()[0];
                    if (deg >= 90) {
                        merged_segments = merged_segments.concat(manager.create(new float[]{x_min, y_max, x_max, y_min}).reshape(1, 4));
                    } else {
                        merged_segments = merged_segments.concat(manager.create(new float[]{x_min, y_min, x_max, y_max}).reshape(1, 4));
                    }
                }

                //  2. get intersections
                NDArray new_segments = merged_segments;

                start = new_segments.get(":, :2"); //  (x1, y1)
                end = new_segments.get(":, 2:"); // (x2, y2)
                NDArray new_centers = start.add(end).div(2.0f);
                diff = start.sub(end);
                NDArray dist_segments = diff.square().sum(new int[]{-1}).sqrt();

                // ax + by = c
                a = diff.get(":, 1");
                b = diff.get(":, 0").neg();
                c = a.mul(start.get(":, 0")).add(b.mul(start.get(":, 1")));

                NDArray pre_det = a.expandDims(1).mul(b.expandDims(0));
                NDArray det = pre_det.sub(pre_det.transpose());
                NDArray pre_inter_y = a.expandDims(1).mul(c.expandDims(0));
                NDArray inter_y = pre_inter_y.sub(pre_inter_y.transpose()).div(det.add(Math.exp(-10)));
                NDArray pre_inter_x = c.expandDims(1).mul(b.expandDims(0));
                NDArray inter_x = pre_inter_x.sub(pre_inter_x.transpose()).div(det.add(Math.exp(-10)));
                NDArray inter_pts = inter_x.expandDims(2).concat(inter_y.expandDims(2), -1).toType(DataType.INT32, false);

                // 3. get corner information
                // 3.1 get distance
                NDArray dist_inter_to_segment1_start = inter_pts.sub(start.expandDims(1)).square().sum(new int[]{-1}, true).sqrt();
                NDArray dist_inter_to_segment1_end = inter_pts.sub(end.expandDims(1)).square().sum(new int[]{-1}, true).sqrt();
                NDArray dist_inter_to_segment2_start = inter_pts.sub(start.expandDims(0)).square().sum(new int[]{-1}, true).sqrt();
                NDArray dist_inter_to_segment2_end = inter_pts.sub(end.expandDims(0)).square().sum(new int[]{-1}, true).sqrt();

                // sort ascending
                NDArray dist_inter_to_segment1 = dist_inter_to_segment1_start.concat(dist_inter_to_segment1_end, -1).sort(-1);
                NDArray dist_inter_to_segment2 = dist_inter_to_segment2_start.concat(dist_inter_to_segment2_end, -1).sort(-1);

                // 3.2 get degree
                NDArray inter_to_start = new_centers.expandDims(1).sub(inter_pts);
                NDArray deg_inter_to_start = NDArrayUtils.arctan2(inter_to_start.get(":, :, 1"), inter_to_start.get(":, :, 0"));
                index = deg_inter_to_start.lt(0.0f);
                index = index.toType(DataType.INT32, false).mul(360);
                deg_inter_to_start = deg_inter_to_start.add(index);

                NDArray inter_to_end = new_centers.expandDims(0).sub(inter_pts);

                // np.arctan2和np.arctan都是计算反正切值的NumPy函数，但它们的参数和返回值不同。一般来说，np.arctan2的参数为(y, x)，
                NDArray deg_inter_to_end = NDArrayUtils.arctan2(inter_to_end.get(":, :, 1"), inter_to_end.get(":, :, 0"));
                index = deg_inter_to_end.lt(0.0f);
                index = index.toType(DataType.INT32, false).mul(360);
                deg_inter_to_end = deg_inter_to_end.add(index);

                // rename variables
                NDArray deg1_map = deg_inter_to_start;
                NDArray deg2_map = deg_inter_to_end;

                // sort deg ascending
                NDArray deg_sort = deg1_map.expandDims(2).concat(deg2_map.expandDims(2), -1).sort(-1);
                NDArray deg_diff_map = deg1_map.sub(deg2_map).abs();
                // we only consider the smallest degree of intersect
                // deg_diff_map[deg_diff_map > 180] = 360 - deg_diff_map[deg_diff_map > 180]
                // x -> 360- x => x + 360 - 2x = 360 - x
                index = deg_diff_map.gt(180);
                NDArray val1 = index.toType(DataType.INT32, false).mul(360);
                NDArray val2 = index.toType(DataType.INT32, false).mul(deg_diff_map).neg().mul(2);

                deg_diff_map = deg_diff_map.add(val1).add(val2);

                //  define available degree range
                int[] deg_range = new int[]{60, 120};
                ArrayList<ArrayList<int[]>> corner_dict = new ArrayList<>();
                ArrayList<int[]> blueList = new ArrayList<>();
                ArrayList<int[]> greenList = new ArrayList<>();
                ArrayList<int[]> blackList = new ArrayList<>();
                ArrayList<int[]> cyanList = new ArrayList<>();
                ArrayList<int[]> redList = new ArrayList<>();

                corner_dict.add(blueList);
                corner_dict.add(greenList);
                corner_dict.add(blackList);
                corner_dict.add(cyanList);
                corner_dict.add(redList);

                NDArray inter_points = manager.zeros(new Shape(0, 2));

                float[] dist_segments_arr = dist_segments.toFloatArray();
                for (int i = 0; i < inter_pts.getShape().get(0); i++) {
                    for (int j = i + 1; j < inter_pts.getShape().get(1); j++) {
                        // i, j > line index, always i < j
                        int[] point1 = inter_pts.get(i + "," + j + ",:").toIntArray();
                        int x = point1[0];
                        int y = point1[1];
                        float[] point2 = deg_sort.get(i + "," + j + ",:").toFloatArray();
                        float deg1 = point2[0];
                        float deg2 = point2[1];
                        float deg_diff = deg_diff_map.getFloat(i, j);
                        boolean check_degree = false;
                        if (deg_diff > deg_range[0] && deg_diff < deg_range[1]) {
                            check_degree = true;
                        }
                        boolean check_distance = false;

                        if (((dist_inter_to_segment1.getFloat(i, j, 1) >= dist_segments_arr[i] &&
                                dist_inter_to_segment1.getFloat(i, j, 0) <= dist_segments_arr[i] * this.outside_ratio) ||
                                (dist_inter_to_segment1.getFloat(i, j, 1) <= dist_segments_arr[i] &&
                                        dist_inter_to_segment1.getFloat(i, j, 0) <= dist_segments_arr[i] * this.inside_ratio)) &&
                                ((dist_inter_to_segment2.getFloat(i, j, 1) >= dist_segments_arr[j] &&
                                        dist_inter_to_segment2.getFloat(i, j, 0) <= dist_segments_arr[j] * this.outside_ratio) ||
                                        (dist_inter_to_segment2.getFloat(i, j, 1) <= dist_segments_arr[j] &&
                                                dist_inter_to_segment2.getFloat(i, j, 0) <= dist_segments_arr[j] * this.inside_ratio))) {
                            check_distance = true;
                        }

                        if (check_degree && check_distance) {
                            int corner_info = 0;
                            if ((deg1 >= 0 && deg1 <= 45 && deg2 >= 45 && deg2 <= 120) ||
                                    (deg2 >= 315 && deg1 >= 45 && deg1 <= 120)) {
                                corner_info = 0; // blue
                            } else if (deg1 >= 45 && deg1 <= 125 && deg2 >= 125 && deg2 <= 225) {
                                corner_info = 1; // green
                            } else if (deg1 >= 125 && deg1 <= 225 && deg2 >= 225 && deg2 <= 315) {
                                corner_info = 2; // black
                            } else if ((deg1 >= 0 && deg1 <= 45 && deg2 >= 225 && deg2 <= 315) ||
                                    (deg2 >= 315 && deg1 >= 225 && deg1 <= 315)) {
                                corner_info = 3; // cyan
                            } else {
                                corner_info = 4; // red  - we don't use it
                                continue;
                            }
                            corner_dict.get(corner_info).add(new int[]{x, y, i, j});
                            inter_points = inter_points.concat(manager.create(new int[]{x, y}).reshape(1, 2));
                        }

                    }
                }

                NDArray square_list = manager.zeros(new Shape(0, 8));
                NDArray connect_list = manager.zeros(new Shape(0, 4));
                NDArray segment_list = manager.zeros(new Shape(0, 8));

                int corner0_line = 0;
                int corner1_line = 0;
                int corner2_line = 0;
                int corner3_line = 0;
                for (int[] corner0 : corner_dict.get(0)) {
                    for (int[] corner1 : corner_dict.get(1)) {
                        boolean connect01 = false;
                        for (int i = 0; i < 2; i++) {
                            corner0_line = corner0[2 + i];
                            for (int j = 0; j < 2; j++) {
                                if (corner0_line == corner1[2 + j]) {
                                    connect01 = true;
                                    break;
                                }
                            }
                        }
                        if (connect01) {
                            for (int[] corner2 : corner_dict.get(2)) {
                                boolean connect12 = false;
                                for (int i = 0; i < 2; i++) {
                                    corner1_line = corner1[2 + i];
                                    for (int j = 0; j < 2; j++) {
                                        if (corner1_line == corner2[2 + j]) {
                                            connect12 = true;
                                            break;
                                        }
                                    }
                                }
                                if (connect12) {
                                    for (int[] corner3 : corner_dict.get(3)) {
                                        boolean connect23 = false;
                                        for (int i = 0; i < 2; i++) {
                                            corner2_line = corner1[2 + i];
                                            for (int j = 0; j < 2; j++) {
                                                if (corner2_line == corner2[2 + j]) {
                                                    connect23 = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (connect23) {
                                            for (int i = 0; i < 2; i++) {
                                                corner3_line = corner3[2 + i];
                                                for (int j = 0; j < 2; j++) {
                                                    if (corner3_line == corner0[2 + j]) {
                                                        square_list = square_list.concat(manager.create(new int[]{corner0[0], corner0[1], corner1[0], corner1[1], corner2[0], corner2[1], corner3[0], corner3[1]}).reshape(1, 8));
                                                        connect_list = connect_list.concat(manager.create(new int[]{corner0_line, corner1_line, corner2_line, corner3_line}).reshape(1, 4));
                                                        segment_list = segment_list.concat(manager.create(new int[]{corner0[2], corner0[3], corner1[2], corner1[3], corner2[2], corner2[3], corner3[2], corner3[3]}).reshape(1, 8));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }

                float map_size = (int) imgArray.getShape().get(0) / 2;
                NDArray squares = square_list.reshape(-1, 4, 2);
                NDArray score_array = null;
                NDArray connect_array = connect_list;
                NDArray segments_array = segment_list.reshape(-1, 4, 2);
                //get degree of corners:

                NDArray squares_rollup = squares.duplicate();
                NDArray last = squares.get(":," + (squares.size(1) - 1) + ",:");
                for (int i = ((int) squares.size(1) - 1); i > 0; i--) {
                    squares_rollup.set(new NDIndex(":," + i + ",:"), squares.get(":," + (i - 1) + ",:"));
                }
                squares_rollup.set(new NDIndex(":,0,:"), last);

                NDArray squares_rolldown = manager.zeros(squares.getShape());
                NDArray first = squares.get(":,0,:");
                for (int i = 0; i < squares.size(1) - 1; i++) {
                    squares_rolldown.set(new NDIndex(":," + i + ",:"), squares.get(":," + (i + 1) + ",:"));
                }
                squares_rolldown.set(new NDIndex(":," + (squares.size(1) - 1) + ",:"), first);

                NDArray vec1 = squares_rollup.sub(squares);
                NDArray normalized_vec1 = vec1.div(vec1.norm(new int[]{-1}, true).add(Math.exp(-10)));

                NDArray vec2 = squares_rolldown.sub(squares);
                NDArray normalized_vec2 = vec2.div(vec2.norm(new int[]{-1}, true).add(Math.exp(-10)));

                NDArray inner_products = normalized_vec1.mul(normalized_vec2).sum(new int[]{-1});

                NDArray squares_degree = inner_products.acos().mul(180).div(Math.PI);

                NDArray overlap_scores = null;
                NDArray degree_scores = null;
                NDArray length_scores = null;

                for (int i = 0; i < connect_array.size(0); i++) {
                    NDArray connects = connect_array.get(i);
                    segments = segments_array.get(i);
                    NDArray square = squares.get(i);
                    NDArray degree = squares_degree.get(i);

                    // ###################################### OVERLAP SCORES
                    float cover = 0;
                    float perimeter = 0;
                    // check 0 > 1 > 2 > 3
                    float[] square_length = new float[4];

                    for (int start_idx = 0; start_idx < 4; start_idx++) {
                        int end_idx = (start_idx + 1) % 4;
                        int connect_idx = (int) connects.get(start_idx).toFloatArray()[0];
                        NDArray start_segments = segments.get(start_idx);
                        NDArray end_segments = segments.get(end_idx);

                        // check whether outside or inside
                        int idx_i = (int) start_segments.toFloatArray()[0];
                        int idx_j = (int) start_segments.toFloatArray()[1];
                        NDArray check_dist_mat;
                        if (connect_idx == idx_i) {
                            check_dist_mat = dist_inter_to_segment1;
                        } else {
                            check_dist_mat = dist_inter_to_segment2;
                        }
                        float[] range = check_dist_mat.get(idx_i + "," + idx_j + ",:").toFloatArray();
                        float min_dist = range[0];
                        float max_dist = range[1];
                        float connect_dist = dist_segments.get(connect_idx).toFloatArray()[0];
                        String start_position;
                        float start_min;
                        int start_cover_param;
                        int start_peri_param;
                        if (max_dist > connect_dist) {
                            start_position = "outside";
                            start_min = min_dist;
                            start_cover_param = 0;
                            start_peri_param = 1;
                        } else {
                            start_position = "inside";
                            start_min = min_dist;
                            start_cover_param = -1;
                            start_peri_param = -1;
                        }

                        // check whether outside or inside
                        idx_i = (int) end_segments.toFloatArray()[0];
                        idx_j = (int) end_segments.toFloatArray()[1];
                        if (connect_idx == idx_i) {
                            check_dist_mat = dist_inter_to_segment1;
                        } else {
                            check_dist_mat = dist_inter_to_segment2;
                        }
                        range = check_dist_mat.get(idx_i + "," + idx_j + ",:").toFloatArray();
                        min_dist = range[0];
                        max_dist = range[1];
                        connect_dist = dist_segments.get(connect_idx).toFloatArray()[0];
                        String end_position;
                        float end_min;
                        int end_cover_param;
                        int end_peri_param;
                        if (max_dist > connect_dist) {
                            end_position = "outside";
                            end_min = min_dist;
                            end_cover_param = 0;
                            end_peri_param = 1;
                        } else {
                            end_position = "inside";
                            end_min = min_dist;
                            end_cover_param = -1;
                            end_peri_param = -1;
                        }

                        cover += connect_dist + start_cover_param * start_min + end_cover_param * end_min;
                        perimeter += connect_dist + start_peri_param * start_min + end_peri_param * end_min;

                        square_length[start_idx] = connect_dist + start_peri_param * start_min + end_peri_param * end_min;
                    }
                    if (overlap_scores == null) {
                        overlap_scores = manager.create(cover / perimeter).reshape(1);
                    } else {
                        overlap_scores = overlap_scores.concat(manager.create(cover / perimeter).reshape(1));
                    }

                    // ######################################
                    // ###################################### DEGREE SCORES
                    float[] degreeArr = degree.toFloatArray();
                    float deg0 = degreeArr[0];
                    float deg1 = degreeArr[1];
                    float deg2 = degreeArr[2];
                    float deg3 = degreeArr[3];
                    float deg_ratio1 = deg0 / deg2;
                    if (deg_ratio1 > 1.0) {
                        deg_ratio1 = 1 / deg_ratio1;
                    }
                    float deg_ratio2 = deg1 / deg3;
                    if (deg_ratio2 > 1.0) {
                        deg_ratio2 = 1 / deg_ratio2;
                    }
                    if (degree_scores == null) {
                        degree_scores = manager.create((deg_ratio1 + deg_ratio2) / 2).reshape(1);
                    } else {
                        degree_scores = degree_scores.concat(manager.create((deg_ratio1 + deg_ratio2) / 2).reshape(1));
                    }

                    // ######################################
                    // ###################################### LENGTH SCORES
                    float len0 = square_length[0];
                    float len1 = square_length[1];
                    float len2 = square_length[2];
                    float len3 = square_length[3];
                    float len_ratio1 = 0;
                    if (len2 > len0) {
                        len_ratio1 = len0 / len2;
                    } else {
                        len_ratio1 = len2 / len0;
                    }
                    float len_ratio2 = 0;
                    if (len3 > len1) {
                        len_ratio2 = len1 / len3;
                    } else {
                        len_ratio2 = len3 / len1;
                    }
                    if (length_scores == null) {
                        length_scores = manager.create((len_ratio1 + len_ratio2) / 2).reshape(1);
                    } else {
                        length_scores = length_scores.concat(manager.create((len_ratio1 + len_ratio2) / 2).reshape(1));
                    }
                }
                if (overlap_scores != null)
                    overlap_scores = overlap_scores.div(overlap_scores.max().toFloatArray()[0]);

                //  ###################################### AREA SCORES
                NDArray area_scores = squares.reshape(new Shape(-1, 4, 2));
                NDArray area_x = area_scores.get(":, :, 0");
                NDArray area_y = area_scores.get(":, :, 1");
                NDArray correction = area_x.get(":, -1").mul(area_y.get(":, 0")).sub(area_y.get(":, -1").mul(area_x.get(":, 0")));

                NDArray area_scores1 = area_x.get(":, :-1").mul(area_y.get(":, 1:")).sum(new int[]{-1});
                NDArray area_scores2 = area_y.get(":, :-1").mul(area_x.get(":, 1:")).sum(new int[]{-1});

                area_scores = area_scores1.sub(area_scores2);
                area_scores = area_scores.add(correction).abs().mul(0.5);
                area_scores = area_scores.div(map_size * map_size);

                // ###################################### CENTER SCORES
                NDArray centers = manager.create(new float[]{256 / 2, 256 / 2});
                NDArray square_centers = squares.mean(new int[]{1});
                NDArray center2center = centers.sub(square_centers).square().sum().sqrt();
                NDArray center_scores = center2center.div(map_size / Math.sqrt(2.0));

                if (overlap_scores != null) {
                    score_array = overlap_scores.mul(this.w_overlap).add(degree_scores.mul(this.w_degree)).add(area_scores.mul(this.w_area)).add(center_scores.mul(this.w_center)).add(length_scores.mul(this.w_length));
                    NDArray sorted_idx = score_array.argSort(0, false);
                    score_array = score_array.get(sorted_idx);
                    squares = squares.get(sorted_idx);
                }

                try {
                    new_segments.get(":, 0").muli(2);
                    new_segments.get(":, 1").muli(2);
                    new_segments.get(":, 2").muli(2);
                    new_segments.get(":, 3").muli(2);
                } catch (Exception e) {
                    new_segments = null;
                }

                try {
                    squares.get(":, :, 0").muli(2).divi(input_shape[1]).muli(original_shape[1]);
                    squares.get(":, :, 1").muli(2).divi(input_shape[0]).muli(original_shape[0]);
                } catch (Exception e) {
                    squares = null;
                    score_array = null;
                }

                try {
                    inter_points.get(":, 0").muli(2);
                    inter_points.get(":, 1").muli(2);
                } catch (Exception e) {
                    inter_points = null;
                }

                Image img = ImageFactory.getInstance().fromNDArray(imgArray);
                Mat mat = (Mat) img.getWrappedImage();
                if (drawSquare) {
                    OpenCVUtils.drawSquares(mat, squares, 1);
                    ImageUtils.saveImage(img, "mlsdSquareBefore.png", "build/output");
                }

                if(squares.getShape().get(0) == 0)
                    return null;

                NDArray maxSquare = squares.get(0);
                float[] points = maxSquare.toFloatArray();
                int[] wh = OpenCVUtils.imgCrop(points);

                Mat dst = OpenCVUtils.perspectiveTransform(mat, points);

                img = ImageFactory.getInstance().fromImage(dst);
//                return img;
                return img.getSubImage(0,0,wh[0],wh[1]);
            }
        }

        private int[] resize64(double h, double w, double resolution) {

            double k = resolution / Math.min(h, w);
            h *= k;
            w *= k;

            int height = (int) (Math.round(h / 64.0)) * 64;
            int width = (int) (Math.round(w / 64.0)) * 64;

            return new int[]{height, width};
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }

    }
}
