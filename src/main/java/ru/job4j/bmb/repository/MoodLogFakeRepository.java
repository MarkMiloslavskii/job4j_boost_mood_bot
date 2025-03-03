package ru.job4j.bmb.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoodLogFakeRepository extends CrudRepositoryFake<MoodLog, Long> implements MoodLogRepository {

    @Override
    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public Optional<MoodLog> findMoodById(Long moodId) {
        return Optional.empty();
    }

    @Override
    public List<MoodLog> findLogsForPeriod(Long userId, Instant startTime) {
        return List.of();
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(Long startOfDay, Long endOfDay) {
        return List.of();
    }

    @Override
    public List<MoodLog> findByUserId(Long userId) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().getId().equals(userId))
                .sorted(Comparator.comparing(MoodLog::getCreatedAt).reversed());
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {
        Set<Long> usersWhoVoted = memory.values().stream()
                .filter(moodLog -> {
                    long createdAtMillis = moodLog.getCreatedAt().toEpochMilli();
                    return createdAtMillis >= startOfDay && createdAtMillis <= endOfDay;
                })
                .map(moodLog -> moodLog.getUser().getId())
                .collect(Collectors.toSet());

        return memory.values().stream()
                .map(MoodLog::getUser)
                .distinct()
                .filter(user -> !usersWhoVoted.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoodLog> findMoodLogsForWeek(Long userId, long weekStart) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().getId().equals(userId))
                .filter(moodLog -> moodLog.getCreatedAt().toEpochMilli() >= weekStart)
                .collect(Collectors.toList());
    }

    @Override
    public List<MoodLog> findMoodLogsForMonth(Long userId, long monthStart) {
        return memory.values().stream()
                .filter(moodLog -> moodLog.getUser().getId().equals(userId))
                .filter(moodLog -> moodLog.getCreatedAt().toEpochMilli() >= monthStart)
                .collect(Collectors.toList());
    }
}
