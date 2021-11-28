package me.aias.training;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import me.aias.domain.TrainArgument;

import java.io.IOException;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
public class Training extends Thread {

    TrainArgument trainArgument;
    private String newModelPath;
    private String fileRootPath;
    
    public Training(TrainArgument trainArgument, String newModelPath, String fileRootPath) {
        this.trainArgument =  trainArgument;
        this.newModelPath = newModelPath;
        this.fileRootPath = fileRootPath;
    }

    @Override
    public void run() {
        try {
            TrainResNet50V2.train(trainArgument, newModelPath, fileRootPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }
    }
}
