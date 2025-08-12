package com.coindesk.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.coindesk.demo"})
@EnableJpaAuditing
public class CoindeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoindeskApplication.class, args);
    }

}
