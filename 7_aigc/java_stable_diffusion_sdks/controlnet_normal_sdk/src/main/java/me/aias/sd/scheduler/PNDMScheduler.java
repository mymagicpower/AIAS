package me.aias.sd.scheduler;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;

public class PNDMScheduler {
    private final int TRAIN_TIMESTEPS = 1000;
    private int numInferenceSteps;
    private float betaStart = 0.00085f;
    private float betaEnd = 0.012f;
    private NDManager manager;
    private NDArray betas;
    private NDArray alphas;
    private NDArray alphasCumProd;
    private NDArray finalAlphaCumProd;
    private int counter = 0;
    private NDArray curSample = null;
    private NDList ets = new NDList();
    private int stepSize;
    public NDArray timesteps;

    private PNDMScheduler() {
    }

    public PNDMScheduler(NDManager manager) {
        this.manager = manager;
        // scaled_linear
        betas = manager.linspace((float) Math.sqrt(betaStart), (float) Math.sqrt(betaEnd), TRAIN_TIMESTEPS );
        betas = betas.mul(betas);
        alphas = manager.ones(betas.getShape()).sub(betas);
        alphasCumProd = alphas.cumProd(0);
        finalAlphaCumProd = alphasCumProd.get(0);
    }

    public NDArray addNoise(NDArray latent, NDArray noise, int timesteps){
        NDArray alphaProd = alphasCumProd.get(timesteps);
        NDArray sqrtAlphaProd = alphaProd.sqrt();

        NDArray one = manager.create(new float[]{1});
        NDArray sqrtOneMinusAlphaProd = one.sub(alphaProd).sqrt();

        latent = latent.mul(sqrtAlphaProd).add(noise.mul(sqrtOneMinusAlphaProd));
        return latent;
    }

    public void setTimesteps(int inferenceSteps, int offset) {
        numInferenceSteps = inferenceSteps;
        stepSize = TRAIN_TIMESTEPS / numInferenceSteps;
        timesteps = manager.arange(0, numInferenceSteps).mul(stepSize).add(offset);
        // np.concatenate([self._timesteps[:-1], self._timesteps[-2:-1], self._timesteps[-1:]])[::-1]
        NDArray part1 = timesteps.get(new NDIndex(":-1"));
        NDArray part2 = timesteps.get(new NDIndex("-2:-1"));
        NDArray part3 = timesteps.get(new NDIndex("-1:"));
        NDList list = new NDList();
        list.add(part1);
        list.add(part2);
        list.add(part3);
        // [::-1]
        timesteps = NDArrays.concat(list).flip(0);
    }

    public NDArray step(NDArray modelOutput, NDArray timestep, NDArray sample) {
        NDArray prevTimestep = manager.create(timestep.getLong() - stepSize);
        if (counter != 1) {
            ets.add(modelOutput);
        } else {
            prevTimestep = timestep.duplicate();
            timestep.add(-stepSize);
        }

        if (ets.size() == 1 && counter == 0) {
            curSample = sample;
        } else if (ets.size() == 1 && counter == 1) {
            modelOutput = modelOutput.add(ets.get(0)).div(2);
            sample = curSample;
            curSample = null;
        } else if (ets.size() == 2) {
            NDArray firstModel = ets.get(ets.size() - 1).mul(3);
            NDArray secondModel = ets.get(ets.size() - 2).mul(-1);
            modelOutput = firstModel.add(secondModel);
            modelOutput = modelOutput.div(2);
        } else if (ets.size() == 3) {
            NDArray firstModel = ets.get(ets.size() - 1).mul(23);
            NDArray secondModel = ets.get(ets.size() - 2).mul(-16);
            NDArray thirdModel = ets.get(ets.size() - 3).mul(5);
            modelOutput = firstModel.add(secondModel).add(thirdModel);
            modelOutput = modelOutput.div(12);
        } else {
            NDArray firstModel = ets.get(ets.size() - 1).mul(55);
            NDArray secondModel = ets.get(ets.size() - 2).mul(-59);
            NDArray thirdModel = ets.get(ets.size() - 3).mul(37);
            NDArray fourthModel = ets.get(ets.size() - 4).mul(-9);
            modelOutput = firstModel.add(secondModel).add(thirdModel).add(fourthModel);
            modelOutput = modelOutput.div(24);
        }

        NDArray prevSample = getPrevSample(sample, timestep, prevTimestep, modelOutput);
        prevSample.setName("prev_sample");
        counter++;

        return prevSample;
    }

    private NDArray getPrevSample(NDArray sample, NDArray timestep, NDArray prevTimestep, NDArray modelOutput) {
        NDArray alphaProdT = alphasCumProd.get(timestep);

        NDArray alphaProdTPrev;
        if (prevTimestep.getLong() >= 0) {
            alphaProdTPrev = alphasCumProd.get(prevTimestep);
        } else {
            alphaProdTPrev = finalAlphaCumProd;
        }

        NDArray one = manager.create(new float[]{1});
        NDArray betaProdT = one.sub(alphaProdT);
        NDArray betaProdTPrev = one.sub(alphaProdTPrev);

        NDArray sampleCoeff = alphaProdTPrev.div(alphaProdT).sqrt();

        NDArray partA = betaProdTPrev.sqrt().mul(alphaProdT);
        NDArray partB = alphaProdT.mul(betaProdT).mul(alphaProdTPrev).sqrt();

        NDArray modelOutputCoeff = partA.add(partB);

        sample = sample.mul(sampleCoeff);
        modelOutput = modelOutput.mul(alphaProdTPrev.sub(alphaProdT));
        modelOutput = modelOutput.div(modelOutputCoeff);
        modelOutput = modelOutput.neg();
        return sample.add(modelOutput);
    }
}
