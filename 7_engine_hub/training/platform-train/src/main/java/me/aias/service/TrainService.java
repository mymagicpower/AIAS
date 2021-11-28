package me.aias.service;

import me.aias.domain.TrainArgument;
import me.aias.training.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@Service
public class TrainService {

    private Logger logger = LoggerFactory.getLogger(TrainService.class);
    
	public void train(TrainArgument trainArgument, String newModelPath, String fileRootPath) throws Exception {

		Training training=new Training(trainArgument, newModelPath, fileRootPath);
		// 运行训练程序
		training.start();
	}
}
