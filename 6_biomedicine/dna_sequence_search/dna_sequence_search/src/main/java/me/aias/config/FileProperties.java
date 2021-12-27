package me.aias.config;

import lombok.Data;
import me.aias.common.constant.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 * @author Calvin
 * @date 2021-12-12
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /** 文件大小限制 */
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

        private String imageRootPath;

    }
}
