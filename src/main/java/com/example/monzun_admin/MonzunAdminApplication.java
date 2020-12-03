package com.example.monzun_admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.monzun_admin.repository")
public class MonzunAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonzunAdminApplication.class, args);
    }

}
