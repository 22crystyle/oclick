package org.oclick.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@ConfigurationPropertiesScan
@SpringBootApplication
public class ShellApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShellApplication.class, args);
    }
}