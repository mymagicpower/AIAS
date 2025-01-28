package me.aias.service;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.infer.FeatureExtraction;
import me.aias.infer.ImageClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@Service
public class InferService {

    private Logger logger = LoggerFactory.getLogger(InferService.class);
    
	public String getClassificationInfo(String newModelPath, InputStream inputStream) {
		try {
			Image img = ImageFactory.getInstance().fromInputStream(inputStream);
			Classifications classifications = ImageClassification.predict(newModelPath, img);
			return classifications.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getClassificationInfoForUrl(String newModelPath, String imageUrl) {
		try {
			Image img = ImageFactory.getInstance().fromUrl(imageUrl);
			Classifications classifications = ImageClassification.predict(newModelPath, img);
			return classifications.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public float[] feature(String newModelPath,Image image) {
		try {
			float[] feature = FeatureExtraction.predict(newModelPath,image);
			return feature;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String compare(String newModelPath,Image img1, Image img2) throws ModelException, TranslateException, IOException {
		float[] feature1 = FeatureExtraction.predict(newModelPath,img1);
		float[] feature2 = FeatureExtraction.predict(newModelPath,img2);
		return Float.toString(calculSimilar(feature1, feature2));
	}

	private float calculSimilar(float[] feature1, float[] feature2) {
		float ret = 0.0f;
		float mod1 = 0.0f;
		float mod2 = 0.0f;
		int length = feature1.length;
		for (int i = 0; i < length; ++i) {
			ret += feature1[i] * feature2[i];
			mod1 += feature1[i] * feature1[i];
			mod2 += feature2[i] * feature2[i];
		}
		return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2) + 1) / 2.0f);
	}
}
