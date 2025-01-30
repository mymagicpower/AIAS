package top.aias.training.training;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import top.aias.training.config.UIServerInstance;
import top.aias.training.domain.TrainArgument;

import java.io.IOException;

/**
 * 模型训练
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class Training extends Thread {

    TrainArgument trainArgument;
    private String modelPath;
    private String savePath;
    private String dataRootPath;
    private UIServerInstance uiServer;

    public Training(UIServerInstance uiServer, TrainArgument trainArgument, String modelPath, String savePath, String dataRootPath) {
        this.trainArgument = trainArgument;
        this.modelPath = modelPath;
        this.savePath = savePath;
        this.dataRootPath = dataRootPath;
        this.uiServer = uiServer;
    }

    @Override
    public void run() {
        try {
            TrainResNet50.train(uiServer, trainArgument, modelPath, savePath, dataRootPath);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }
    }
}
