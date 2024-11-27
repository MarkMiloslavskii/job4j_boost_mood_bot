package ru.job4j.bmb.config;

import org.springframework.beans.factory.annotation.Value;

public class AppConfig {

    @Value("${telegram.bot.name}")
    private String appName;
}
