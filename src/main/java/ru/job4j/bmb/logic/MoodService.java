package ru.job4j.bmb.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jvnet.hk2.annotations.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.recommendation.RecommendationEngine;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Object mood = moodLogRepository.findMoodById(moodId)
                .orElseThrow(() -> new IllegalArgumentException("Mood not found"));
        moodLogRepository.save(new MoodLog(user.getId(), mood));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        List<MoodLog> weekLogs = moodLogRepository.findLogsForPeriod(clientId, Duration.ofDays(7));
        String message = formatMoodLogs(weekLogs, "Mood statistics for the week");
        var content = new Content(chatId, message);
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        List<MoodLog> monthLogs = moodLogRepository.findLogsForPeriod(clientId, Duration.ofDays(30));
        String message = formatMoodLogs(monthLogs, "Mood statistics for the month");
        var content = new Content(chatId, message);
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        List<MoodLog> logs = moodLogRepository.findLogsForPeriod(clientId, Duration.ofDays(7));
        long positiveMoodCount = logs.stream()
                .filter(log -> log.getMood().isPositive())
                .count();

        if (positiveMoodCount >= 5) {
            achievementRepository.save(new Achievement(clientId, "Good Mood Streak"));
            String message = "Congratulations! You have been awarded for maintaining a positive mood.";
            var content = new Content(chatId, message);
            return Optional.of(content);
        }

        String message = "Keep it up! Maintain a positive mood to earn rewards.";
        var content = new Content(chatId, message);
        return Optional.of(content);
    }
}

