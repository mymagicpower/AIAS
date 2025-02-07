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
import org.deeplearning4j.zoo.model.YOLO2;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;

/**
 * Yolo2 模型
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
@Data
public final class Yolo2Model extends BaseModel{

    public Yolo2Model(){
    }

    public ComputationGraph build() throws IOException {

        // 步骤 1: 迁移学习步骤 - 加载 TinyYOLO 的预构建模型。
        ComputationGraph pretrained = (ComputationGraph) YOLO2.builder().build().initPretrained();
        INDArray priors = Nd4j.create(priorBoxes);

        // 步骤 2: 迁移学习步骤 - 模型配置。
        FineTuneConfiguration fineTuneConf = getFineTuneConfiguration();

        // 步骤 3: 迁移学习步骤 - 修改预构建模型的架构。
        computationGraph = getNewComputationGraph(pretrained, priors, fineTuneConf);
        return computationGraph;
    }

    private FineTuneConfiguration getFineTuneConfiguration() {

        FineTuneConfiguration fineTuneConf =  new FineTuneConfiguration.Builder()
                .seed(rng.nextInt())
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                .gradientNormalizationThreshold(1.0)
                .updater(new Adam.Builder().learningRate(learningRate).build())
                .l2(0.00001)
                .activation(Activation.IDENTITY)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .inferenceWorkspaceMode(WorkspaceMode.ENABLED)
                .build();

        return fineTuneConf;
    }

    private ComputationGraph getNewComputationGraph(ComputationGraph pretrained, INDArray priors, FineTuneConfiguration fineTuneConf) {
       // https://github.com/muhdlaziem/TrainingLabs/blob/1fd314543e4e6589b33f75506862b0471971d9b9/dl4j-cv-labs/src/main/java/ai/certifai/solution/object_detection/MetalDefectsDetector/MetalSurfaceDefectDetector_YOLOv2.java#L124

        ComputationGraph computationGraph =  new TransferLearning.GraphBuilder(pretrained)
                .fineTuneConfiguration(fineTuneConf)
                .removeVertexKeepConnections("conv2d_23")
                .removeVertexKeepConnections("outputs")
                .addLayer("convolution2d_23",
                        new ConvolutionLayer.Builder(1, 1)
                                .nIn(1024)
                                .nOut(nBoxes * (5 + nClasses))
                                .stride(1, 1)
                                .convolutionMode(ConvolutionMode.Same)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.IDENTITY)
                                .build(),
                        "leaky_re_lu_22")
                .addLayer("outputs",
                        new Yolo2OutputLayer.Builder()
                                .lambdaNoObj(lambdaNoObj)
                                .lambdaCoord(lambdaCoord)
                                .boundingBoxPriors(priors.castTo(DataType.FLOAT))
                                .build(),
                        "convolution2d_23")
                .setOutputs("outputs")
                .setInputTypes(InputType.convolutional(height, width, nChannels))
                .build();

        return computationGraph;
    }
}