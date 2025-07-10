package top.aias.config;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import top.aias.model.generate.SearchConfig;
import top.aias.model.trans.TranslationModel;
import top.aias.model.vec.Code2VecModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.model.vec.CodeModel;

import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @email 179209347@qq.com
 */
@Configuration
public class ModelConfiguration {
    // 向量模型路径
    @Value("${model.vecModelPath}")
    private String vecModelPath;
    // 向量模型名称
    @Value("${model.vecModelName}")
    private String vecModelName;
    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;
    // 输入文字最大长度
    @Value("${model.maxLength}")
    private int maxLength;
    // 翻译模型路径
    @Value("${model.transModelPath}")
    private String transModelPath;
    @Bean
    public CodeModel codeModel() throws IOException, ModelNotFoundException, MalformedModelException {
        Code2VecModel textEncoderModel = new Code2VecModel();
        textEncoderModel.init(vecModelPath, vecModelName, poolSize, maxLength, Device.cpu());
        return textEncoderModel;
    }

    @Bean
    public TranslationModel translationModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TranslationModel translationModel = new TranslationModel();
        SearchConfig config = new SearchConfig();
        config.setMaxSeqLength(maxLength);

        translationModel.init(config, transModelPath, poolSize, Device.cpu());
        return translationModel;
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}