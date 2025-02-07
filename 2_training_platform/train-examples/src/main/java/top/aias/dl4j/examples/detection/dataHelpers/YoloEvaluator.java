package top.aias.dl4j.examples.detection.dataHelpers;

import org.datavec.api.records.metadata.RecordMetaDataImageURI;
import org.datavec.image.recordreader.objdetect.ImageObject;
import org.datavec.image.recordreader.objdetect.ImageObjectLabelProvider;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer;
import org.nd4j.common.primitives.Counter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class YoloEvaluator {

    private static final Logger log = LoggerFactory.getLogger(YoloEvaluator.class);

    private final double iouThreshold;
    private final double detectionThreshold;

    private final int w;
    private final int h;

    private final int gridWidth = 13;
    private final int gridHeight = 13;

    // 定义：模型正确地检测到了目标，且预测的类别与真实类别一致，边界框的重叠程度（IoU）超过设定的阈值（如0.5）。
    private Counter<String> truePositives = new Counter<>();

    // 定义：模型错误地将背景或非目标区域预测为目标，或者预测的目标类别与真实类别不一致，或者边界框定位不准确（IoU < 阈值）。
    private Counter<String> falsePositives = new Counter<>();

    // 定义：模型正确地识别了背景区域，没有将背景误检测为目标，训练数据无此情形，忽略。
    // protected Counter<Integer> trueNegatives = new Counter<>();

    // 定义：模型未能检测到真实存在的目标，或虽然检测到了目标但边界框的IoU < 阈值，或分类错误。
    private Counter<String> falseNegatives = new Counter<>();

    private List<String> labelsList;

    public YoloEvaluator(ComputationGraph Yolo2_model, RecordReaderDataSetIterator dataSetIterator, ImageObjectLabelProvider labelProvider, int w, int h){
        this(Yolo2_model, dataSetIterator,labelProvider, 0.5, 0.4, w, h);
    }

    public YoloEvaluator(ComputationGraph Yolo2_model, RecordReaderDataSetIterator dataSetIterator, ImageObjectLabelProvider labelProvider, double detectionThreshold, int w, int h){
        this(Yolo2_model, dataSetIterator,labelProvider, 0.5, detectionThreshold, w, h);
    }

    public YoloEvaluator(ComputationGraph Yolo2_model, RecordReaderDataSetIterator dataSetIterator, ImageObjectLabelProvider labelProvider, double iouThreshold, double detectionThreshold, int w, int h){
        this.iouThreshold = iouThreshold;
        this.detectionThreshold = detectionThreshold;
        this.w = w;
        this.h = h;

        dataSetIterator.reset();

        labelsList = dataSetIterator.getLabels();

        Yolo2OutputLayer yout = (Yolo2OutputLayer)Yolo2_model.getOutputLayer(0);

        while (dataSetIterator.hasNext()) {
            org.nd4j.linalg.dataset.DataSet ds = dataSetIterator.next();
            RecordMetaDataImageURI metadata = (RecordMetaDataImageURI)ds.getExampleMetaData().get(0);

            INDArray features = ds.getFeatures();
            INDArray results = Yolo2_model.outputSingle(features);

            List<ImageObject> expectedObjects = labelProvider.getImageObjectsForPath(metadata.getURI());
            LinkedList<DetectedObject> detectedObjects = new LinkedList<>(yout.getPredictedObjects(results, detectionThreshold));

            //ImageObject expected : expectedObjects
            for (ImageObject expected : expectedObjects) {

                double best_iou = 0.0;
                int detectedObjectIndex = -1;

                if (detectedObjects.size() > 0) {
                    for (DetectedObject detectedObj : detectedObjects) {
                        if (detectedObj == null)
                            continue;
                        if (!labelsList.get(detectedObj.getPredictedClass()).equals(expected.getLabel()))
                            continue;

                        if (calcIoU(expected, detectedObj) > best_iou && (detectedObj.getConfidence() > detectionThreshold)) {
                            best_iou = calcIoU(expected, detectedObj);
                            detectedObjectIndex = detectedObjects.indexOf(detectedObj);
                        }
                    }

                    if (best_iou >= iouThreshold) {
                        truePositives.incrementCount(expected.getLabel(),1); //True Positives
                    } else {
                        falsePositives.incrementCount(expected.getLabel(),1); //False Positive
                    }

                    if (detectedObjectIndex != -1)
                        detectedObjects.remove(detectedObjectIndex); //removing detected object to avoid repetition
                }else {
                    falseNegatives.incrementCount(expected.getLabel(),1); //False Negative
                }
            }
        }
    }

    public double getPrecision() {
        return (truePositives.totalCount()) / (truePositives.totalCount() + falsePositives.totalCount());
    }

    public double getRecall() {
        return (truePositives.totalCount()) / (truePositives.totalCount() + falseNegatives.totalCount());
    }

    public double getF1() {
        return (0.5)*(getPrecision() + getRecall());
    }

    public List<String> getLabelsList() {
        return labelsList;
    }

    public void print(){
        LinkedList<String> headers = new LinkedList<String>();
        headers.add("Labels");
        headers.addAll(labelsList);

        LinkedList<LinkedList<String>> content = new LinkedList<>();

        LinkedList<String> True_Positives = new LinkedList<>();
        True_Positives.add("True Positives");
        for (String label : labelsList)
            True_Positives.add(String.valueOf(truePositives.getCount(label)));

        LinkedList<String> False_Positives = new LinkedList<>();
        False_Positives.add("False Positives");
        for (String label : labelsList)
            False_Positives.add(String.valueOf(falsePositives.getCount(label)));

        LinkedList<String> False_Negatives = new LinkedList<>();
        False_Negatives.add("False Negatives");
        for (String label : labelsList)
            False_Negatives.add(String.valueOf(falseNegatives.getCount(label)));

        content.add(True_Positives);
        content.add(False_Positives);
        content.add(False_Negatives);

        ConsoleTable ct = new ConsoleTable(headers, content);
        System.out.println(ct.toString());

        System.out.println("True Positives:   " + truePositives.totalCount());
        System.out.println("False Positives:  " + falsePositives.totalCount());
        System.out.println("False Negatives:  " + falseNegatives.totalCount());
        System.out.println("Precision:  " + getPrecision());
        System.out.println("Recall:  " + getRecall());
        System.out.println("F1 Score:  " + getF1());

    }

    //https://towardsdatascience.com/evaluating-performance-of-an-object-detection-model-137a349c517b
    private double calcIoU(ImageObject expected, DetectedObject obj){
        assert expected != null;
        assert obj != null;

        double[] xy1 = obj.getTopLeftXY();
        double[] xy2 = obj.getBottomRightXY();

        int x1 = (int) Math.round(w * xy1[0] / gridWidth);
        int y1 = (int) Math.round(h * xy1[1] / gridHeight);

        int x2 = (int) Math.round(w * xy2[0] / gridWidth);
        int y2 = (int) Math.round(h * xy2[1] / gridHeight);


        //if the GT bbox and predcited BBox do not overlap then iou=0
        // If bottom right of x-coordinate  GT  bbox is less than or above the top left of x coordinate of  the predicted BBox
        if(expected.getX2()  < x1)
            return 0.0;

        // If bottom right of y-coordinate  GT  bbox is less than or above the top left of y coordinate of  the predicted BBox
        if(expected.getY2() < y1)
            return 0.0;

        // If bottom right of x-coordinate  GT  bbox is greater than or below the bottom right  of x coordinate of  the predcited BBox
        if(expected.getX1() > x2)
            return 0.0;

        // If bottom right of y-coordinate  GT  bbox is greater than or below the bottom right  of y coordinate of  the predcited BBox
        if(expected.getY1() > y2)
            return 0.0;

        double GT_bbox_area = (expected.getX2() - expected.getX1() + 1) * (expected.getY2() - expected.getY1() + 1);
        double Pred_bbox_area = (x2 - x1 + 1) * (y2 - y1 + 1);

        double x_top_left = Math.max(expected.getX1(), x1);
        double y_top_left = Math.max(expected.getY1(), y1);
        double x_bottom_right = Math.min(expected.getX2(), x2);
        double y_bottom_right = Math.min(expected.getY2(), y2);

        double intersection_area = (x_bottom_right- x_top_left + 1) * (y_bottom_right-y_top_left + 1);
        double union_area = (GT_bbox_area + Pred_bbox_area - intersection_area);

        return intersection_area / union_area;
    }
}