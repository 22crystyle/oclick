package org.oclick.services.vacancy.config;

import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfiguration {

    @Bean
    public SpringPluginManager pluginManager(ApplicationContext applicationContext) {
        SpringPluginManager springPluginManager = new SpringPluginManager();
        springPluginManager.setApplicationContext(applicationContext);
        return springPluginManager;
    }
}
