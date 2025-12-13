package org.example.antiplagiarism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {
                "org.example.antiplagiarism",
                "org.example.antiplagiarism.events",
        },
        exclude = {DataSourceAutoConfiguration.class}
)
public class AntiplagiarismApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntiplagiarismApplication.class, args);
    }

}
