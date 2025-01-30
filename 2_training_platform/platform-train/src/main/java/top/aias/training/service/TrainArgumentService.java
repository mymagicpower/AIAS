package top.aias.training.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import top.aias.training.domain.TrainArgument;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * 超参数服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface TrainArgumentService {
    /**
     * 保存设置
     * save config
     */
    public void saveTrainArgument();

    /**
     * 编辑
     * edit
     *
     * @param trainArgument
     */
    public void update(TrainArgument trainArgument);


    public TrainArgument getTrainArgument();
}