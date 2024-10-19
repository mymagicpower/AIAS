package top.aias.img.model.gan;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
/**
 * 人脸修复模型前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceGanTranslator implements Translator<Image, Image> {
    private int[] min_max = new int[]{-1, 1};

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
        NDManager manager = ctx.getNDManager();

        NDArray array = input.toNDArray(manager).toType(DataType.FLOAT32, false);

        array = array.transpose(2, 0, 1).div(255f); // HWC -> CHW RGB

        NDArray mean =
                ctx.getNDManager()
                        .create(
                                new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));
        NDArray std =
                ctx.getNDManager().create(new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));

        array.subi(mean).divi(std);

        return new NDList(array);
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {

        NDArray array = list.get(0);
        array = array.clip(min_max[0], min_max[1]);
        array.subi(min_max[0]).divi(min_max[1] - min_max[0]).muli(255.0f);
        array = array.round();
        array = array.toType(DataType.UINT8, false);
        return ImageFactory.getInstance().fromNDArray(array);
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
