package com.example.actors.wordcount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.example.actors.*")
public class CustomActorRunnerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CustomActorRunnerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            log.info("---------APPLICATION STARTED---------");

        } catch (Exception e) {
            log.error("Unable to process incoming POST payload from Server() to do computation using Actors ", e);
        }
    }
}



