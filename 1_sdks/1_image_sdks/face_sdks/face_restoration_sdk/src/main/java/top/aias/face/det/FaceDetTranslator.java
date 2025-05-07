package top.aias.face.det;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Landmark;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸检测模型前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceDetTranslator implements Translator<Image, DetectedObjects> {
    // topk值 - topk value
    int topK = 200;

    // eye_dist_threshold=5: skip faces whose eye distance is smaller than 5 pixels
    double eye_dist_threshold = 5;
    // 置信度阈值 - confidence threshold
    double confThresh = 0.85; // 0.97f 0.85f
    // 非极大值抑制阈值 - non-maximum suppression threshold
    double nmsThresh = 0.45; // 0.4f 0.45f

    double[] variance = {0.1, 0.2};
    int[][] scales = {{16, 32}, {64, 128}, {256, 512}};
    int[] steps = {8, 16, 32};

    private int width;
    private int height;

    public FaceDetTranslator() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        width = input.getWidth();
        height = input.getHeight();
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        if (array.getShape().dimension() == 4) {
            array = array.get(new NDIndex(":, :, 0:3"));
        }

        array = array.transpose(2, 0, 1); // HWC -> CHW RGB -> BGR .flip(0)
        // The network by default takes float32
        if (!array.getDataType().equals(DataType.FLOAT32)) {
            array = array.toType(DataType.FLOAT32, false);
        }
        NDArray mean =
                ctx.getNDManager().create(new float[]{104f, 117f, 123f}, new Shape(3, 1, 1));
        array = array.sub(mean);
        return new NDList(array);
    }

    /**
     * {@inheritDoc}
     */
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
                                prob.max(new int[]{1})));

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

        List<BoundingBox> boxes = new ArrayList<>();

        for (int i = 0; i < order.length; i++) {
            long currMaxLoc = order[i];
            float[] classProb = prob.get(currMaxLoc).toFloatArray();
            double probability = classProb[1];

            double[] boxArr = boundingBoxes.get(currMaxLoc).toDoubleArray();

            double[] landmsArr = landms.get(currMaxLoc).toDoubleArray();
            Rectangle rect = new Rectangle(boxArr[0], boxArr[1], boxArr[2], boxArr[3]);
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
                    keyPoints.add(new Point(x * width, y * height));
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
                String className = "Face"; // classes.get(classId)
                retNames.add(className);
                retProbs.add(probability);
                retBB.add(landmark);
            }
        }

        return new DetectedObjects(retNames, retProbs, retBB);
    }

    private NDArray boxRecover(
            NDManager manager, int width, int height, int[][] scales, int[] steps) {
        int[][] aspectRatio = new int[steps.length][2];
        for (int i = 0; i < steps.length; i++) {
            int wRatio = (int) Math.ceil((float) width / steps[i]);
            int hRatio = (int) Math.ceil((float) height / steps[i]);
            aspectRatio[i] = new int[]{hRatio, wRatio};
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
                        defaultBoxes.add(new double[]{cx, cy, skx, sky});
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

        // judge if there is an intersect
        if (left >= right || top >= bottom) {
            return 0.0;
        } else {
            double intersect = (right - left) * (bottom - top);
            return intersect / (sumArea - intersect);
        }
    }


    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
