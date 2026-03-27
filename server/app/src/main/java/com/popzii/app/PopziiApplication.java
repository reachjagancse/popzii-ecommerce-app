package com.popzii.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.popzii")
@EnableJpaRepositories(basePackages = "com.popzii")
@EntityScan(basePackages = "com.popzii")
public class PopziiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PopziiApplication.class, args);
    }
}
