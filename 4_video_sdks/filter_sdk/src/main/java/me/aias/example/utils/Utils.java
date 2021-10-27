package me.aias.example.utils;

import ai.djl.training.util.DownloadUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static void downloadFont() throws IOException {
        Path modelFile = Paths.get("src/test/resources/simfang.ttf");
        if (Files.notExists(modelFile)) {
            DownloadUtils.download(
                    "https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_sdk/simfang.ttf",
                    "src/test/resources/simfang.ttf");
        }
    }
}
