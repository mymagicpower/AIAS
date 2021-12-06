package me.calvin.modules.search.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.calvin.modules.search.common.utils.face.FaceFeatureModel;
import me.calvin.modules.search.common.utils.feature.CommonFeatureModel;
import me.calvin.modules.search.common.utils.feature.CustFeatureModel;
import me.calvin.modules.search.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private FaceFeatureModel faceFeatureModel;

    @Autowired
    private CommonFeatureModel commonFeatureModel;
    
    @Autowired
    private CustFeatureModel custFeatureModel;

    public List<Float> feature(String newModelPath, Image image) throws ModelException, TranslateException, IOException {
        float[] embeddings = custFeatureModel.predict(image);
        List<Float> feature = new ArrayList<>();
        for (int i = 0; i < embeddings.length; i++) {
            feature.add(new Float(embeddings[i]));
        }
        return feature;
    }

    public List<Float> commonFeature(Image img) throws IOException, ModelException, TranslateException {
        float[][] embeddings = null;
        embeddings = commonFeatureModel.predict(img);
        List<Float> feature = new ArrayList<>();

        if (embeddings != null) {
            for (int i = 0; i < embeddings.length; i++) {
                feature.add(new Float(embeddings[i][0]));
            }
        } else {
            return null;
        }
        return feature;
    }

    public List<Float> faceFeature(Image img) throws IOException, ModelException, TranslateException {
        float[] embeddings = null;
        embeddings = faceFeatureModel.predict(img);
        List<Float> feature = new ArrayList<>();

        if (embeddings != null) {
            for (int i = 0; i < embeddings.length; i++) {
                feature.add(new Float(embeddings[i]));
            }
        } else {
            return null;
        }
        return feature;
    }
}
