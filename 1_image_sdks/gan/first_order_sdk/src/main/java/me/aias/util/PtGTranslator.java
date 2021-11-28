package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PtGTranslator implements Translator<List, Image> {

    public PtGTranslator() {
    }
    private NDArray copy ;
    /**
     * {@inheritDoc}
     */
    @Override
    public NDList processInput(TranslatorContext ctx, List input) {
    	NDArray source = ((Image)input.get(0)).toNDArray(ctx.getNDManager());
        source = NDImageUtils.resize(source, 256, 256);
        source = source.div(255);
        source = source.transpose(2, 0, 1);
        source = source.toType(DataType.FLOAT32,false);
        source = source.broadcast(new Shape(1,3,256,256));

    	/*Shape shape = source.getShape(); 
    	source = source.broadcast(new Shape(1,shape.get(0),shape.get(1),shape.get(2)));*/
      
        Map<String, NDArray> kp_driving = (Map<String, NDArray>) input.get(1);
        NDArray kp_driving_v = kp_driving.get("value");
        kp_driving_v = kp_driving_v.expandDims(0);
//        kp_driving_v = kp_driving_v.broadcast(new Shape(1,10,2));
        NDArray kp_driving_j = kp_driving.get("jacobian");
        kp_driving_j = kp_driving_j.expandDims(0);
//        kp_driving_j = kp_driving_j.broadcast(new Shape(1,10,2,2));
        
        
        Map<String, NDArray> kp_source = (Map<String, NDArray>) input.get(2);
        NDArray kp_source_v = kp_source.get("value");
        kp_source_v = kp_source_v.expandDims(0);
//        kp_source_v = kp_source_v.broadcast(new Shape(1,10,2));
        NDArray kp_source_j = kp_source.get("jacobian");
        kp_source_j = kp_source_j.expandDims(0);
//        kp_source_j = kp_source_j.broadcast(new Shape(1,10,2,2));
       
        
        Map<String, NDArray> kp_driving_initial = (Map<String, NDArray>) input.get(3);
        NDArray kp_initial_v = kp_driving_initial.get("value");
        kp_initial_v = kp_initial_v.expandDims(0);
//        kp_initial_v = kp_initial_v.broadcast(new Shape(1,10,2));
        NDArray kp_initial_j = kp_driving_initial.get("jacobian");
        kp_initial_j = kp_initial_j.expandDims(0);
//        kp_initial_j = kp_initial_j.broadcast(new Shape(1,10,2,2));
        
        NDList re = new NDList();
        re.add(source); 
        re.add(kp_driving_v);
        re.add(kp_driving_j);
        re.add(kp_source_v);
        re.add(kp_source_j);
        re.add(kp_initial_v);
        re.add(kp_initial_j); 
        
        return re;
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {
    	for(NDArray ig : list){
    		if(ig.getName().equals("prediction")){
    			NDArray img = ig.get(0);
    			img = img.mul(255).toType(DataType.UINT8, true);
    	    	//System.out.println(img.toDebugString(1000000000, 1000, 1000, 1000)); 
    	    	//saveBoundingBoxImage(ImageFactory.getInstance().fromNDArray(img));
    	        return ImageFactory.getInstance().fromNDArray(img);
    		} 
    	}
    	return null;
    }

    @Override
    public Batchifier getBatchifier() {
        return null;
    }

}
