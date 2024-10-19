package me.aias.sd.controlnet;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
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
import me.aias.sd.utils.NDArrayUtils;
import me.aias.sd.utils.OpenCVUtils;
import org.opencv.core.Point;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class MlsdDetector implements AutoCloseable {
    private float thr_v = 0.1f;
    private float thr_d = 0.1f;
    private int detect_resolution = 512;
    private int image_resolution = 512;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;
    public MlsdDetector(int detect_resolution, int image_resolution, Device device) throws ModelException, IOException {
        this.detect_resolution = detect_resolution;
        this.image_resolution = image_resolution;
        this.device = device;
        this.model = ModelZoo.loadModel(criteria());
        this.predictor = model.newPredictor();
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
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/pytorch/mlsd.pt"))
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
        private float score_thr = 0.1f;
        private float dist_thr = 0.1f;
        private NDArray imgArray;
        private int width;
        private int height;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {
                width = input.getWidth();
                height = input.getHeight();

                NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

                int[] hw = resize64(height, width, detect_resolution);

                array = NDImageUtils.resize(array, hw[1], hw[0], Image.Interpolation.AREA);

                array = array.toType(DataType.UINT8, true);

                imgArray = array;

                NDArray ones = manager.ones(new Shape(array.getShape().get(0), array.getShape().get(1), 1), DataType.UINT8);

                array = array.concat(ones, -1);

                array = array.transpose(2, 0, 1); // HWC -> CHW RGB

                array = array.div(127.5f).sub(1.0f);

                array = array.flip(0);

                return new NDList(array);
            }
        }

        @Override
        public Image processOutput(TranslatorContext ctx, NDList list) {
            try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {
                NDArray tpMap = list.singletonOrThrow();

                // deccode_output_score_and_ptss(tpMap, topk_n = 200, ksize = 3)
                int width = (int) (tpMap.getShape().get(2));

                NDArray displacement = tpMap.get(new NDIndex("1:5, :, :"));

                NDArray center = tpMap.get(new NDIndex("0, :, :"));

                // Sigmoid 函数，即f(x)=1/(1+e-x)
                NDArray heat = center.neg().exp().add(1).pow(-1);

                int rows = (int) (heat.getShape().get(0));
                int cols = (int) (heat.getShape().get(1));

                // hmax = F.max_pool2d( heat, (ksize, ksize), stride=1, padding=(ksize-1)//2)
                NDArray max_pool2d = manager.zeros(new Shape(rows + 2, cols + 2));

                max_pool2d.set(new NDIndex("1:" + (rows + 1) + ",1:" + (cols + 1)), heat);
                float[][] max_pool2d_arr = NDArrayUtils.floatNDArrayToArray(max_pool2d);
                float[][] arr = new float[rows][cols];
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
//                    arr[row][col] = max_pool2d.get(new NDIndex(row + ":" + (row + ksize) + "," + col + ":" + (col + ksize))).max().toFloatArray()[0];
                        float max = max_pool2d_arr[row][col];
                        for (int i = row; i < row + ksize; i++) {
                            for (int j = col; j < col + ksize; j++) {
                                if (max_pool2d_arr[i][j] > max) {
                                    max = max_pool2d_arr[i][j];
                                }
                            }
                        }
                        arr[row][col] = max;
                    }
                }

                NDArray hmax = manager.create(arr).reshape(rows, cols);

                NDArray keep = hmax.eq(heat);
                keep = keep.toType(DataType.FLOAT32, true);

                heat = heat.mul(keep);
                heat = heat.reshape(-1);

                NDArray indices = heat.argSort(-1, false).get(new NDIndex("0:200"));
                NDArray pts_score = heat.get(indices);
                indices = indices.toType(DataType.FLOAT32, true);
                NDArray yy = indices.div(width).floor().expandDims(-1);
                NDArray xx = indices.mod(width).expandDims(-1);
                NDArray pts = yy.concat(xx, -1);

                NDArray vmap = displacement.transpose(1, 2, 0);

                NDArray start = vmap.get(new NDIndex(":, :, :2"));
                NDArray end = vmap.get(new NDIndex(":, :, 2:"));

                NDArray dist_map = start.sub(end).pow(2).sum(new int[]{-1}).sqrt();

                ArrayList<int[]> lines = new ArrayList<>();

                for (int i = 0; i < pts_score.size(); i++) {
                    center = pts.get(i);
                    int y = (int) center.getFloat(0);
                    int x = (int) center.getFloat(1);
                    float score = pts_score.getFloat(i);
                    float distance = dist_map.getFloat(y, x);

                    if (score > score_thr && distance > dist_thr) {
                        NDArray array = vmap.get(new NDIndex(y + "," + x + ",:"));  // y, x, :
                        int disp_x_start = (int) array.getFloat(0);
                        int disp_y_start = (int) array.getFloat(1);
                        int disp_x_end = (int) array.getFloat(2);
                        int disp_y_end = (int) array.getFloat(3);
                        int x_start = x + disp_x_start;
                        int y_start = y + disp_y_start;
                        int x_end = x + disp_x_end;
                        int y_end = y + disp_y_end;

                        int[] line = new int[4];
                        line[0] = 2 * x_start;
                        line[1] = 2 * y_start;
                        line[2] = 2 * x_end;
                        line[3] = 2 * y_end;

                        lines.add(line);
                    }
                }

                NDArray img_output = manager.zeros(imgArray.getShape());
                Image img = ImageFactory.getInstance().fromNDArray(img_output);
                org.opencv.core.Mat mat = (org.opencv.core.Mat) img.getWrappedImage();
                for (int[] line : lines) {
                    int x_start = line[0];
                    int y_start = line[1];
                    int x_end = line[2];
                    int y_end = line[3];
                    Point point1 = new Point(x_start, y_start);
                    Point point2 = new Point(x_end, y_end);
                    OpenCVUtils.line(mat, point1, point2);
                }

                int[] hw = resize64(img.getHeight(), img.getWidth(), image_resolution);
                NDArray detected_map = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);
                img = ImageFactory.getInstance().fromNDArray(detected_map);

                return img;
            }
        }

//        private ArrayList<int[]> detectEdge(ArrayList<int[]> lines){
//            ArrayList<int[]> h_lines = new ArrayList<>();
//            ArrayList<int[]> v_lines = new ArrayList<>();
//            ArrayList<int[]> sorted_h_lines = new ArrayList<>();
//            ArrayList<int[]> sorted_v_lines = new ArrayList<>();
//
//            for (int[] line : lines) {
//                // double delta_x = v._p1.x - v._p2.x, delta_y = v._p1.y - v._p2.y;
//                int x_start = line[0];
//                int y_start = line[1];
//                int x_end = line[2];
//                int y_end = line[3];
//                int delta_x = x_start - x_end;
//                int delta_y = y_start - y_end;
//                if(Math.abs(delta_x) > Math.abs(delta_y)){
//                    h_lines.add(line);
//                }else {
//                    v_lines.add(line);
//                }
//                if (h_lines.size() >= 2 && v_lines.size() >= 2){
//
//                }
//            }
//
//        }

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
