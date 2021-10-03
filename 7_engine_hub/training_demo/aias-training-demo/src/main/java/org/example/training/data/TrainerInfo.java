package org.example.training.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TrainerInfo {

	public enum State {Undefined, Training, Validating}
	private ModelInfo modelInfo;
	private int epoch;
	private int trainingProgress;
	private int validatingProgress;
	private BigDecimal speed;
	private State state;
	private String engine;
	private List<String> devices;
	private List<String> metricNames;
	private Map<String, List<MetricInfo>> metrics;
	private int metricsSize;

}
             