package me.aias.example.utils;

import ai.djl.training.util.DownloadUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static void downloadHumanData(Path path) throws IOException {
        if (Files.notExists(path)) {
            DownloadUtils.download(
                    "https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/human_data.txt",
                    path.toString());
        }
    }

    public static void downloadChimpData(Path path) throws IOException {
        if (Files.notExists(path)) {
            DownloadUtils.download(
                    "https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/chimp_data.txt",
                    path.toString());
        }
    }

    public static void downloadDogData(Path path) throws IOException {
        if (Files.notExists(path)) {
            DownloadUtils.download(
                    "https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/dog_data.txt",
                    path.toString());
        }
    }
}