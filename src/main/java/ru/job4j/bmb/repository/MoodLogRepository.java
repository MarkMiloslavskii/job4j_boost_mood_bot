package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    Optional<MoodLog> findMoodById(Long moodId);

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :startTime")
    List<MoodLog> findLogsForPeriod(Long userId, Instant startTime);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT m.user.id FROM MoodLog m WHERE m.createdAt BETWEEN :startOfDay AND :endOfDay)")
    List<User> findUsersWhoDidNotVoteToday(Long startOfDay, Long endOfDay);

    List<MoodLog> findByUserId(Long userId);

    Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);

    List<MoodLog> findMoodLogsForWeek(Long userId, long weekStart);

    List<MoodLog> findMoodLogsForMonth(Long userId, long monthStart);
}