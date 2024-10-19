package top.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.model.CodeModel;
import top.aias.model.code2vec.Code2VecModel;
import top.aias.model.code5p.codet5p2VecModel;
import top.aias.model.mpnet.Mpnet2VecModel;

import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Configuration
public class ModelConfiguration {
    // Text Model
    @Value("${model.modelPath}")
    private String modelPath;
    @Value("${model.modelName}")
    private String modelName;

    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    @Value("${model.maxLength}")
    private int maxLength;

    @Bean
    public CodeModel codeModel() throws IOException, ModelNotFoundException, MalformedModelException {
//        Code2VecModel textEncoderModel = new Code2VecModel();
        codet5p2VecModel textEncoderModel = new codet5p2VecModel();
//        Mpnet2VecModel textEncoderModel = new Mpnet2VecModel();
        textEncoderModel.init(modelPath, modelName, poolSize, maxLength);
        return textEncoderModel;
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}