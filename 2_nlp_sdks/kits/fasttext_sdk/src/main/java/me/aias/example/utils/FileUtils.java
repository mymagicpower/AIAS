package me.aias.example.utils;

import ai.djl.training.util.DownloadUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static void downloadModel() throws IOException {
        Path modelFile = Paths.get("build/tmp/model/text_classification.bin");
        if (Files.notExists(modelFile)) {
            DownloadUtils.download(
                    "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/fasttext/blazingtext_classification.bin",
                    "build/tmp/model/text_classification.bin");
        }
    }
}
