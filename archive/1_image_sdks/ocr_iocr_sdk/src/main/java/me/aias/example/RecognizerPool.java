package me.aias.example;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

public class RecognizerPool {
    private int poolSize;
    private ZooModel<Image, String> recognitionModel;
    private ArrayList<Predictor<Image, String>> recognizerList = new ArrayList<>();


    public RecognizerPool(int poolSize, ZooModel<Image, String> detectionModel) {
        this.poolSize = poolSize;
        this.recognitionModel = detectionModel;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, String> detector = detectionModel.newPredictor();
            recognizerList.add(detector);
        }
    }

    public synchronized Predictor<Image, String> getRecognizer(){
        while (recognizerList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, String> recognizer = recognizerList.remove(0);
        return recognizer;
    }

    public synchronized void releaseRecognizer(Predictor<Image, String> recognizer) {
        recognizerList.add(recognizer);
        notifyAll();
    }

    public void close() {
        recognitionModel.close();
        for (Predictor<Image, String> detector : recognizerList) {
            detector.close();
        }

    }
}