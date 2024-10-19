package top.aias.sd.controlnet;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;

import java.util.ArrayList;

public final class FaceEstimation {
    protected Batchifier batchifier = Batchifier.STACK;

    public FaceEstimation() {
    }

    public ArrayList<ArrayList<float[]>> faces(NDManager manager,FaceDetector faceDetector, NDArray oriImg, ArrayList<int[]> faces_list) throws TranslateException {
        ArrayList<ArrayList<float[]>> faces = new ArrayList<>();
        float W = oriImg.getShape().get(1);
        float H = oriImg.getShape().get(0);

        for (int i = 0; i < faces_list.size(); i++) {
            int[] faceArr = faces_list.get(i);
            int x = faceArr[0];
            int y = faceArr[1];
            int w = faceArr[2];

            NDArray subImg = oriImg.get(new NDIndex(y + ":" + (y + w) + "," + x + ":" + (x + w) + ",:"));
//                img = ImageFactory.getInstance().fromNDArray(subImg);
//                ImageUtils.saveImage(img, "hand_" + i + ".png", "build/output");

            ArrayList<float[]> peaks = estimation(manager, faceDetector, subImg);

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
            faces.add(peaks);
        }

        return faces;
    }

    public ArrayList<float[]> estimation(NDManager manager, FaceDetector faceDetector, NDArray face_img) throws TranslateException {
        int width = (int) face_img.getShape().get(0);
        int height = (int) face_img.getShape().get(1);

        NDArray ndArray = faceDetector.predict(face_img);
        ndArray.attach(manager);

        ndArray = ndArray.transpose(1, 2, 0);
        NDArray heatmaps = NDImageUtils.resize(ndArray, width, height, Image.Interpolation.AREA);
        heatmaps = heatmaps.transpose(2, 0, 1);

        ArrayList<float[]> all_peaks = new ArrayList();

        for (int part = 0; part < heatmaps.getShape().get(0); part++) {
            NDArray map_ori = heatmaps.get(part);
            NDArray binary = map_ori.gt(0.05f);
            binary = binary.toType(DataType.INT32, false);
            if (binary.sum().equals(0)) {
                continue;
            }

            NDArray positions = binary.gt(0.5);
            positions = positions.toType(DataType.INT32, false);
            NDArray intensities = map_ori.mul(positions);
            float[] arr = intensities.toFloatArray();
            int cols = (int) intensities.getShape().get(0);
            int rows = (int) intensities.getShape().get(1);

            int x = 0;
            int y = 0;
            float max = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (arr[row * rows + col] > max) {
                        x = row;
                        y = col;
                        max = arr[row * rows + col];
                    }
                }
            }

            all_peaks.add(new float[]{y, x});
        }

        return all_peaks;
    }
}
