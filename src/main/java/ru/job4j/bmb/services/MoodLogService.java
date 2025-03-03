package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MoodLogService {
    private final MoodLogRepository moodLogRepository;

    public MoodLogService(MoodLogRepository moodLogRepository) {
        this.moodLogRepository = moodLogRepository;
    }

    public List<MoodLog> findLogsByPeriods(Long clientId, Duration duration) {
        Instant startTime = Instant.now().minus(duration);
        return moodLogRepository.findLogsForPeriod(clientId, startTime);
    }
}
