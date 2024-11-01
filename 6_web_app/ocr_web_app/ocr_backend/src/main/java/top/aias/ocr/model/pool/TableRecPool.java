package top.aias.ocr.model.pool;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;
import top.aias.ocr.bean.TableResult;

import java.util.ArrayList;
/**
 * 表格识别连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TableRecPool {
    private int poolSize;
    private ZooModel<Image, TableResult> model;
    private ArrayList<Predictor<Image, TableResult>> predictorList = new ArrayList<>();


    public TableRecPool(int poolSize, ZooModel<Image, TableResult> model) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, TableResult> predictor = model.newPredictor();
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<Image, TableResult> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, TableResult> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Image, TableResult> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, TableResult> predictor : predictorList) {
            predictor.close();
        }

    }
}