package ru.job4j.bmb.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jvnet.hk2.annotations.Service;

@Service
public class ReminderService {

    @PostConstruct
    public void init() {
        System.out.println("RemindService инициализирован.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RemindService уничтожен.");
    }
}
