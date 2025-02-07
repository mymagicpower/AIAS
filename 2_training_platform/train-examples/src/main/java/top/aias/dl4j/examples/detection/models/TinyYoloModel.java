package top.aias.dl4j.examples.detection.models;

import lombok.Data;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.objdetect.Yolo2OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.zoo.model.TinyYOLO;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;

import java.io.IOException;

/**
 * TinyYolo 模型
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
@Data
public final class TinyYoloModel extends BaseModel{
    public TinyYoloModel(){
    }

    public ComputationGraph build() throws IOException {

        // 步骤 1: 迁移学习步骤 - 加载 TinyYOLO 的预构建模型。
        ComputationGraph pretrained = (ComputationGraph) TinyYOLO.builder().build().initPretrained();
        INDArray priors = Nd4j.create(priorBoxes);

        // 步骤 2: 迁移学习步骤 - 模型配置。
        FineTuneConfiguration fineTuneConf = getFineTuneConfiguration();

        // 步骤 3: 迁移学习步骤 - 修改预构建模型的架构。
        ComputationGraph computationGraph = getNewComputationGraph(pretrained, priors, fineTuneConf);
        this.setComputationGraph(computationGraph);

        return computationGraph;
    }

    private FineTuneConfiguration getFineTuneConfiguration() {
        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .seed(rng.nextInt())
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                .gradientNormalizationThreshold(1.0)
                .updater(new Nesterovs(learningRate, lrMomentum))
                .activation(Activation.IDENTITY)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .inferenceWorkspaceMode(WorkspaceMode.ENABLED)
                .build();
        return fineTuneConf;
    }

    private ComputationGraph getNewComputationGraph(ComputationGraph pretrained, INDArray priors, FineTuneConfiguration fineTuneConf) {
    // https://github.com/msf4-0/DL4JRA/blob/main/server/src/main/java/com/dl4jra/server/odtraining/TinyYoloTransferLearner.java

        ComputationGraph computationGraph = new TransferLearning.GraphBuilder(pretrained)
                .fineTuneConfiguration(fineTuneConf)

                .removeVertexKeepConnections("conv2d_9")
                .addLayer("conv2d_9",
                        new ConvolutionLayer.Builder(1,1)
                                .nIn(1024).nOut(nBoxes * (5 + nClasses)).hasBias(false)
                                .stride(1,1).convolutionMode(ConvolutionMode.Same)
                                .weightInit(WeightInit.UNIFORM)
                                .activation(Activation.IDENTITY)
                                .build(),
                        "leaky_re_lu_8")

                .removeVertexKeepConnections("outputs")
                .addLayer("outputs",
                        new Yolo2OutputLayer.Builder()
                                .lambdaCoord(lambdaCoord)
                                .lambdaNoObj(lambdaNoObj)
                                .boundingBoxPriors(priors)
                                .build(),
                        "conv2d_9")
                .setInputTypes(InputType.convolutional(height, width, nChannels))
                .setOutputs("outputs")
                .build();

        return computationGraph;
    }
}