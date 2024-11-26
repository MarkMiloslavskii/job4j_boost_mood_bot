package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jvnet.hk2.annotations.Service;
import ru.job4j.bmb.content.Content;

@Service
public class TelegramBotService {
    private final BotCommandHandler handler;

    public TelegramBotService(BotCommandHandler handler) {
        this.handler = handler;
    }

    @PostConstruct
    public void init() {
        System.out.println("TelegramBotService инициализирован.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("TelegramBotService уничтожен.");
    }

    public void receive(Content content) {
        handler.receive(content);
    }
}
