package me.aias.sd.controlnet;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;
import me.aias.sd.utils.NDArrayUtils;
import me.aias.sd.utils.PoseUtils;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public final class HandEstimation {
    protected Batchifier batchifier = Batchifier.STACK;
    private float[] scale_search = new float[]{0.5f, 1.0f, 1.5f, 2.0f};
    private int boxsize = 368;
    private int stride = 8;
    private int padValue = 128;
    private float thre = 0.05f; // 0.05f

    public HandEstimation() {
    }

    public ArrayList<ArrayList<float[]>> hands(NDManager manager, HandDetector handPredictor, NDArray oriImg, ArrayList<int[]> hands_list) throws TranslateException {
        ArrayList<ArrayList<float[]>> hands = new ArrayList<>();
        float W = oriImg.getShape().get(1);
        float H = oriImg.getShape().get(0);

        for (int i = 0; i < hands_list.size(); i++) {
            int[] handArr = hands_list.get(i);
            int x = handArr[0];
            int y = handArr[1];
            int w = handArr[2];
            int is_left = handArr[3];
            NDArray subImg = oriImg.get(new NDIndex(y + ":" + (y + w) + "," + x + ":" + (x + w) + ",:"));
//                img = ImageFactory.getInstance().fromNDArray(subImg);
//                ImageUtils.saveImage(img, "hand_" + i + ".png", "build/output");

            ArrayList<float[]> peaks = estimation(manager, handPredictor, subImg);

            for (int index = 0; index < peaks.size(); index++) {
                float[] item = peaks.get(index);
                if (item[0] < Math.exp(-6)) {
                    item[0] = -1 / (float) W;
                } else {
                    item[0] = (item[0] + x) / (float) W;
                }
                if (item[1] < Math.exp(-6)) {
                    item[1] = -1 / (float) W;
                } else {
                    item[1] = (item[1] + y) / (float) H;
                }
            }
            hands.add(peaks);
        }

        return hands;
    }

    public ArrayList<float[]> estimation(NDManager manager, HandDetector handPredictor, NDArray oriImg) throws TranslateException {
        int width = (int) oriImg.getShape().get(0);
        int height = (int) oriImg.getShape().get(1);

        Shape shape = oriImg.getShape();

        float[] multiplier = new float[scale_search.length];
        for (int i = 0; i < multiplier.length; i++) {
            multiplier[i] = scale_search[i] * boxsize / shape.get(0);
        }

        shape = new Shape(shape.get(0), shape.get(1), 22);
        NDArray heatmap_avg = manager.zeros(shape);

        for (int m = 0; m < multiplier.length; m++) {
            float scale = multiplier[m];
            NDArray imageToTest = NDImageUtils.resize(oriImg, (int) (height * scale), (int) (width * scale), Image.Interpolation.BICUBIC);

            // padRightDownCorner
            int[] pad = new int[4];
            NDArray img_padded = PoseUtils.padRightDownCorner(manager,imageToTest,pad,stride,padValue);
//            Image img = ImageFactory.getInstance().fromNDArray(img_padded);
//            ImageUtils.saveImage(img, "hand_m_" + m + ".png", "build/output");

            img_padded = img_padded.toType(DataType.FLOAT32, false);
            img_padded = img_padded.div(256).sub(0.5);
            img_padded = img_padded.transpose(2, 0, 1);

            img_padded = img_padded.flip(0);
            NDArray ndArray = handPredictor.predict(img_padded);
            ndArray.attach(manager);

            // extract outputs, resize, and remove padding
            NDArray heatmap = ndArray.transpose(1, 2, 0);
            heatmap = NDImageUtils.resize(heatmap, (int) (heatmap.getShape().get(1) * stride), (int) (heatmap.getShape().get(0) * stride), Image.Interpolation.BICUBIC);
            int val1 = (int) img_padded.getShape().get(1) - pad[2];
            int val2 = (int) img_padded.getShape().get(2) - pad[3];
            NDIndex ndIndex = new NDIndex(":" + val1 + ",:" + val2 + ",:");
            heatmap = heatmap.get(ndIndex);
            heatmap = NDImageUtils.resize(heatmap, (int) oriImg.getShape().get(1), (int) oriImg.getShape().get(0), Image.Interpolation.BICUBIC);
            heatmap_avg = heatmap_avg.add(heatmap);

        }

        heatmap_avg.divi(multiplier.length);

        ArrayList<float[]> all_peaks = new ArrayList();

        for (int part = 0; part < 21; part++) {
            NDArray map_ori = heatmap_avg.get(new NDIndex(":,:," + part));
            // 高斯滤波器(GaussianFilter)对图像进行平滑处理
            org.opencv.core.Mat src = NDArrayUtils.floatNDArrayToMat(map_ori);
            org.opencv.core.Mat mat = src.clone();
            Imgproc.GaussianBlur(src, mat, new Size(0, 0), 3.0f);
            float[][] floats = NDArrayUtils.matToFloatArray(mat);
            NDArray one_heatmap = manager.create(floats);
            NDArray binary = one_heatmap.gt(thre);
            binary = binary.toType(DataType.INT32, false);
            if (binary.sum().equals(0)) {
                all_peaks.add(new float[]{0f, 0f});
                continue;
            }

            int w = (int) binary.getShape().get(0);
            int h = (int) binary.getShape().get(1);
            int[] arr = binary.toIntArray();
            int[][] binaryArr = new int[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    binaryArr[i][j] = arr[i * w + j];
                }
            }

            Pair<int[][], Integer> pair = PoseUtils.label(binaryArr);
            NDArray label_img = manager.create(pair.getKey());
            int label_numbers = pair.getValue();

            float max = -1;
            int max_index = 0;
            for (int i = 1; i < label_numbers + 1; i++) {
                float val = map_ori.mul(label_img.eq(i).toType(DataType.FLOAT32, false)).sum().toFloatArray()[0];
                if (val > max) {
                    max_index = i;
                    max = val;
                }
            }

            label_img.set(label_img.neq(max_index), 0);
            map_ori.set(label_img.eq(0), 0f);
            all_peaks.add(PoseUtils.npmax(map_ori));
        }

        return all_peaks;
    }
}
