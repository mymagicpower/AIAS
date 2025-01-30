package top.aias.training.service;

import top.aias.training.config.UIServerInstance;
import top.aias.training.domain.TrainArgument;

/**
 * 训练服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface TrainService {
	public void train(UIServerInstance uiServer, TrainArgument trainArgument, String modelPath, String savePath, String dataRootPath) throws Exception;

}
