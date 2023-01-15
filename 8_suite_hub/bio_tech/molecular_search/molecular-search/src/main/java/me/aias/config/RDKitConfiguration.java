package me.aias.config;

import me.aias.common.rdkit.RDKitInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RDKit配置
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Configuration
public class RDKitConfiguration {
    @Bean
    public RDKitInstance rdKit() {
        RDKitInstance rdKit = new RDKitInstance();
        return rdKit;
    }
}