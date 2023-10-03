package ru.practicum.main.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "ru.practicum")
@SpringBootApplication
public class EwmMainService {
    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }
}