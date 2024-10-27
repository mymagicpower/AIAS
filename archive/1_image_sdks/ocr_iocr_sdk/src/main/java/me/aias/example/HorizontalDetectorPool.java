package me.aias.example;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

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
        detectionModel.close();
        for (Predictor<Image, DetectedObjects> detector : detectorList) {
            detector.close();
        }

    }
}