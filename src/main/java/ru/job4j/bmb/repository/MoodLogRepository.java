package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    Optional<MoodLog> findMoodById(Long moodId);

    @Query("SELECT m FROM MoodLog m WHERE m.clientId = :clientId AND m.timestamp >= :startTime")
    List<MoodLog> findLogsForPeriod(Long clientId, Instant startTime);
}
