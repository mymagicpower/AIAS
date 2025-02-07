package top.aias.dl4j.exercises.userinterface;

import org.deeplearning4j.core.storage.StatsStorage;
import top.aias.dl4j.exercises.userinterface.util.UIExampleUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.FileStatsStorage;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;

/**
 * 一个简单的示例，展示如何将Deeplearning4j的训练UI附加到网络
 *
 * 要更改UI端口（通常不需要） - 设置org.deeplearning4j.ui.port系统属性
 * 例如，运行示例并传递以下参数给JVM，使用9001端口: -Dorg.deeplearning4j.ui.port=9001
 *
 * @author Alex Black
 */
public class UIStorageExample {

    public static void main(String[] args){

        boolean collectStats = false;

        if(collectStats){
            // 获取我们的网络和训练数据
            MultiLayerNetwork net = UIExampleUtils.getMnistNetwork();
            DataSetIterator trainData = UIExampleUtils.getMnistData();

            // 1. 初始化用户界面后端
            UIServer uiServer = UIServer.getInstance();

            // 2. 配置网络信息（梯度、激活值、时间与得分等）存储位置
            StatsStorage statsStorage = new FileStatsStorage(new File(System.getProperty("java.io.tmpdir"), "ui-stats.dl4j"));

            // 3. 然后添加 StatsListener 以在训练过程中收集这些信息
            net.setListeners(new ScoreIterationListener(1), new StatsListener(statsStorage));

            // 4. 将 StatsStorage 实例附加到UI：这使得StatsStorage的内容能够可视化
            uiServer.attach(statsStorage);

            // 5. 开始训练：
            net.fit(trainData);

            // 6. 最后：打开浏览器并访问 http://localhost:9000/train
        } else {
            //加载状态数据. 打开浏览器并访问 http://localhost:9000/train

            StatsStorage statsStorage = new FileStatsStorage(new File(System.getProperty("java.io.tmpdir"), "ui-stats.dl4j"));
            UIServer uiServer = UIServer.getInstance();
            uiServer.attach(statsStorage);
        }
    }
}
