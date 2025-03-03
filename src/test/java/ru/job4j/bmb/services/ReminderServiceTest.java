package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodFakeRepository;
import ru.job4j.bmb.repository.MoodLogFakeRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderServiceTest {

    interface SentContent {
        void sent(Content content);
    }

    @Test
    void whenMoodGood() {
        List<Content> result = new ArrayList<>();
        SentContent sentContent = result::add;

        MoodFakeRepository moodRepository = new MoodFakeRepository();
        moodRepository.save(new Mood("Good", true));

        MoodLogFakeRepository moodLogRepository = new MoodLogFakeRepository();
        User user = new User();
        user.setChatId(100L);

        MoodLog moodLog = new MoodLog();
        moodLog.setUser(user);
        long yesterday = LocalDate.now().minusDays(10)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        moodLog.setCreatedAt(Instant.ofEpochMilli(yesterday));
        moodLogRepository.save(moodLog);

        TgUI tgUI = new TgUI();
        new ReminderService((ru.job4j.bmb.services.SentContent) sentContent, moodLogRepository, tgUI).remindUsers();

        assertThat(result)
                .isNotEmpty()
                .extracting(c -> c.getMarkup().getKeyboard().get(0).get(0).getText())
                .contains("Good");
    }
}
