package org.oclick.services.vacancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.oclick.services.vacancy"})
public class VacancyApplication {
    public static void main(String[] args) {
        SpringApplication.run(VacancyApplication.class, args);
    }
}