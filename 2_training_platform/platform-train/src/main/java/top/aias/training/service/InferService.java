package top.aias.training.service;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.training.infer.FeatureExtraction;
import top.aias.training.infer.ImageClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * 推理服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface InferService {
	public String getClassificationInfo(String newModelPath, InputStream inputStream);
	
	public String getClassificationInfoForUrl(String newModelPath, String imageUrl);

	public float[] feature(String newModelPath,Image image);

	public String compare(String newModelPath,Image img1, Image img2) throws ModelException, TranslateException, IOException ;

	public float calculSimilar(float[] feature1, float[] feature2);
}
