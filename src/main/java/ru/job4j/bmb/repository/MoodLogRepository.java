package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    Optional<Object> findMoodById(Long moodId);

    List<MoodLog> findLogsForPeriod(Long clientId, Duration duration);
}
