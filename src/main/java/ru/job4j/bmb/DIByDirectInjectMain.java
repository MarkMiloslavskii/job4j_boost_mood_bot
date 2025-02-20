package ru.job4j.bmb;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.BotCommandHandler;
import ru.job4j.bmb.services.TelegramBotService;

public class DIByDirectInjectMain {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("ru.job4j.bmb")) {
            var handler = context.getBean(BotCommandHandler.class);
            var tg = new TelegramBotService(handler);
            tg.receive(new Content());
        }
    }
}

