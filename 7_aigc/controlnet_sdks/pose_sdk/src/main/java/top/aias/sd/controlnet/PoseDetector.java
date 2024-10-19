package top.aias.sd.controlnet;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import top.aias.sd.utils.NDArrayUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class PoseDetector implements AutoCloseable {
    private int detect_resolution = 512;
    private int image_resolution = 512;
    ZooModel model;
    Predictor<Image, NDList> predictor;
    private Device device;
    public PoseDetector(int detect_resolution, int image_resolution, Device device) throws ModelException, IOException {
        this.detect_resolution = detect_resolution;
        this.image_resolution = image_resolution;
        this.device = device;
        this.model = ModelZoo.loadModel(criteria());
        this.predictor = model.newPredictor();
    }

    public NDList predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, NDList> criteria() {

        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get("models/body.pt"))
                        .optDevice(device)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private final class FeatureTranslator implements Translator<Image, NDList> {
        protected Batchifier batchifier = Batchifier.STACK;
        private float scale_search = 0.5f;
        private int boxsize = 368;
        private int stride = 8;
        private int padValue = 128;
        private float thre1 = 0.1f;
        private float thre2 = 0.05f;
        int[] pad = new int[4];
        NDArray heatmap_avg;
        NDArray paf_avg;
        NDArray img_padded;
        NDArray oriImg;
        private int width;
        private int height;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDManager manager = ctx.getNDManager();

            width = input.getWidth();
            height = input.getHeight();
            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

            int[] hw = resize64(height, width, detect_resolution);
            array = NDImageUtils.resize(array, hw[1], hw[0], Image.Interpolation.AREA);
            oriImg = array;

            Shape shape = array.getShape();

            float scale = (scale_search * boxsize) / (float) shape.get(0);

            shape = new Shape(shape.get(0), shape.get(1), 19);
            heatmap_avg = manager.zeros(shape);

            shape = new Shape(shape.get(0), shape.get(1), 38);
            paf_avg = manager.zeros(shape);

            array = NDImageUtils.resize(array, (int) (hw[1] * scale), (int) (hw[0] * scale), Image.Interpolation.BICUBIC);

            int h = (int) array.getShape().get(0);
            int w = (int) array.getShape().get(1);
            // padRightDownCorner
            pad[0] = 0; // up
            pad[1] = 0; // left
            if ((h % stride) == 0) {
                pad[2] = 0; // down
            } else {
                pad[2] = stride - (h % stride);
            }
            if ((w % stride) == 0) {
                pad[3] = 0; // right
            } else {
                pad[3] = stride - (w % stride);
            }

            img_padded = manager.zeros(new Shape(array.getShape().get(0) + pad[2], array.getShape().get(1) + pad[3], array.getShape().get(2)));
            img_padded = img_padded.add(padValue);
            img_padded.set(new NDIndex(":" + array.getShape().get(0) + ",:" + array.getShape().get(1) + ",:3"), array);

            img_padded = img_padded.toType(DataType.FLOAT32, false);
            img_padded = img_padded.div(256).sub(0.5);
            img_padded = img_padded.transpose(2, 0, 1);
//            img_padded = img_padded.expandDims(0);
            return new NDList(img_padded);
        }

        @Override
        public NDList processOutput(TranslatorContext ctx, NDList list) {
            try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {  //PyTorch MXNet

//                NDManager manager = ctx.getNDManager();

                NDArray Mconv7_stage6_L1 = list.get(0);
                NDArray Mconv7_stage6_L2 = list.get(1);

                // extract outputs, resize, and remove padding
                // heatmap = np.transpose(np.squeeze(net.blobs[output_blobs.keys()[1]].data), (1, 2, 0))  # output 1 is heatmaps
                NDArray heatmap = Mconv7_stage6_L2.transpose(1, 2, 0);
                heatmap = NDImageUtils.resize(heatmap, (int) (heatmap.getShape().get(1) * stride), (int) (heatmap.getShape().get(0) * stride), Image.Interpolation.BICUBIC);
                int val1 = (int) img_padded.getShape().get(1) - pad[2];
                int val2 = (int) img_padded.getShape().get(2) - pad[3];
                NDIndex ndIndex = new NDIndex(":" + val1 + ",:" + val2 + ",:");
                heatmap = heatmap.get(ndIndex);
                heatmap = NDImageUtils.resize(heatmap, (int) oriImg.getShape().get(1), (int) oriImg.getShape().get(0), Image.Interpolation.BICUBIC);

                // paf = np.transpose(np.squeeze(net.blobs[output_blobs.keys()[0]].data), (1, 2, 0))  # output 0 is PAFs
                NDArray paf = Mconv7_stage6_L1.transpose(1, 2, 0);
                paf = NDImageUtils.resize(paf, (int) (paf.getShape().get(1) * stride), (int) (paf.getShape().get(0) * stride), Image.Interpolation.BICUBIC);
                paf = paf.get(ndIndex);
                paf = NDImageUtils.resize(paf, (int) oriImg.getShape().get(1), (int) oriImg.getShape().get(0), Image.Interpolation.BICUBIC);

                heatmap_avg = heatmap_avg.add(heatmap);
                paf_avg = paf_avg.add(paf);
                int peak_counter = 0;
                ArrayList<float[][]> all_peaks = new ArrayList();

                for (int part = 0; part < 18; part++) {
                    NDArray map_ori = heatmap_avg.get(new NDIndex(":,:," + part));
                    // 高斯滤波器(GaussianFilter)对图像进行平滑处理
                    org.opencv.core.Mat src = NDArrayUtils.floatNDArrayToMat(map_ori);
                    org.opencv.core.Mat mat = src.clone();
                    Imgproc.GaussianBlur(src, mat, new Size(3, 3), 3.0f);
                    float[][] floats = NDArrayUtils.matToFloatArray(mat);
                    NDArray one_heatmap = manager.create(floats);

                    NDArray map_left = manager.zeros(one_heatmap.getShape());
                    map_left.set(new NDIndex("1:, :"), one_heatmap.get(new NDIndex(":-1, :")));

                    NDArray map_right = manager.zeros(one_heatmap.getShape());
                    map_right.set(new NDIndex(":-1, :"), one_heatmap.get(new NDIndex("1:, :")));

                    NDArray map_up = manager.zeros(one_heatmap.getShape());
                    map_up.set(new NDIndex(":, 1:"), one_heatmap.get(new NDIndex(":, :-1")));

                    NDArray map_down = manager.zeros(one_heatmap.getShape());
                    map_down.set(new NDIndex(":, :-1"), one_heatmap.get(new NDIndex(":, 1:")));

                    NDArray part1 = one_heatmap.gte(map_left);
                    NDArray part2 = one_heatmap.gte(map_right);
                    NDArray part3 = one_heatmap.gte(map_up);
                    NDArray part4 = one_heatmap.gte(map_down);
                    NDArray part5 = one_heatmap.gt(thre1);

                    NDArray peaks_binary = part1.logicalAnd(part2).logicalAnd(part3).logicalAnd(part4).logicalAnd(part5);

                    NDArray nonzero = peaks_binary.nonzero();
                    long[] arr1 = nonzero.get(new NDIndex(":,1")).toLongArray();
                    long[] arr0 = nonzero.get(new NDIndex(":,0")).toLongArray();
                    int peaksLength = arr1.length;
                    float[][] peaks_with_score_and_id = new float[peaksLength][4];
                    for (int i = 0; i < peaksLength; i++) {
                        peaks_with_score_and_id[i][0] = arr1[i];
                        peaks_with_score_and_id[i][1] = arr0[i];
                        peaks_with_score_and_id[i][2] = map_ori.get(arr0[i], arr1[i]).toFloatArray()[0];
                        peaks_with_score_and_id[i][3] = peak_counter + i;
                    }
                    all_peaks.add(peaks_with_score_and_id);
                    peak_counter += peaksLength;
                }

                // find connection in the specified sequence, center 29 is in the position 15
                int[][] limbSeq = {{2, 3}, {2, 6}, {3, 4}, {4, 5}, {6, 7}, {7, 8}, {2, 9}, {9, 10},
                        {10, 11}, {2, 12}, {12, 13}, {13, 14}, {2, 1}, {1, 15}, {15, 17},
                        {1, 16}, {16, 18}, {3, 17}, {6, 18}};

                // the middle joints heatmap correpondence
                int[][] mapIdx = {{31, 32}, {39, 40}, {33, 34}, {35, 36}, {41, 42}, {43, 44}, {19, 20}, {21, 22},
                        {23, 24}, {25, 26}, {27, 28}, {29, 30}, {47, 48}, {49, 50}, {53, 54}, {51, 52},
                        {55, 56}, {37, 38}, {45, 46}};

                NDList connection_all = new NDList();
                ArrayList<Integer> special_k = new ArrayList<>();
                int mid_num = 10;

                for (int k = 0; k < mapIdx.length; k++) {
                    int[] idx = mapIdx[k];
                    NDArray score_mid = paf_avg.get(new NDIndex(":,:," + (idx[0] - 19) + ":" + (idx[1] - 19 + 1)));
                    float[][] candA = all_peaks.get(limbSeq[k][0] - 1); // peaks_with_score_and_id
                    float[][] candB = all_peaks.get(limbSeq[k][1] - 1);
                    int nA = candA.length;
                    int nB = candB.length;
                    int indexA = limbSeq[k][0];
                    int indexB = limbSeq[k][1];
                    if (nA != 0 && nB != 0) {
                        ArrayList<float[]> connection_candidates = new ArrayList();
                        for (int i = 0; i < nA; i++) {
                            for (int j = 0; j < nB; j++) {
                                float vec0 = candB[j][0] - candA[i][0];
                                float vec1 = candB[j][1] - candA[i][1];
                                float norm = (float) Math.sqrt(vec0 * vec0 + vec1 * vec1);
                                norm = Math.max(0.001f, norm);
                                vec0 = vec0 / norm;
                                vec1 = vec1 / norm;

                                NDArray startend = manager.linspace(candA[i][0], candB[j][0], mid_num);
                                float[] startend_0 = startend.toFloatArray();
                                startend = manager.linspace(candA[i][1], candB[j][1], mid_num);
                                float[] startend_1 = startend.toFloatArray();

                                float[] vec_x = new float[mid_num];
                                for (int I = 0; I < mid_num; I++) {
                                    int a = Math.round(startend_1[I]);
                                    int b = Math.round(startend_0[I]);
                                    vec_x[I] = score_mid.get(new NDIndex(a + "," + b + ",0")).toFloatArray()[0];
                                }

                                float[] vec_y = new float[mid_num];
                                for (int I = 0; I < mid_num; I++) {
                                    int a = Math.round(startend_1[I]);
                                    int b = Math.round(startend_0[I]);
                                    vec_y[I] = score_mid.get(new NDIndex(a + "," + b + ",1")).toFloatArray()[0];
                                }

                                float sum = 0;
                                int len = 0;
                                float[] score_midpts = new float[mid_num];
                                for (int I = 0; I < mid_num; I++) {
                                    score_midpts[I] = vec_x[I] * vec0 + vec_y[I] * vec1;
                                    if (score_midpts[I] > thre2) {
                                        len = len + 1;
                                    }
                                    sum += score_midpts[I];
                                }

                                float score_with_dist_prior = sum / score_midpts.length + Math.min(0.5f * oriImg.getShape().get(0) / norm - 1, 0);

                                boolean criterion1 = false;
                                if (len > (0.8 * score_midpts.length)) {
                                    criterion1 = true;
                                }
                                boolean criterion2 = false;
                                if (score_with_dist_prior > 0) {
                                    criterion2 = true;
                                }
                                if (criterion1 && criterion2) {
                                    float[] connection_candidate = new float[4];
                                    connection_candidate[0] = i;
                                    connection_candidate[1] = j;
                                    connection_candidate[2] = score_with_dist_prior;
                                    connection_candidate[3] = score_with_dist_prior + candA[i][2] + candB[j][2];
                                    connection_candidates.add(connection_candidate);
                                }

                            }
                        }

                        ArrayList<float[]> sorted_connection_candidates = new ArrayList(); // 降序排列

                        int size = connection_candidates.size();
                        for (int i = 0; i < size; i++) {
                            float max = -1;
                            int index = 0;
                            for (int j = 0; j < connection_candidates.size(); j++) {
                                if (connection_candidates.get(j)[2] > max) {
                                    max = connection_candidates.get(j)[2];
                                    index = j;
                                }
                            }
                            sorted_connection_candidates.add(connection_candidates.remove(index));
                        }

                        NDArray connection = manager.zeros(new Shape(0, 5));
                        for (int c = 0; c < sorted_connection_candidates.size(); c++) {
                            int i = (int) sorted_connection_candidates.get(c)[0];
                            int j = (int) sorted_connection_candidates.get(c)[1];
                            float s = sorted_connection_candidates.get(c)[2];
                            float[] iArr = connection.get(new NDIndex(":, 3")).toFloatArray();
                            float[] jArr = connection.get(new NDIndex(":, 4")).toFloatArray();
                            boolean found = false;
                            for (int index = 0; index < iArr.length; index++) {
                                if (iArr[index] == i) {
                                    found = true;
                                    break;
                                }
                                if (jArr[index] == j) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                float[] con = new float[]{candA[i][3], candB[j][3], s, i, j};

                                NDArray newCon = manager.create(con).reshape(1, 5);
                                connection = connection.concat(newCon, 0); // vstack
                                if (connection.getShape().get(0) >= Math.min(nA, nB)) {
                                    break;
                                }
                            }
                        }
                        connection_all.add(connection);
                    } else {
                        special_k.add(k);
                        connection_all.add(null);
                    }
                }

                // last number in each row is the total parts number of that person
                // the second last number in each row is the score of the overall configuration
                NDArray subset = manager.ones(new Shape(0, 20)).neg();

                NDArray candidate = null;
                for (int i = 0; i < all_peaks.size(); i++) {
                    float[][] item = all_peaks.get(i);
                    if (item != null && item.length > 0) {
                        for (int j = 0; j < item.length; j++) {
                            if (candidate == null) {
                                candidate = manager.create(item[j]).reshape(1, 4);
                            } else {
                                candidate = candidate.concat(manager.create(item[j]).reshape(1, 4), 0);
                            }
                        }
                    }
                }

                for (int k = 0; k < mapIdx.length; k++) {
                    if (!special_k.contains(k)) {
                        NDArray partAs = connection_all.get(k).get(new NDIndex(":, 0"));
                        NDArray partBs = connection_all.get(k).get(new NDIndex(":, 1"));

                        int indexA = limbSeq[k][0] - 1;
                        int indexB = limbSeq[k][1] - 1;

                        for (int i = 0; i < connection_all.get(k).getShape().get(0); i++) {
                            int found = 0;
                            int[] subset_idx = new int[]{-1, -1};
                            for (int j = 0; j < subset.getShape().get(0); j++) {
                                if (subset.get(j, indexA).equals(partAs.get(i)) ||
                                        subset.get(j, indexB).equals(partBs.get(i))) {
                                    subset_idx[found] = j;
                                    found += 1;
                                }
                            }
                            if (found == 1) {
                                int j = subset_idx[0];
                                if (!subset.get(j, indexB).equals(partBs.get(i))) {
                                    subset.set(new NDIndex(j, indexB), partBs.get(i));
                                    subset.set(new NDIndex(j, -1), subset.get(j, -1).add(1));

                                    NDArray value = candidate.get((int) partBs.get(i).toFloatArray()[0], 2);
                                    value = value.add(connection_all.get(k).get(i, 2));
                                    subset.set(new NDIndex(j, -2), subset.get(j, -2).add(value));
                                }
                            } else if (found == 2) { // if found 2 and disjoint, merge them
                                int j1 = subset_idx[0];
                                int j2 = subset_idx[1];

                                NDArray subset_j1 = subset.get(new NDIndex(j1 + ",:")).gte(0).toType(DataType.INT32, false);
                                NDArray subset_j2 = subset.get(new NDIndex(j2 + ",:")).gte(0).toType(DataType.INT32, false);
                                NDArray membership = subset_j1.add(subset_j2).get(new NDIndex(":-2"));
                                NDArray arr = membership.eq(2).nonzero();
                                if (arr.getShape().get(0) == 0) { // merge
                                    // subset[j1][:-2] += (subset[j2][:-2] + 1)
                                    subset.get(new NDIndex(j1 + ",:-2")).addi(subset.get(new NDIndex(j2 + ",:-2")).add(1));
                                    // subset[j1][-2:] += subset[j2][-2:]
                                    subset.get(new NDIndex(j1 + ",-2:")).addi(subset.get(new NDIndex(j2 + ",-2:")));
                                    // subset[j1][-2] += connection_all[k][i][2]
                                    subset.get(new NDIndex(j1, -2)).addi(connection_all.get(k).get(i, 2));
                                } else {
                                    subset.set(new NDIndex(j1, indexB), partBs.get(i));
                                    subset.set(new NDIndex(j1, -1), subset.get(j1, -1).add(1));

                                    NDArray value = candidate.get((int) partBs.get(i).toFloatArray()[0], 2);
                                    value = value.add(connection_all.get(k).get(i, 2));
                                    subset.set(new NDIndex(j1, -2), subset.get(j1, -2).add(value));
                                }
                            } else if (found == 0 && k < 17) {
                                NDArray row = manager.ones(new Shape(20)).neg();

                                row.set(new NDIndex(indexA), partAs.get(i));
                                row.set(new NDIndex(indexB), partBs.get(i));
                                row.set(new NDIndex(-1), 2);
                                int index = (int) connection_all.get(k).get(new NDIndex(i + ",:2")).toFloatArray()[0];
                                float v = candidate.get(index, 2).sum().toFloatArray()[0] + connection_all.get(k).get(i, 2).toFloatArray()[0];
                                row.set(new NDIndex(-2), v);
                                subset = subset.concat(row.reshape(1, 20), 0);
                            }
                        }

                    }
                }

                ArrayList<Integer> deleteIdx = new ArrayList<>();
                for (int i = 0; i < subset.getShape().get(0); i++) {
                    if (subset.get(i, -1).toFloatArray()[0] < 4 ||
                            (subset.get(i, -2).toFloatArray()[0] / subset.get(i, -1).toFloatArray()[0] < 0.4f)) {
                        deleteIdx.add(i);
                    }
                }

                NDArray newSubset = manager.create(new Shape(0, 20));
                for (int i = 0; i < subset.getShape().get(0); i++) {
                    if (!deleteIdx.contains(i)) {
                        newSubset = newSubset.concat(subset.get(new NDIndex(i + ",:")).reshape(1, 20));
                    }
                }


                NDList ndList = new NDList();
                candidate.detach();
                newSubset.detach();
                oriImg.detach();
                ndList.add(candidate);
                ndList.add(newSubset);
                ndList.add(oriImg);

                return ndList;
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
