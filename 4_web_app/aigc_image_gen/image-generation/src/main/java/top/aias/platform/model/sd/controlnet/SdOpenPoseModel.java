package top.aias.platform.model.sd.controlnet;

import ai.djl.Device;

/**
 * ControlNet 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SdOpenPoseModel extends BaseModel {
    public SdOpenPoseModel(String root, String modelName, int poolSize, Device device) {
        super(root, modelName, poolSize, device);
    }
}
