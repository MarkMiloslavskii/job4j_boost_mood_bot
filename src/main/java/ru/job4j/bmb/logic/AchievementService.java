package ru.job4j.bmb.logic;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.services.SentContent;

import java.time.Instant;
import java.util.List;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodLogRepository;
    private final SentContent sentContent;

    public AchievementService(AchievementRepository achievementRepository,
                              MoodLogRepository moodLogRepository,
                              SentContent sentContent) {
        this.achievementRepository = achievementRepository;
        this.moodLogRepository = moodLogRepository;
        this.sentContent = sentContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        User user = event.getUser();
        checkAndGrantAchievements(user);
    }

    private void checkAndGrantAchievements(User user) {
        Instant weekAgo = Instant.now().minusSeconds(7 * 24 * 3600);
        List<MoodLog> logs = moodLogRepository.findLogsForPeriod(user.getId(), weekAgo);

        long positiveMoodCount = logs.stream()
                .filter(log -> log.getMood().isPositive())
                .count();

        if (positiveMoodCount >= 5) {
            grantAchievement(user, "Good Mood Streak", "Поздравляем! Вы получили достижение за хорошее настроение!");
        }
    }

    private void grantAchievement(User user, String title, String message) {
        boolean alreadyAwarded = achievementRepository.findByUserIdAndTitle(user.getId(), title).isPresent();
        if (!alreadyAwarded) {
            achievementRepository.save(new Achievement(user.getId(), title));
            sentContent.sent(new Content(user.getChatId(), message));
        }
    }
}
