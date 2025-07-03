package top.aias.face.detection;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.*;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 人脸检测前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceDetTranslator implements Translator<Image, DetectedObjects> {
    // topk值 - topk value
    private int topK = 200;
    // 置信度阈值 - confidence threshold
    private double confThresh = 0.85;
    // 非极大值抑制阈值 - non-maximum suppression threshold
    private double nmsThresh = 0.45;
    private double[] variance = {0.1, 0.2};
    private int[][] scales = {{16, 32}, {64, 128}, {256, 512}};
    private int[] steps = {8, 16, 32};
    private int orgWidth;
    private int orgHeight;
    private int width;
    private int height;
    float eye_dist_threshold = 5;
    private int limit_side_len = 640; // 建议 640 , 320， 放大可以检测更多小脸
    public FaceDetTranslator() {}

    /** {@inheritDoc} */
    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        width = input.getWidth();
        height = input.getHeight();
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        int h = input.getHeight();
        int w = input.getWidth();
        orgWidth = w;
        orgHeight = h;

        // limit the max side
        float ratio = 1.0f;
        if (Math.max(h, w) > limit_side_len) {
            if (h > w) {
                ratio = (float) limit_side_len / (float) h;
            } else {
                ratio = (float) limit_side_len / (float) w;
            }
        }

        int resize_h = (int) (h * ratio);
        int resize_w = (int) (w * ratio);

        width = resize_w;
        height = resize_h;

        array = NDImageUtils.resize(array, resize_w, resize_h, Image.Interpolation.AREA);


        array = array.transpose(2, 0, 1); // HWC -> CHW RGB -> BGR .flip(0)
        // The network by default takes float32
        if (!array.getDataType().equals(DataType.FLOAT32)) {
            array = array.toType(DataType.FLOAT32, false);
        }
        NDArray mean =
                ctx.getNDManager().create(new float[] {104f, 117f, 123f}, new Shape(3, 1, 1));
        array = array.sub(mean);
        return new NDList(array);
    }

    /** {@inheritDoc} */
    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
        NDManager manager = ctx.getNDManager();
        double scaleXY = variance[0];
        double scaleWH = variance[1];

        NDArray prob = list.get(1).get(":, 1:");
        prob =
                NDArrays.stack(
                        new NDList(
                                prob.argMax(1).toType(DataType.FLOAT32, false),
                                prob.max(new int[] {1})));

        NDArray boxRecover = boxRecover(manager, width, height, scales, steps);
        NDArray boundingBoxes = list.get(0);
        NDArray bbWH = boundingBoxes.get(":, 2:").mul(scaleWH).exp().mul(boxRecover.get(":, 2:"));
        NDArray bbXY =
                boundingBoxes
                        .get(":, :2")
                        .mul(scaleXY)
                        .mul(boxRecover.get(":, 2:"))
                        .add(boxRecover.get(":, :2"))
                        .sub(bbWH.mul(0.5f));

        boundingBoxes = NDArrays.concat(new NDList(bbXY, bbWH), 1);

        NDArray landms = list.get(2);
        landms = decodeLandm(landms, boxRecover, scaleXY);

        // filter the result below the threshold
        NDArray cutOff = prob.get(1).gt(confThresh);
        boundingBoxes = boundingBoxes.transpose().booleanMask(cutOff, 1).transpose();
        landms = landms.transpose().booleanMask(cutOff, 1).transpose();
        prob = prob.booleanMask(cutOff, 1);

        // start categorical filtering
        NDArray newProb = prob.get(1).reshape(1,prob.getShape().getShape()[1]);
        long[] order = newProb.argSort(1,false).get(":" + topK).toLongArray();

        prob = prob.transpose();
        List<String> retNames = new ArrayList<>();
        List<Double> retProbs = new ArrayList<>();
        List<BoundingBox> retBB = new ArrayList<>();

        Map<Integer, List<BoundingBox>> recorder = new ConcurrentHashMap<>();

        for (int i = 0; i < order.length; i++) {
            long currMaxLoc = order[i];
            float[] classProb = prob.get(currMaxLoc).toFloatArray();
            int classId = (int) classProb[0];
            double probability = classProb[1];

            double[] boxArr = boundingBoxes.get(currMaxLoc).toDoubleArray();
            double[] landmsArr = landms.get(currMaxLoc).toDoubleArray();
            Rectangle rect = new Rectangle(boxArr[0], boxArr[1], boxArr[2], boxArr[3]);
            List<BoundingBox> boxes = recorder.getOrDefault(classId, new ArrayList<>());
            boolean belowIoU = true;
            for (BoundingBox box : boxes) {
                double iou = this.getIoU(rect,box.getBounds());
                if (iou > nmsThresh) {
                    belowIoU = false;
                    break;
                }
            }
            if (belowIoU) {
                List<Point> keyPoints = new ArrayList<>();
                for (int j = 0; j < 5; j++) { // 5 face landmarks
                    double x = landmsArr[j * 2];
                    double y = landmsArr[j * 2 + 1];
                    keyPoints.add(new Point(x * orgWidth, y * orgHeight));
                }

                //      1.  left_eye_x , left_eye_y
                //      2.  right_eye_x , right_eye_y
                //      3.  nose_x , nose_y
                //      4.  left_mouth_x , left_mouth_y
                //      5.  right_mouth_x , right_mouth_y
                Point left_eye = keyPoints.get(0);
                Point right_eye = keyPoints.get(1);
                float sum = (float) Math.pow(left_eye.getX() - right_eye.getX(), 2) + (float) Math.pow(left_eye.getY() - right_eye.getY(), 2);
                float eye_dist = (float) Math.sqrt(sum);
                if (eye_dist < eye_dist_threshold) {
                    continue;
                }

                Landmark landmark =
                        new Landmark(boxArr[0], boxArr[1], boxArr[2], boxArr[3], keyPoints);

                boxes.add(landmark);
                recorder.put(classId, boxes);
                String className = "Face"; // classes.get(classId)
                retNames.add(className);
                retProbs.add(probability);
                retBB.add(landmark);
            }
        }

        return new DetectedObjects(retNames, retProbs, retBB);
    }

    /**
     * computing IoU
     *
     * @param rec1: (x, y, width, height)
     * @param rec2: (x, y, width, height)
     * @return scala value of IoU
     */
    private double getIoU(Rectangle rec1, Rectangle rec2) {
        // computing area of each rectangles
        double s1 = rec1.getWidth() * rec1.getHeight();
        double s2 = rec2.getWidth() * rec2.getHeight();

        // computing the sum_area
        double sumArea = s1 + s2;

        // find the each edge of intersect rectangle
        double left = Math.max(rec1.getX(), rec2.getX());
        double top = Math.max(rec1.getY(), rec2.getY());
        double right = Math.min(rec1.getX() + rec1.getWidth(), rec2.getX() + rec2.getWidth());
        double bottom = Math.min(rec1.getY() + rec1.getHeight(), rec2.getY() + rec2.getHeight());

        // check if there is an intersect
        if (left >= right || top >= bottom) {
            return 0.0;
        } else {
            double intersect = (right - left) * (bottom - top);
            return intersect / (sumArea - intersect);
        }
    }

    /**
     * computing IoU
     *
     * @param rec1: (y0, x0, y1, x1), which reflects (top, left, bottom, right)
     * @param rec2: (y0, x0, y1, x1)
     * @return scala value of IoU
     */
    private double getIoU(double[] rec1, double[] rec2) {
        // computing area of each rectangles
        double SRec1 = (rec1[2] - rec1[0]) * (rec1[3] - rec1[1]);
        double SRec2 = (rec2[2] - rec2[0]) * (rec2[3] - rec2[1]);

        // computing the sum_area
        double sumArea = SRec1 + SRec2;

        // find the each edge of intersect rectangle
        double left = Math.max(rec1[1], rec2[1]);
        double right = Math.min(rec1[3], rec2[3]);
        double top = Math.max(rec1[0], rec2[0]);
        double bottom = Math.min(rec1[2], rec2[2]);

        // check if there is an intersect
        if (left >= right || top >= bottom) {
            return 0.0;
        } else {
            double intersect = (right - left) * (bottom - top);
            return (intersect / (sumArea - intersect)) * 1.0;
        }
    }

    private NDArray boxRecover(
            NDManager manager, int width, int height, int[][] scales, int[] steps) {
        int[][] aspectRatio = new int[steps.length][2];
        for (int i = 0; i < steps.length; i++) {
            int wRatio = (int) Math.ceil((float) width / steps[i]);
            int hRatio = (int) Math.ceil((float) height / steps[i]);
            aspectRatio[i] = new int[] {hRatio, wRatio};
        }

        List<double[]> defaultBoxes = new ArrayList<>();

        for (int idx = 0; idx < steps.length; idx++) {
            int[] scale = scales[idx];
            for (int h = 0; h < aspectRatio[idx][0]; h++) {
                for (int w = 0; w < aspectRatio[idx][1]; w++) {
                    for (int i : scale) {
                        double skx = i * 1.0 / width;
                        double sky = i * 1.0 / height;
                        double cx = (w + 0.5) * steps[idx] / width;
                        double cy = (h + 0.5) * steps[idx] / height;
                        defaultBoxes.add(new double[] {cx, cy, skx, sky});
                    }
                }
            }
        }

        double[][] boxes = new double[defaultBoxes.size()][defaultBoxes.get(0).length];
        for (int i = 0; i < defaultBoxes.size(); i++) {
            boxes[i] = defaultBoxes.get(i);
        }
        return manager.create(boxes).clip(0.0, 1.0);
    }

    // decode face landmarks, 5 points per face
    private NDArray decodeLandm(NDArray pre, NDArray priors, double scaleXY) {
        NDArray point1 =
                pre.get(":, :2").mul(scaleXY).mul(priors.get(":, 2:")).add(priors.get(":, :2"));
        NDArray point2 =
                pre.get(":, 2:4").mul(scaleXY).mul(priors.get(":, 2:")).add(priors.get(":, :2"));
        NDArray point3 =
                pre.get(":, 4:6").mul(scaleXY).mul(priors.get(":, 2:")).add(priors.get(":, :2"));
        NDArray point4 =
                pre.get(":, 6:8").mul(scaleXY).mul(priors.get(":, 2:")).add(priors.get(":, :2"));
        NDArray point5 =
                pre.get(":, 8:10").mul(scaleXY).mul(priors.get(":, 2:")).add(priors.get(":, :2"));
        return NDArrays.concat(new NDList(point1, point2, point3, point4, point5), 1);
    }

    /** {@inheritDoc} */
    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
