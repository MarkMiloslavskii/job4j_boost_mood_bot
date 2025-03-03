package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.repository.UserRepository;
import ru.job4j.bmb.content.Content;

import static ru.job4j.bmb.content.Content.ContentType.TEXT;

@Service
public class RemindService {
    private final SentContent telegramBotService;
    private final UserRepository userRepository;

    public RemindService(SentContent telegramBotService, UserRepository userRepository) {
        this.telegramBotService = telegramBotService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        userRepository.findAll().forEach(user -> {
            var content = new Content(user.getChatId(), "Ping");
            content.setType(TEXT);
            telegramBotService.sent(content);
        });
    }
}