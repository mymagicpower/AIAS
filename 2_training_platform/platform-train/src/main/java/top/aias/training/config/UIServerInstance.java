package top.aias.training.config;

import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.springframework.stereotype.Component;
import org.deeplearning4j.ui.model.stats.StatsListener;

/**
 * 训练UI
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class UIServerInstance {

    private org.deeplearning4j.ui.api.UIServer uiServer;
    private StatsStorage statsStorage;
    private StatsListener statsListener;
    private ComputationGraph computationGraph;

    public UIServerInstance() {
//        statsStorage = new FileStatsStorage(new File("ui-stats.dat"));
        statsStorage = new InMemoryStatsStorage();
        statsListener = new StatsListener(statsStorage);
        uiServer = org.deeplearning4j.ui.api.UIServer.getInstance();
        uiServer.attach(statsStorage);
    }

    public void init(ComputationGraph computationGraph) {
            if (computationGraph != null){
                computationGraph.getListeners().remove(statsListener);
                computationGraph.addListeners(statsListener, new ScoreIterationListener(1));
                this.computationGraph = computationGraph;
            }
            System.gc();
    }


    public void stop() throws InterruptedException {
            uiServer.stop();
    }
}