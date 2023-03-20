package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WaveGlowEncoder {
    public WaveGlowEncoder() {
    }

    public Criteria<NDArray, NDArray> criteria() {
        Criteria<NDArray, NDArray> criteria =
                Criteria.builder()
                        .setTypes(NDArray.class, NDArray.class)
                        .optModelPath(Paths.get("models/waveGlow.zip"))
                        .optTranslator(new WaveGlowTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
