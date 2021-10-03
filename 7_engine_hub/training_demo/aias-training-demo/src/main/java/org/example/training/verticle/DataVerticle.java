package org.example.training.verticle;

import ai.djl.Device;
import ai.djl.metric.Metrics;
import ai.djl.training.Trainer;
import ai.djl.training.listener.EvaluatorTrainingListener;
import ai.djl.training.listener.TrainingListener;
import io.vavr.control.Try;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.example.training.data.MetricInfo;
import org.example.training.data.ModelInfo;
import org.example.training.data.TrainerInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class DataVerticle extends AbstractVerticle {

    private final Logger LOGGER = LoggerFactory.getLogger(WebVerticle.class.getCanonicalName());

    public static final String ADDRESS_TRAINER_REQUEST = "trainer-request";
    public static final String ADDRESS_TRAINER = "trainer";

    private Trainer trainer;
    private int currentEpoch = 1;
    private TrainerInfo.State currentState = TrainerInfo.State.Undefined;
    private int trainingProgress;
    private int validatingProgress;
    private int batchSize = 0;
    private final Map<String, List<MetricInfo>> performance = new HashMap<>();
    private long updateInterval = 500;
    private long lastUpdate = System.currentTimeMillis();

    @Override
    public void start() {
        LOGGER.info("DataVerticle starting...");
        vertx.eventBus().consumer(ADDRESS_TRAINER_REQUEST, event -> Try.run(this::sendTrainer));
    }

    public void setEpoch(Trainer trainer) {
        this.trainer = trainer;
        this.validatingProgress = 0;
        this.trainingProgress = 0;
        this.performance.clear();
        this.currentEpoch++;
        sendTrainer();
    }

    public void setTrainingBatch(Trainer trainer, TrainingListener.BatchData batchData) {
        setBatch(trainer, batchData, TrainerInfo.State.Training);
    }

    public void setValidationBatch(Trainer trainer, TrainingListener.BatchData batchData) {
        setBatch(trainer, batchData, TrainerInfo.State.Validating);
    }

    public void sendTrainer() {
        Try.run(() -> {
            ModelInfo mi = ModelInfo.builder()
                    .name(Try.of(() -> trainer.getModel().getName()).getOrElse("Noname"))
                    .block(Try.of(() -> trainer.getModel().getBlock().toString()).getOrElse("Undefined"))
                    .build();

            TrainerInfo trainerInfo = TrainerInfo.builder()
                    .devices(getDevices())
                    .modelInfo(mi)
                    .state(currentState)
                    .epoch(currentEpoch)
                    .speed(getSpeed())
                    .trainingProgress(trainingProgress)
                    .validatingProgress(validatingProgress)
                    .metrics(this.copy(performance))
                    .metricNames(new ArrayList<>(performance.keySet()))
                    .metricsSize(performance.isEmpty() ? 0 : performance.values().iterator().next().size())
                    .build();
            vertx.eventBus().publish(ADDRESS_TRAINER, Json.encode(trainerInfo));
        }).onFailure(throwable -> LOGGER.error("", throwable));
    }

    private void setBatch(Trainer trainer, TrainingListener.BatchData batchData, TrainerInfo.State state) {
        this.trainer = trainer;
        this.batchSize = batchData.getBatch().getSize();
        this.currentState = state;
        if (state.equals(TrainerInfo.State.Training)) {
            this.trainingProgress = (int) ((batchData.getBatch().getProgress() + 1) * 100 / batchData.getBatch().getProgressTotal());
        } else {
            this.validatingProgress = (int) ((batchData.getBatch().getProgress() + 1) * 100 / batchData.getBatch().getProgressTotal());
        }
        setPerformance();
        if (isTimeToUpdate()) {
            sendTrainer();
            lastUpdate = System.currentTimeMillis();
        }
    }

    private void setPerformance() {
        Try.run(() -> {
            Metrics metrics = trainer.getMetrics();
            trainer.getEvaluators().forEach(e -> {
                String metricName = EvaluatorTrainingListener.metricName(e, EvaluatorTrainingListener.TRAIN_PROGRESS);
                if (metrics.hasMetric(metricName)) {
                    List<MetricInfo> mis = getMetrics(e.getName());
                    float y = metrics.latestMetric(metricName).getValue().floatValue();
                    mis.add(MetricInfo.builder().name(e.getName()).x(mis.size()).y(y).build());
                    setMetrics(e.getName(), mis);
                }
            });
        }).onFailure(throwable -> LOGGER.error("", throwable));
    }

    private void setMetrics(String name, List<MetricInfo> metricInfos) {
        performance.put(name, metricInfos);
    }

    private List<MetricInfo> getMetrics(String name) {
        return performance.getOrDefault(name, new ArrayList<>());
    }

    private boolean isTimeToUpdate() {
        return System.currentTimeMillis() - lastUpdate > updateInterval;
    }

    private List<String> getDevices() {
        return Try.of(() -> Arrays.stream(trainer.getDevices()).map(Device::toString).collect(Collectors.toList()))
                .getOrElse(Collections.singletonList(Device.cpu().toString()));
    }

    private BigDecimal getSpeed() {
        return Try.of(() -> {
            Metrics metrics = trainer.getMetrics();
            if (metrics != null && metrics.hasMetric("train")) {
                float batchTime = metrics.latestMetric("train").getValue().longValue() / 1_000_000_000f;
                return BigDecimal.valueOf(batchSize / batchTime).setScale(2, RoundingMode.HALF_UP);
            }
            return BigDecimal.ZERO;
        }).getOrElse(BigDecimal.ZERO);

    }

    private Map<String, List<MetricInfo>> copy(
            Map<String, List<MetricInfo>> original) {
        Map<String, List<MetricInfo>> copy = new HashMap<String, List<MetricInfo>>();
        for (Map.Entry<String, List<MetricInfo>> entry : original.entrySet()) {
            copy.put(entry.getKey(),
                    new ArrayList<MetricInfo>(entry.getValue()));
        }
        return copy;
    }
}
