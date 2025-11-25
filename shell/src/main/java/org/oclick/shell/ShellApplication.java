package org.oclick.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"org.oclick.shell"})
@ConfigurationPropertiesScan
public class ShellApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShellApplication.class, args);
    }
}