package top.aias.img.configuration;

import lombok.Data;
import top.aias.img.utils.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * File size limitation
     */
    private Long maxSize;

    private ElPath mac;

    private ElPath linux;

    private ElPath windows;

    public ElPath getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(Constants.WIN)) {
            return windows;
        } else if (os.toLowerCase().startsWith(Constants.MAC)) {
            return mac;
        }
        return linux;
    }

    @Data
    public static class ElPath {

        private String path;
    }
}
