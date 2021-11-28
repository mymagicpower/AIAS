package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.HashMap;
import java.util.Map;

public class NPtKTranslator implements Translator<Image, Map> {

    public NPtKTranslator() {

    }
    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray array = input.toNDArray(ctx.getNDManager());
        array = NDImageUtils.resize(array, 256, 256);
        array = array.div(255);
        array = array.transpose(2, 0, 1);
        array = array.toType(DataType.FLOAT32,false);
//        array = array.broadcast(new Shape(1,3,256,256));
//        array = array.get(0);
        return new NDList(array);
    }

    @Override
    public Map<String, NDArray> processOutput(TranslatorContext ctx, NDList list) {
        Map map = new HashMap();
        NDArray jacobian = list.get(0);
        jacobian.detach();
        NDArray value = list.get(1);
        value.detach();
        map.put(list.get(0).getName(),jacobian);
        map.put(list.get(1).getName(),value);
        return map;
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }

}
