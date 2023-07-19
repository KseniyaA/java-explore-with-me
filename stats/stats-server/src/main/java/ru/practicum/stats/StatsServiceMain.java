package ru.practicum.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication/*(scanBasePackages = "ru.practicum.stats.server")*/
public class StatsServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(StatsServiceMain.class, args);
    }
}