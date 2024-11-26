package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import ru.job4j.bmb.content.Content;

@Service
public class BotCommandHandler {

    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler инициализирован.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler уничтожен.");
    }

    public void receive(Content content) {
        System.out.println(content);
    }
}
