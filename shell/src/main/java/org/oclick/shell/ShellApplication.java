package org.oclick.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@SpringBootApplication
@ShellComponent
public class ShellApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShellApplication.class, args);
    }

    @ShellMethod("Says hello")
    public String hello(@ShellOption(valueProvider = NameCompletionProvider.class) String name) {
        return "Hello, " + name + "!";
    }
}