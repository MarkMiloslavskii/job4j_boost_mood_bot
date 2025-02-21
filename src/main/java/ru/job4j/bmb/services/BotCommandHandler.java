package ru.job4j.bmb.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.logic.MoodService;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        String text = message.getText();
        Long clientId = message.getFrom().getId();
        long chatId = message.getChatId();

        switch (text) {
            case "/start":
                return handleStartCommand(chatId, clientId);
            case "/week_mood_log":
                return handleMoodLog(clientId, "неделя");
            case "/month_mood_log":
                return handleMoodLog(clientId, "месяц");
            case "/award":
                return handleAward(clientId);
            default:
                return Optional.empty();
        }
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        try {
            var moodId = Long.parseLong(callback.getData());
            var user = userRepository.findByClientId(callback.getFrom().getId());
            return user.map(value -> moodService.chooseMood(value, moodId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        var content = new Content(user.getChatId(), "Как настроение?");
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildMoodButtons());
        return Optional.of(content);
    }

    private Optional<Content> handleMoodLog(Long clientId, String period) {
        var user = userRepository.findByClientId(clientId);
        return user.map(value -> {
            var content = new Content(value.getChatId());
            content.setText("Лог настроений за " + period + ":\n" + moodService.getMoodLog(value, period));
            return content;
        });
    }

    private Optional<Content> handleAward(Long clientId) {
        var user = userRepository.findByClientId(clientId);
        return user.map(value -> {
            var content = new Content(value.getChatId());
            content.setText("Ваши награды:\n" + moodService.getAwards(value));
            return content;
        });
    }

    public void receive(Content content) {

    }
}
