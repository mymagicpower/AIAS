package me.aias.training.listener;

import ai.djl.training.Trainer;
import ai.djl.training.listener.TrainingListener;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.aias.training.verticle.DataVerticle;
import me.aias.training.verticle.WebVerticle;

public class UiTrainingListener  implements TrainingListener {

    private static final Logger logger = LoggerFactory.getLogger(UiTrainingListener.class.getCanonicalName());

    private Vertx vertx;
    private DataVerticle dataVerticle;

    public UiTrainingListener() {
        logger.info("UiTrainingListener starting...");
        vertx = Vertx.vertx();
        dataVerticle = new DataVerticle();
        vertx.deployVerticle(dataVerticle);
        vertx.deployVerticle(new WebVerticle());
    }

    @Override
    public void onEpoch(Trainer trainer) {
        dataVerticle.setEpoch(trainer);
    }

    @Override
    public void onTrainingBatch(Trainer trainer, BatchData batchData) {
        dataVerticle.setTrainingBatch(trainer, batchData);
    }

    @Override
    public void onValidationBatch(Trainer trainer, BatchData batchData) {
        dataVerticle.setValidationBatch(trainer, batchData);
    }

    @Override
    public void onTrainingBegin(Trainer trainer) {
        logger.info("onTrainingBegin ...");
    }

    @Override
    public void onTrainingEnd(Trainer trainer) {
        logger.info("onTrainingEnd ...");
        vertx.close();
    }
}
