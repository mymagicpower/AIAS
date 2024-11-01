package top.aias.ocr.model.pool;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;
/**
 * 水平文字检测连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class HorizontalDetectorPool {
    private int poolSize;
    private ZooModel<Image, DetectedObjects>  detectionModel;
    private ArrayList<Predictor<Image, DetectedObjects>> detectorList = new ArrayList<>();


    public HorizontalDetectorPool(int poolSize, ZooModel<Image, DetectedObjects>  detectionModel) {
        this.poolSize = poolSize;
        this.detectionModel = detectionModel;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
            detectorList.add(detector);
        }
    }

    public synchronized Predictor<Image, DetectedObjects> getDetector(){
        while (detectorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, DetectedObjects> detector = detectorList.remove(0);
        return detector;
    }

    public synchronized void releaseDetector(Predictor<Image, DetectedObjects> detector) {
        detectorList.add(detector);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, DetectedObjects> detector : detectorList) {
            detector.close();
        }

    }
}