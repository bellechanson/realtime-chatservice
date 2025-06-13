package com.example.realtimechatservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class RealtimeChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeChatServiceApplication.class, args);
    }

}
