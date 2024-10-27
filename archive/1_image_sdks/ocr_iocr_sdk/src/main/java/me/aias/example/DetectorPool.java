package me.aias.example;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

public class DetectorPool {
    private int poolSize;
    private ZooModel<Image, NDList> detectionModel;
    private ArrayList<Predictor<Image, NDList>> detectorList = new ArrayList<>();


    public DetectorPool(int poolSize, ZooModel<Image, NDList> detectionModel) {
        this.poolSize = poolSize;
        this.detectionModel = detectionModel;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, NDList> detector = detectionModel.newPredictor();
            detectorList.add(detector);
        }
    }

    public synchronized Predictor<Image, NDList> getDetector() {
        while (detectorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, NDList> detector = detectorList.remove(0);
        return detector;
    }

    public synchronized void releaseDetector(Predictor<Image, NDList> detector) {
        detectorList.add(detector);
        notifyAll();
    }

    public void close() {
        detectionModel.close();
        for (Predictor<Image, NDList> detector : detectorList) {
            detector.close();
        }

    }
}