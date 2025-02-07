package top.aias.dl4j.exercises.modelio;

import org.deeplearning4j.nn.graph.ComputationGraph;

import java.io.File;

/**
 * 加载计算图 ComputationGraph
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ComputationGraphLoad {

    public static void main(String[] args) throws Exception {
        // 模型路径
        File locationToSave = new File("MyComputationGraph.zip");

        // 载入模型
        ComputationGraph restored = ComputationGraph.load(locationToSave, true);

        System.out.println("模型参数:      " + restored.params());
        System.out.println("模型配置:  " + restored.getConfiguration());
    }

}
