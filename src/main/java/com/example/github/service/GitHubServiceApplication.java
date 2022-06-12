package com.example.github.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GitHubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitHubServiceApplication.class, args);
    }

}
