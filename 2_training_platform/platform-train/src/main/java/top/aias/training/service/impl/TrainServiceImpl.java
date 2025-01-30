package top.aias.training.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.aias.training.config.UIServerInstance;
import top.aias.training.domain.TrainArgument;
import top.aias.training.service.TrainArgumentService;
import top.aias.training.service.TrainService;
import top.aias.training.training.Training;

/**
 * 训练服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class TrainServiceImpl implements TrainService {

    private Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);
    
	public void train(UIServerInstance uiServer, TrainArgumentService trainArgumentService, String modelPath, String savePath, String dataRootPath){

		Training training=new Training(uiServer, trainArgumentService, modelPath, savePath, dataRootPath);
		// 运行训练程序
		// run training program
		training.start();
	}
}
