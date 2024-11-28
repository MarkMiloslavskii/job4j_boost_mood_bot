package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import ru.job4j.bmb.content.Content;

@Service
public class BotCommandHandler implements BeanNameAware {

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

    @Override
    public void setBeanName(String name) {
        System.out.println("Имя бина: " + name);
    }
}
