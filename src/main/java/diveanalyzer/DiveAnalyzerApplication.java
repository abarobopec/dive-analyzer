package diveanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@ComponentScan(basePackages = {"diveanalyzer.controllers", "diveanalyzer.models"})
public class DiveAnalyzerApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DiveAnalyzerApplication.class, args);
    }
}
