package top.aias.config;

import lombok.Data;
import top.aias.common.constant.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件操作常量类
 * File operation constants class
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    // 文件大小限制
    // File size limit
    private Long maxSize;

    private ElPath mac;

    private ElPath linux;

    private ElPath windows;

    public ElPath getPath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(Constants.WIN)) {
            return windows;
        } else if(os.toLowerCase().startsWith(Constants.MAC)){
            return mac;
        }
        return linux;
    }

    @Data
    public static class ElPath{

        private String path;

        private String rootPath;

    }
}
