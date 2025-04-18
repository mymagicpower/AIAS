package top.aias.platform.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

public interface ImgSdService {
    Image txt2Image(String prompt, String negativePrompt, int steps) throws TranslateException;

    Image image2Image(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdCanny(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdDepth(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdLineartAnime(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdLineart(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdMlsd(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdNormalBae(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdOpenPose(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdP2P(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdScribble(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdSeg(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdShuffle(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;

    Image sdSoftEdge(Image image, String prompt, String negativePrompt, int steps) throws TranslateException;
}
